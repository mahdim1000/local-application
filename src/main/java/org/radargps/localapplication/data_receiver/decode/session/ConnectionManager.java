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
package org.radargps.localapplication.data_receiver.decode.session;

import io.netty.channel.Channel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import org.radargps.localapplication.data_receiver.DataCaptureDeviceService;
import org.radargps.localapplication.data_receiver.decode.Protocol;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.util.config.Config;
import org.radargps.localapplication.util.config.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private final long deviceTimeout;
    private final Map<UUID, DeviceSession> sessionsByDeviceId = new ConcurrentHashMap<>();
    private final Map<String, DeviceSession> sessionByUniqueId = new ConcurrentHashMap<>();
    private final Timer timer;
    private final DataCaptureDeviceService dataCaptureDeviceService;

    private final Map<Long, Timeout> timeouts = new ConcurrentHashMap<>();

    public ConnectionManager(Timer timer, DataCaptureDeviceService dataCaptureDeviceService) {
        this.timer = timer;
        this.dataCaptureDeviceService = dataCaptureDeviceService;
        deviceTimeout = Config.getConfig().getLong(Keys.STATUS_TIMEOUT);
    }

    public DeviceSession getDeviceSession(UUID deviceId) {
        return sessionsByDeviceId.get(deviceId);
    }

    public DeviceSession getDeviceSessionByUniqueId(String uniqueId) {
        return sessionByUniqueId.get(uniqueId);
    }


    public DeviceSession getDeviceSession(
            Protocol protocol, Channel channel, SocketAddress remoteAddress,
            String uniqueId) throws Exception {

        if (sessionByUniqueId.containsKey(uniqueId)) {
            return sessionByUniqueId.get(uniqueId);
        }

        Optional<DataCaptureDevice> device = dataCaptureDeviceService.findByUniqueId(uniqueId);

        if (device.isPresent()) {
            DeviceSession deviceSession = new DeviceSession(device.get().getId(), device.get().getUniqueId(),
                    device.get().getType(), device.get().getReadEntityType(), protocol, channel, remoteAddress);
            sessionByUniqueId.put(device.get().getUniqueId(), deviceSession);
            sessionsByDeviceId.put(device.get().getId(), deviceSession);
            return deviceSession;
        } else {
            return null;
        }
    }
}
