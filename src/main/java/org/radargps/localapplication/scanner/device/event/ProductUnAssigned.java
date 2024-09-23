package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    private String macAddress;
    String productId;

    public ProductUnAssigned(String macAddress, String productId) {
        this.macAddress = macAddress;
        this.productId = productId;
    }
}
