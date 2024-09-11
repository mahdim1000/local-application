package org.radargps.localapplication.common.outbox;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.radargps.localapplication.util.ObjectUtil;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "outbox")
@Entity
public final class Outbox {
    @UuidGenerator
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OutboxType type;
    private String event;
    private ZonedDateTime createAt;

    public Outbox(DomainEvent event, OutboxType type) {
        this.type = type;
        this.event = ObjectUtil.convertObjectToJsonString(event);
        this.createAt = ZonedDateTime.now();
    }

    public Outbox() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OutboxType getType() {
        return type;
    }

    public void setType(OutboxType type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }
}
