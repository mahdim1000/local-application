package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    private String dataCaptureDevice;
    String product;

    public ProductUnAssigned(String dataCaptureDevice, String product) {
        this.dataCaptureDevice = dataCaptureDevice;
        this.product = product;
    }
}
