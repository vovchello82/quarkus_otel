package de.vovchello.quarkus.internal.order.internal;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "app.redis.orders")
public class RedisConfiguration {
    @ConfigProperty(name = "income.queue")
    private String incomingQueue;
    @ConfigProperty(name = "result.channel")
    private String resultChannel;

    public String getIncomingQueue() {
        return incomingQueue;
    }

    public void setIncomingQueue(String incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    public String getResultChannel() {
        return resultChannel;
    }

    public void setResultChannel(String resultChannel) {
        this.resultChannel = resultChannel;
    }

}
