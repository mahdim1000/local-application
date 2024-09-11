package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductStagePassed extends DomainEvent {
    String dataCaptureDevice;
    String product;

    public ProductStagePassed(String dataCaptureDevice, String product) {
        this.dataCaptureDevice = dataCaptureDevice;
        this.product = product;
    }
}
