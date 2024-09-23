package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductProductAssigned extends DomainEvent {
    private String macAddress;
    private String productLink;
    private String productId;

    public ProductProductAssigned(String macAddress, String productLink, String productId) {
        this.macAddress = macAddress;
        this.productLink = productLink;
        this.productId = productId;
    }
}
