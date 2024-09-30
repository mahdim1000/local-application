package org.radargps.localapplication.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Payload;

import java.util.Map;

public abstract class DomainEvent {
    protected String pattern = "product-scanner";

    public DomainEvent(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
