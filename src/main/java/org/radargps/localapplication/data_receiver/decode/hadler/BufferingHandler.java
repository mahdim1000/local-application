/*
 * Copyright 2015 - 2024 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.radargps.localapplication.data_receiver.decode.hadler;

import org.radargps.localapplication.common.outbox.DomainEvent;
import org.radargps.localapplication.data_receiver.DataCaptureDeviceService;
import org.radargps.localapplication.data_receiver.DataService;
import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.data_receiver.domain.DeviceRole;
import org.radargps.localapplication.data_receiver.event.PalletStagePassed;
import org.radargps.localapplication.data_receiver.event.ProductPalletAssigned;
import org.radargps.localapplication.data_receiver.event.ProductStagePassed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BufferingHandler extends BaseDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferingHandler.class);
    private final DataService dataService;
    private final DataCaptureDeviceService dataCaptureDeviceService;

    public BufferingHandler(DataService dataService, DataCaptureDeviceService dataCaptureDeviceService) {
        this.dataService = dataService;
        this.dataCaptureDeviceService = dataCaptureDeviceService;
    }


    @Override
    public void handleReceivedData(Data data, Callback callback) {
        var device = dataCaptureDeviceService.findOneExec(data.getDeviceId());
        if (device.isPresent()) {
            DomainEvent event;
            switch (device.get().getRole()) {
                case PRODUCT_STAGE -> event = new ProductStagePassed(device.get().getUniqueId(), data.getData());
                case PALLET_STAGE -> event = new PalletStagePassed(device.get().getUniqueId(), data.getData());
                case ASSIGN_PRODUCT_PALLET -> {
                    var connectedDevice = device.get().getConnectedDevice();
                    event = new ProductPalletAssigned()
                }
            }
        }
        callback.processed(false);
    }

}
