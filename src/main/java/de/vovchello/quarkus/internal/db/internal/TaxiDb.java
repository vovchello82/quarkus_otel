package de.vovchello.quarkus.internal.db.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.db.api.WriteTaxi;

@ApplicationScoped
class TaxiDb implements ReadTaxi, WriteTaxi {
    private Map<String, Taxi> db = new HashMap<>();

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
    public Set<Taxi> getAllTaxies() {
        return Set.copyOf(db.values());
    }

    @Override
    public synchronized void addTaxi(Taxi taxi) {
        db.putIfAbsent(taxi.id, taxi);
    }

    @Override
    public synchronized void updateTaxi(Taxi taxi) {
        if (db.containsKey(taxi.id)) {
            db.put(taxi.id, taxi);
        }
    }

    @Override
    public synchronized void deleteTaxiById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTaxiById'");
    }

}