package de.vovchello.quarkus.internal.order.internal;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.order.api.OrderIncoming;
import de.vovchello.quarkus.internal.order.api.OrderResult;
import de.vovchello.quarkus.internal.taxibooker.api.TaxiBooker;
import de.vovchello.quarkus.internal.taxisimulator.internal.SimpleOrderExecutor;
import io.opentelemetry.context.Context;
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

    private final RedisConfiguration redisConfig;
    private final TaxiBooker taxiBooker;
    private final PubSubCommands<OrderResult> publisher;
    private final ListCommands<String, Order> queue;
    private volatile boolean stopped;
    private final Executor exec = Executors.newCachedThreadPool();

    public OrderIncomingRedis(Logger logger, TaxiBooker taxiBooker, RedisDataSource rds,
            RedisConfiguration redisConfig) {
        this.logger = logger;
        this.taxiBooker = taxiBooker;
        this.queue = rds.list(String.class, Order.class);
        this.publisher = rds.pubsub(OrderResult.class);
        this.redisConfig = redisConfig;
    }

    public void start(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    public void stop(@Observes ShutdownEvent ev) {
        stopped = true;
    }

    @Override
    public void run() {
        logger.infof("starting listening to %s", redisConfig.getIncomingQueue());
        while (!stopped) {
            final KeyValue<String, Order> incomingOrder = queue.brpop(Duration.ofSeconds(1),
                    redisConfig.getIncomingQueue());

            if (incomingOrder != null) {
                var order = incomingOrder.value();
                logger.infof("incoming order %s", order);
                Optional<OrderResult> result = orderIncoming(order);
                result.ifPresent(r -> publisher.publish(redisConfig.getResultChannel(), r));
                if (result.isPresent()) {
                    Context.taskWrapping(exec).execute(new SimpleOrderExecutor(taxiBooker, order));
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
