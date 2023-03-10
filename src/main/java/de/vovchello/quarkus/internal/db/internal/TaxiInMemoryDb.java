package de.vovchello.quarkus.internal.db.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.db.api.WriteTaxi;
import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
class TaxiInMemoryDb implements ReadTaxi, WriteTaxi {
    private Map<String, Taxi> db = new HashMap<>();

    TaxiInMemoryDb() {
        Taxi taxi1 = new Taxi("1", "taxi1", true);
        db.put(taxi1.id, taxi1);
        Taxi taxi2 = new Taxi("2", "taxi2", true);
        db.put(taxi2.id, taxi2);
        Taxi taxi3 = new Taxi("3", "taxi3", false);
        db.put(taxi3.id, taxi3);
    }

    @Override
    public Optional<Taxi> getTaxiById(String id) {
        return Optional.ofNullable(db.get(id));
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