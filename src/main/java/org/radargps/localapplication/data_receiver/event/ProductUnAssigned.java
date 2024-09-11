package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    String product;

    public ProductUnAssigned(String product) {
        this.product = product;
    }
}
