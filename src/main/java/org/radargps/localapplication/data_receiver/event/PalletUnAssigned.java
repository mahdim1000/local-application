package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletUnAssigned extends DomainEvent {
    private String pallet;

    public PalletUnAssigned(String pallet) {
        this.pallet = pallet;
    }
}
