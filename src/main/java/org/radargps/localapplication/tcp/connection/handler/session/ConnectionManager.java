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
package org.radargps.localapplication.tcp.connection.handler.session;

import io.netty.channel.Channel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.tcp.connection.handler.Protocol;
import org.radargps.localapplication.common.util.config.Config;
import org.radargps.localapplication.common.util.config.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private final long deviceTimeout;
    private final Map<String, DeviceSession> sessionByUniqueId = new ConcurrentHashMap<>();
    private final Timer timer;
    private final ScannerInternalService scannerInternalService;

    private final Map<Long, Timeout> timeouts = new ConcurrentHashMap<>();

    public ConnectionManager(Timer timer, ScannerInternalService scannerInternalService) {
        this.timer = timer;
        this.scannerInternalService = scannerInternalService;
        deviceTimeout = Config.getConfig().getLong(Keys.STATUS_TIMEOUT);
    }

    public DeviceSession getDeviceSessionByUniqueId(String uniqueId) {
        return sessionByUniqueId.get(uniqueId);
    }


    public DeviceSession getDeviceSession(
            Protocol protocol, Channel channel, SocketAddress remoteAddress,
            String uniqueId) {

        if (sessionByUniqueId.containsKey(uniqueId)) {
            return sessionByUniqueId.get(uniqueId);
        }

        Optional<Scanner> device = scannerInternalService.findByUniqueId(uniqueId);

        if (device.isPresent()) {
            DeviceSession deviceSession = new DeviceSession(device.get().getUniqueId(),
                    device.get().getType(), device.get().getReadEntityType(), protocol, channel, remoteAddress);
            sessionByUniqueId.put(device.get().getUniqueId(), deviceSession);
            return deviceSession;
        } else {
            return null;
        }
    }
}
