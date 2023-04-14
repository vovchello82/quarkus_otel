package de.vovchello.quarkus.internal.order.internal;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.order.api.OrderIncoming;
import de.vovchello.quarkus.internal.order.api.OrderResult;
import de.vovchello.quarkus.internal.taxibooker.api.TaxiBooker;
import de.vovchello.quarkus.internal.taxisimulator.internal.SimpleOrderExecutor;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.ListCommands;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@Startup
public class OrderIncomingRedis implements OrderIncoming, Runnable {
    private final Logger logger;

    private final RedisConfiguration redisConfiguration;
    private final TaxiBooker taxiBooker;
    private final PubSubCommands<OrderResult> publisher;
    private final ListCommands<String, String> queue;
    private volatile boolean stopped;
    private final ExecutorService exec = Executors.newCachedThreadPool();

    public OrderIncomingRedis(Logger logger, TaxiBooker taxiBooker, RedisDataSource rds,
            RedisConfiguration redisConfig) {
        this.logger = logger;
        this.taxiBooker = taxiBooker;
        this.queue = rds.list(String.class, String.class);
        this.publisher = rds.pubsub(OrderResult.class);
        this.redisConfiguration = redisConfig;
    }

    public void start(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    public void stop(@Observes ShutdownEvent ev) {
        stopped = true;
    }

    @Override
    public void run() {
        logger.infof("starting listening to %s", redisConfiguration.incomingQueue());
        while (!stopped) {
            final KeyValue<String, String> incomingOrder = queue.brpop(Duration.ofSeconds(1),
                    redisConfiguration.incomingQueue());

            if (incomingOrder != null) {
                var order = incomingOrder.value();
                logger.infof("incoming order %s", order);
                var ctx = GlobalOpenTelemetry.getPropagators().getTextMapPropagator()
                        .extract(Context.current(), order, new TextMapGetter<String>() {

                            @Override
                            public Iterable<String> keys(String carrier) {
                                return Set.of("traceparent");
                            }

                            @Override
                            public String get(String carrier, String key) {
                                ObjectMapper om = new ObjectMapper();
                                try {
                                    logger.infof("get key %s", key);
                                    JsonNode node = om.readTree(carrier);

                                    return node == null || node.isNull() ? "" : node.get(key).asText();
                                } catch (Exception e) {
                                    logger.error("error on key extraction", e);
                                }
                                return "";
                            }

                        });
                try (Scope scope = ctx.makeCurrent()) {

                    Span span = GlobalOpenTelemetry.getTracerProvider()
                            .get("application")
                            .spanBuilder("incoming order").setParent(ctx).startSpan();
                    try (Scope ignored = span.makeCurrent()) {
                        ObjectMapper om = new ObjectMapper()
                                .configure(Feature.IGNORE_UNKNOWN, true)
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, stopped);
                        Order orederObj = om.readValue(order, Order.class);
                        Optional<OrderResult> result = orderIncoming(orederObj);

                        result.ifPresent(r -> publisher.publish(redisConfiguration.resultChannel(), r));
                        if (result.isPresent()) {
                            Context.taskWrapping(exec).execute(new SimpleOrderExecutor(taxiBooker, orederObj));
                        }
                    } catch (Exception e) {
                        logger.error("unexpected excpetion", e);
                    } finally {
                        span.end();
                    }
                }
            }
        }
    }

    @Override
    @WithSpan
    public Optional<OrderResult> orderIncoming(Order o) {
        Optional<Taxi> taxi = taxiBooker.placeOrder(o);
        return taxi.map(t -> {
            logger.info("order placed by: " + t);
            return Optional.of(new OrderResult(o.id, t.getId()));
        }).orElse(Optional.empty());
    }

}
