package de.vovchello.quarkus.internal.order.api;

public class OrderResult {

    public String taxiId;
    public String orderId;

    public OrderResult(String orderId, String taxiId) {
        this.orderId = orderId;
        this.taxiId = taxiId;
    }

}
