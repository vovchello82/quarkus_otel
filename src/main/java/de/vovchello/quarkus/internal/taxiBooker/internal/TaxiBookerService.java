package de.vovchello.quarkus.internal.taxiBooker.internal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.google.common.base.Objects;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.taxiBooker.api.TaxiBooker;
import de.vovchello.quarkus.internal.taxifinder.api.TaxiFinder;

@Dependent
public class TaxiBookerService implements TaxiBooker {
    @Inject
    Logger logger;

    private final TaxiFinder taxiFinder;

    public TaxiBookerService(TaxiFinder taxiFinder) {
        this.taxiFinder = taxiFinder;
    }

    @Override
    public Optional<Taxi> placeOrder(Order order) {
        Optional<Taxi> taxi = taxiFinder.findAvailableTaxi().stream().findAny();
        if (taxi.isEmpty()) {
            logger.info("no availble taxi found");
            return Optional.empty();
        }
        taxi.get().order = Optional.of(order);
        logger.info("taxi booked: " + taxi.get().name);
        return taxi;
    }

    @Override
    public Optional<Taxi> finishOrder(Order order) {
        List<Taxi> bookedTaxis = taxiFinder.findAllTaxi().stream().filter(t -> t.order.isPresent())
                .filter(t -> Objects.equal(order.id, t.order.get().id)).collect(Collectors.toList());
        if (bookedTaxis.size() > 1) {
            throw new IllegalStateException("multiple taxis for one order");
        } else if (bookedTaxis.isEmpty()) {
            logger.info("no booked taxi for the order found");
            return Optional.empty();
        }
        Taxi taxi = bookedTaxis.get(0);
        taxi.order = Optional.empty();
        logger.info("taxi released: " + taxi.name);

        return Optional.of(taxi);
    }

}
