package org.radargps.localapplication.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

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
