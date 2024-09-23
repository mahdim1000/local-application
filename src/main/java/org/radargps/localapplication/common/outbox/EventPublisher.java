package org.radargps.localapplication.common.outbox;

public interface EventPublisher {
    void publish(DomainEvent event);
}
