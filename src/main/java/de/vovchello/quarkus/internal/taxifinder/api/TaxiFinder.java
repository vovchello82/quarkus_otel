package de.vovchello.quarkus.internal.taxifinder.api;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import de.vovchello.quarkus.internal.db.api.Taxi;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public interface TaxiFinder {
    Collection<Taxi> findTaxiByNameContains(String searchedText);

    Collection<Taxi> findAvailableTaxi();

    @WithSpan
    Set<Taxi> findAllTaxi();

    Optional<Taxi> findTaxiById(String id);
}
