package de.vovchello.quarkus.internal.order.internal;

import java.time.Duration;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.order.api.OrderIncoming;
import de.vovchello.quarkus.internal.order.api.OrderResult;
import de.vovchello.quarkus.internal.taxiBooker.api.TaxiBooker;
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
    private static final String QUEUE = "orders";
    private static final String RESULT_CHANNEL = "orders-result";

    private final Logger logger;

    private final TaxiBooker taxiBooker;
    private final PubSubCommands<OrderResult> publisher;
    private final ListCommands<String, Order> queue;
    private volatile boolean stopped;

    public OrderIncomingRedis(Logger logger, TaxiBooker taxiBooker, RedisDataSource rds) {
        this.logger = logger;
        this.taxiBooker = taxiBooker;
        this.queue = rds.list(String.class, Order.class);
        this.publisher = rds.pubsub(OrderResult.class);
    }

    public void start(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    public void stop(@Observes ShutdownEvent ev) {
        stopped = true;
    }

    @Override
    public void run() {
        logger.infof("starting listening to %s", QUEUE);
        while (!stopped) {
            final KeyValue<String, Order> item = queue.brpop(Duration.ofSeconds(1), QUEUE);

            if (item != null) {
                var o = item.value();
                orderIncoming(o).ifPresent(result -> publisher.publish(RESULT_CHANNEL, result));
            }
        }
    }

    @Override
    public Optional<OrderResult> orderIncoming(Order o) {
        Optional<Taxi> taxi = taxiBooker.placeOrder(o);

        return taxi.map(t -> {
            logger.info("order placed by: " + t);
            return Optional.of(new OrderResult(o.id, t.id));
        }).orElse(Optional.empty());
    }

}
