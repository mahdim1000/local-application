/*
 * Copyright 2024 Anton Tananaev (anton@traccar.org)
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
package org.radargps.localapplication.tcp.connection.handler.hadler;

import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PostProcessHandler extends BaseDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessHandler.class);

    private final ScannerInternalService scannerInternalService;

    public PostProcessHandler(ScannerInternalService scannerInternalService) {
        this.scannerInternalService = scannerInternalService;
    }

    @Override
    public void handleReceivedData(Data data, Callback callback) {

//            if (PositionUtil.isLatest(cacheManager, position)) {
//                Device updatedDevice = new Device();
//                updatedDevice.setId(position.getDeviceId());
//                updatedDevice.setPositionId(position.getId());
//                storage.updateObject(updatedDevice, new Request(
//                        new Columns.Include("positionId"),
//                        new Condition.Equals("id", updatedDevice.getId())));
//
//                cacheManager.updatePosition(position);
//                connectionManager.updatePosition(true, position);
//            }

        scannerInternalService.updateLatestDeviceData(data.getDeviceId(), data);
        callback.processed(false);
    }

}
