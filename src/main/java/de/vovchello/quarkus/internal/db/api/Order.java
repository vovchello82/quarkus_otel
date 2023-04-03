package de.vovchello.quarkus.internal.db.api;

public class Order {
    public String id;
    public String from;
    public String to;
    public String customer;

    @Override
    public String toString() {
        return "Order [id=" + id + ", from=" + from + ", to=" + to + ", customer=" + customer + "]";
    }

}
