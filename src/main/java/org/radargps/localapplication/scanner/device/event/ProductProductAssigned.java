package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

import java.util.UUID;

public class ProductProductAssigned extends DomainEvent {
    private String productLink;
    private UUID productId;

    public ProductProductAssigned(String productLink, UUID productId) {
        this.productLink = productLink;
        this.productId = productId;
    }
}
