package de.vovchello.quarkus.internal.db.internal;

import java.util.Optional;

import de.vovchello.quarkus.internal.db.api.Order;

class TaxiEntity {

    private String id;
    private String name;
    private Boolean isAvailable;
    private Optional<Order> order;

    public TaxiEntity(String id, String name, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
        this.order = Optional.empty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Optional<Order> getOrder() {
        return order;
    }

    public void setOrder(Optional<Order> order) {
        this.order = order;
    }

}
