package org.radargps.localapplication.common.outbox;

public abstract class DomainEvent {
    protected String type;

    public DomainEvent() {
        this.type = this.getClass().getSimpleName();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
