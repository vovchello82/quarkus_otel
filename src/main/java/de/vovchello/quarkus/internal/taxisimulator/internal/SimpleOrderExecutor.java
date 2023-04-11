package de.vovchello.quarkus.internal.taxisimulator.internal;

import org.jboss.logging.Logger;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.taxibooker.api.TaxiBooker;
import de.vovchello.quarkus.internal.taxisimulator.api.OrderExecutor;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class SimpleOrderExecutor implements OrderExecutor {
    private final Logger logger = Logger.getLogger(SimpleOrderExecutor.class);
    private final TaxiBooker taxiBooker;

    private final Order order;

    @WithSpan
    public SimpleOrderExecutor(TaxiBooker taxiBooker, Order order) {
        this.taxiBooker = taxiBooker;
        this.order = order;
    }

    @Override
    @WithSpan
    public void executeOrder() {
        int executionTime = Math.abs(order.from.codePoints().sum() - order.to.codePoints().sum());
        try {
            logger.infof("Execution time %d starting for order %s", executionTime, order);
            Thread.sleep(100 * executionTime);
            taxiBooker.finishOrder(order);
            logger.infof("execution compeleted for %s", order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @WithSpan
    public void run() {
        executeOrder();
    }

}
