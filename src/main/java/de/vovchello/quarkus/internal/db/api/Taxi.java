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

    @Override
    public String toString() {
        return "Taxi [id=" + id + ", name=" + name + ", isAvailable=" + isAvailable + "]";
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