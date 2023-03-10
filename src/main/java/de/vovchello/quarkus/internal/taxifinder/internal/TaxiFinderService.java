package de.vovchello.quarkus.internal.taxifinder.internal;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.taxifinder.api.TaxiFinder;

@Dependent
class TaxiFinderService implements TaxiFinder {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAvailableTaxi'");
    }

    @Override
    public Set<Taxi> findAllTaxi() {
        return readTaxi.getAllTaxies();
    }

}
