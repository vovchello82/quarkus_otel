package de.vovchello.quarkus.internal.db.internal;

import static de.vovchello.quarkus.internal.db.internal.TaxiEntityConverter.taxiEntityFromTaxi;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.db.api.WriteTaxi;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
class TaxiInMemoryDb implements ReadTaxi, WriteTaxi {
    private final static Logger LOGGER = Logger.getLogger(TaxiInMemoryDb.class);

    private Map<String, TaxiEntity> db = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    TaxiInMemoryDb() {
        LOGGER.info("creation");
        TaxiEntity taxi1 = new TaxiEntity("1", "taxi1", true);
        db.put(taxi1.getId(), taxi1);
        TaxiEntity taxi2 = new TaxiEntity("2", "taxi2", true);
        db.put(taxi2.getId(), taxi2);
        TaxiEntity taxi3 = new TaxiEntity("3", "taxi3", false);
        db.put(taxi3.getId(), taxi3);
    }

    @Override
    @WithSpan
    public Optional<Taxi> getTaxiById(String id) {
        try {
            MDC.put("itemId", id);
            LOGGER.infof("get item by id %s", id);

            if (!db.containsKey(id)) {
                return Optional.empty();
            }
            return Optional.ofNullable(TaxiEntityConverter.taxiEntityToTaxi(db.get(id)));
        } finally {
            MDC.remove("itemId");
        }
    }

    @Override
    public Optional<Taxi> getTaxiByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTaxiByName'");
    }

    @Override
    public Set<Taxi> getAllTaxies() {
        LOGGER.infof("get all items");
        return db.values().stream().map(TaxiEntityConverter::taxiEntityToTaxi).collect(Collectors.toSet());
    }

    @Override
    public void addTaxi(Taxi taxi) {
        if (!db.containsKey(taxi.getId())) {
            TaxiEntity entity = taxiEntityFromTaxi(taxi);
            db.put(entity.getId(), entity);
        }
    }

    @Override
    public void updateTaxi(Taxi taxi) {
        LOGGER.infof("update taxi %s", taxi);
        if (db.containsKey(taxi.getId())) {
            TaxiEntity entity = taxiEntityFromTaxi(taxi);
            db.put(entity.getId(), entity);
        }
    }

    @Override
    public void deleteTaxiById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTaxiById'");
    }

    @Override
    public WriteLock getWriteLock() {
        return lock.writeLock();
    }

    @Override
    public ReadLock getReadLock() {
        return lock.readLock();
    }

}