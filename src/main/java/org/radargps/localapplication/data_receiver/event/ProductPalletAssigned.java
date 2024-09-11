package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private String pallet;
    private String product;
    public ProductPalletAssigned(String pallet, String product) {
        this.pallet = pallet;
        this.product = product;
    }

}
