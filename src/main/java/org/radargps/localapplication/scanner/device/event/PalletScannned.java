package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletScannned extends DomainEvent {
    private String macAddress;
    private String palletId;

    public PalletScannned(String macAddress, String palletId) {
        this.macAddress = macAddress;
        this.palletId = palletId;
    }
}
