package de.vovchello.quarkus.internal.taxifinder.internal;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.taxifinder.api.TaxiFinder;

@Dependent
class TaxiFinderService implements TaxiFinder {
    @Inject
    Logger logger;

    private final ReadTaxi readTaxi;

    public TaxiFinderService(ReadTaxi readTaxi) {
        this.readTaxi = readTaxi;
    }

    @Override
    public Collection<Taxi> findTaxiByNameContains(String searchedText) {
        return readTaxi.getAllTaxies().stream().filter(t -> t.name.contains(searchedText)).collect(Collectors.toSet());
    }

    @Override
    public Collection<Taxi> findAvailableTaxi() {
        return readTaxi.getAllTaxies().stream()
                .filter(t -> t.isAvailable && (t.order == null || t.order.isEmpty()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Taxi> findAllTaxi() {
        logger.info("findAllTaxi");
        return readTaxi.getAllTaxies();
    }

    @Override
    public Optional<Taxi> findTaxiById(String id) {
        return readTaxi.getTaxiById(id);
    }

    @Override
    public Optional<Taxi> findTaxiByName(String name) {
        logger.info("findTaxiByName");
        return readTaxi.getTaxiByName(name);
    }
}
