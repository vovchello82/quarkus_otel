package de.vovchello.quarkus.internal.db.api;

import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public interface WriteTaxi {
    void addTaxi(Taxi taxi);

    void updateTaxi(Taxi taxi);

    void deleteTaxiById(String id);

    WriteLock getWriteLock();
}
