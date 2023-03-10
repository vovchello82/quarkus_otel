package de.vovchello.quarkus.internal.db.api;

import java.util.Collection;
import java.util.Optional;

public interface ReadTaxi {
    Optional<Taxi> getTaxiById(String id);

    Optional<Taxi> getTaxiByName(String name);

    Collection<Taxi> getAllTaxies();
}