package de.vovchello.quarkus.internal.taxifinder.api;

import java.util.Collection;
import java.util.Set;

import de.vovchello.quarkus.internal.db.api.Taxi;

public interface TaxiFinder {
    Collection<Taxi> findTaxiByNameContains(String searchedText);

    Collection<Taxi> findAvailableTaxi();

    Set<Taxi> findAllTaxi();
}
