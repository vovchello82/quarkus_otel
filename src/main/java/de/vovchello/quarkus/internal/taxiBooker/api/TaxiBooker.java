package de.vovchello.quarkus.internal.taxiBooker.api;

import java.util.Optional;

import de.vovchello.quarkus.internal.db.api.Order;
import de.vovchello.quarkus.internal.db.api.Taxi;

public interface TaxiBooker {
    Optional<Taxi> placeOrder(Order order);

    Optional<Taxi> finishOrder(Order order);
}
