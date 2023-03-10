package de.vovchello.quarkus.internal.db.api;

public class Taxi {
    public String id;
    public String name;
    public Boolean isAvailable;

    public Taxi(String id, String name, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
    }

}