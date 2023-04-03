package de.vovchello.quarkus.internal.order.api;

import java.util.Optional;

import de.vovchello.quarkus.internal.db.api.Order;

public interface OrderIncoming {
    Optional<OrderResult> orderIncoming(Order o);
}
