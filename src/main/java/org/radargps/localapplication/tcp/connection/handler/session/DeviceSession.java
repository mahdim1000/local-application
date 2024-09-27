/*
 * Copyright 2016 - 2024 Anton Tananaev (anton@traccar.org)
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
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.radargps.localapplication.tcp.connection.handler.Protocol;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeviceSession {

    private final UUID deviceId;
    private final String uniqueId;
    private final ScannerType type;
    private final ScannerReadEntityType readEntityType;
    private final Protocol protocol;
    private final Channel channel;
    private final SocketAddress remoteAddress;

    public DeviceSession(UUID deviceId, String uniqueId, ScannerType type,
                         ScannerReadEntityType readEntityType, Protocol protocol,
                         Channel channel, SocketAddress remoteAddress) {
        this.deviceId = deviceId;
        this.uniqueId = uniqueId;
        this.type = type;
        this.readEntityType = readEntityType;
        this.protocol = protocol;
        this.channel = channel;
        this.remoteAddress = remoteAddress;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public ScannerType getType() {
        return type;
    }

    public ScannerReadEntityType getReadEntityType() {
        return readEntityType;
    }

    public Channel getChannel() {
        return channel;
    }

    public ConnectionKey getConnectionKey() {
        return new ConnectionKey(channel, remoteAddress);
    }

    public static final String KEY_TIMEZONE = "timezone";

    private final Map<String, Object> locals = new HashMap<>();

    public boolean contains(String key) {
        return locals.containsKey(key);
    }

    public void set(String key, Object value) {
        if (value != null) {
            locals.put(key, value);
        } else {
            locals.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) locals.get(key);
    }

}
