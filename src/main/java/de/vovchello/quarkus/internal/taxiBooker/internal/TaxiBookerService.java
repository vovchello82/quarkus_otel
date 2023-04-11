package de.vovchello.quarkus.internal.taxibooker.internal;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.google.common.base.Objects;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.db.api.WriteTaxi;
import de.vovchello.quarkus.internal.taxibooker.api.TaxiBooker;
import de.vovchello.quarkus.internal.taxifinder.api.TaxiFinder;
import io.opentelemetry.instrumentation.annotations.WithSpan;

@Dependent
public class TaxiBookerService implements TaxiBooker {
    @Inject
    Logger logger;

    private final TaxiFinder taxiFinder;
    private final WriteTaxi writer;

    public TaxiBookerService(TaxiFinder taxiFinder, WriteTaxi writer) {
        this.taxiFinder = taxiFinder;
        this.writer = writer;
    }

    @Override
    @WithSpan
    public Optional<Taxi> placeOrder(Order order) {
        WriteLock wl = writer.getWriteLock();
        wl.lock();
        try {
            Optional<Taxi> taxi = taxiFinder.findAvailableTaxi().stream().findAny();
            if (taxi.isEmpty()) {
                logger.info("no availble taxi found");
                return Optional.empty();
            }
            taxi.get().setOrder(Optional.of(order));
            logger.infof("taxi %s booked for order %s", taxi.get(), order);
            return taxi;
        } catch (Exception e) {
            logger.error("unexpected error", e);
        } finally {
            wl.unlock();
        }
        return Optional.empty();
    }

    @Override
    @WithSpan
    public Optional<Taxi> finishOrder(Order order) {
        WriteLock wl = writer.getWriteLock();
        wl.lock();
        try {
            logger.infof("finishing order %s", order);
            List<Taxi> bookedTaxis = taxiFinder.findAllTaxi().stream().filter(t -> t.getOrder().isPresent())
                    .filter(t -> Objects.equal(order.id, t.getOrder().get().id)).collect(Collectors.toList());
            if (bookedTaxis.size() > 1) {
                throw new IllegalStateException("multiple taxis for one order");
            } else if (bookedTaxis.isEmpty()) {
                logger.errorf("no booked taxi for the order %s found", order);
                return Optional.empty();
            }
            Taxi taxi = bookedTaxis.get(0);
            taxi.setOrder(Optional.empty());
            this.writer.updateTaxi(taxi);
            return Optional.of(taxi);
        } catch (Exception e) {
            logger.error("unexpected error", e);
        } finally {
            wl.unlock();
        }

        return Optional.empty();
    }

}
