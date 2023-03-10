package de.vovchello.quarkus.internal.db.api;

import java.util.Optional;
import java.util.Set;

public interface ReadTaxi {
    Optional<Taxi> getTaxiById(String id);

    Optional<Taxi> getTaxiByName(String name);

    Set<Taxi> getAllTaxies();
}