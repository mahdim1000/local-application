/*
 * Copyright 2012 - 2024 Anton Tananaev (anton@traccar.org)
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
package org.radargps.localapplication.data_receiver.decode;

import io.netty.channel.Channel;
import org.radargps.localapplication.data_receiver.decode.session.ConnectionManager;
import org.radargps.localapplication.data_receiver.decode.session.DeviceSession;
import org.radargps.localapplication.util.config.Config;
import org.radargps.localapplication.util.config.Keys;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public abstract class BaseProtocolDecoder extends ExtendedObjectDecoder {

    private static final String PROTOCOL_UNKNOWN = "unknown";

    private final Protocol protocol;
    private String modelOverride;
    private final ConnectionManager connectionManager;

    public BaseProtocolDecoder(Protocol protocol, ConnectionManager connectionManager) {
        this.protocol = protocol;
        this.connectionManager = connectionManager;
    }

    public String getProtocolName() {
        return protocol != null ? protocol.getName() : PROTOCOL_UNKNOWN;
    }

    public DeviceSession getDeviceSession(String uniqueId) {
        return connectionManager.getDeviceSessionByUniqueId(uniqueId);
    }

    @Override
    protected void onMessageEvent(
            Channel channel, SocketAddress remoteAddress, Object originalMessage, Object decodedMessage) {
        Set<Long> deviceIds = new HashSet<>();
//        if (decodedMessage != null) {
//            if (decodedMessage instanceof Position position) {
//                deviceIds.add(position.getDeviceId());
//            } else if (decodedMessage instanceof Collection) {
//                Collection<Position> positions = (Collection) decodedMessage;
//                for (Position position : positions) {
//                    deviceIds.add(position.getDeviceId());
//                }
//            }
//        }
//        if (deviceIds.isEmpty()) {
//            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
//            if (deviceSession != null) {
//                deviceIds.add(deviceSession.getDeviceId());
//            }
//        }
//        for (long deviceId : deviceIds) {
//            connectionManager.updateDevice(deviceId, Device.STATUS_ONLINE, new Date());
//            sendQueuedCommands(channel, remoteAddress, deviceId);
//        }
    }


    @Override
    protected Object handleEmptyMessage(Channel channel, SocketAddress remoteAddress, Object msg) {
//        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
//        if (getConfig().getBoolean(Keys.DATABASE_SAVE_EMPTY) && deviceSession != null) {
//            Position position = new Position(getProtocolName());
//            position.setDeviceId(deviceSession.getDeviceId());
//            getLastLocation(position, null);
//            return position;
//        } else {
//            return null;
//        }
        return null;
    }

}
