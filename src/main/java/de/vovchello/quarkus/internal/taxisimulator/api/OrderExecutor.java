package de.vovchello.quarkus.internal.taxisimulator.api;

public interface OrderExecutor extends Runnable {
    void executeOrder();
}
