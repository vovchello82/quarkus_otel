package de.vovchello.quarkus.internal.db.api;

import java.util.Optional;

public class Taxi {
    private String id;
    private String name;
    private Boolean isAvailable;
    private Optional<Order> order;

    public Taxi(String id, String name, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
        this.order = Optional.empty();
    }

    @Override
    public String toString() {
        return "Taxi [id=" + id + ", name=" + name + ", isAvailable=" + isAvailable + ", order=" + order + "]";
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((isAvailable == null) ? 0 : isAvailable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Taxi other = (Taxi) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (isAvailable == null) {
            if (other.isAvailable != null)
                return false;
        } else if (!isAvailable.equals(other.isAvailable))
            return false;
        return true;
    }

}