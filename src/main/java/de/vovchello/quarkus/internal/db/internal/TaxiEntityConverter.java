package de.vovchello.quarkus.internal.db.internal;

import de.vovchello.quarkus.internal.db.api.Taxi;

class TaxiEntityConverter {
    private TaxiEntityConverter() {

    }

    static TaxiEntity taxiEntityFromTaxi(Taxi taxi) {
        return new TaxiEntity(taxi.getId(), taxi.getName(), taxi.getIsAvailable());
    }

    static Taxi taxiEntityToTaxi(TaxiEntity taxi) {
        return new Taxi(taxi.getId(), taxi.getName(), taxi.getIsAvailable());
    }

}
