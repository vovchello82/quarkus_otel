package de.vovchello.quarkus.internal.db.api;

public interface WriteTaxi {
    void addTaxi(Taxi taxi);

    void updateTaxi(Taxi taxi);

    void deleteTaxiById(String id);
}
