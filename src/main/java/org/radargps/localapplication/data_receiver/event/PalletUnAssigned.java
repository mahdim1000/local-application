package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletUnAssigned extends DomainEvent {
    private String dataCaptureDevice;
    private String pallet;

    public PalletUnAssigned(String dataCaptureDevice, String pallet) {
        this.dataCaptureDevice = dataCaptureDevice;
        this.pallet = pallet;
    }
}
