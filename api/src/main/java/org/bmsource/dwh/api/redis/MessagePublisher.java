package org.bmsource.dwh.api.redis;

public interface MessagePublisher {
    void publish(final String message);
}
