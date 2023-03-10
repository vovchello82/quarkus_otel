package de.vovchello.quarkus.internal.db.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;

@Singleton
class TaxiDb implements ReadTaxi {
    private Map<String, Taxi> db = new ConcurrentHashMap<>();

    @Override
    public Optional<Taxi> getTaxiById(String id) {
        return Optional.of(db.get(id));
    }

    @Override
    public Optional<Taxi> getTaxiByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTaxiByName'");
    }

    @Override
    public Collection<Taxi> getAllTaxies() {
        return db.values();
    }

}