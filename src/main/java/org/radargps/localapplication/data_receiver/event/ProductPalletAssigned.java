package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private String dataCaptureDevice;
    private String pallet;
    private String product;

    public ProductPalletAssigned(String dataCaptureDevice, String pallet, String product) {
        this.dataCaptureDevice = dataCaptureDevice;
        this.pallet = pallet;
        this.product = product;
    }
}
