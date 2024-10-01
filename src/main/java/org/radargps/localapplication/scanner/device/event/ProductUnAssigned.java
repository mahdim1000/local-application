package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    private final String data;

    public ProductUnAssigned(String macAddress, String productId) {
        super("unassign-product");
        this.data = productId;
    }

    public String getData() {
        return data;
    }
}
