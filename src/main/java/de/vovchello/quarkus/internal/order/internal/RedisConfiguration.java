package de.vovchello.quarkus.internal.order.internal;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app.redis.orders")
public interface RedisConfiguration {
    String incomingQueue();

    String resultChannel();

}
