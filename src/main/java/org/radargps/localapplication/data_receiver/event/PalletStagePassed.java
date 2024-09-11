package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletStagePassed extends DomainEvent {
    private String dataCaptureDevice;
    private String pallet;

    public PalletStagePassed(String dataCaptureDevice, String pallet) {
        this.dataCaptureDevice = dataCaptureDevice;
        this.pallet = pallet;
    }
}
