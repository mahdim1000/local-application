/*
 * Copyright 2012 - 2021 Anton Tananaev (anton@traccar.org)
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
package org.radargps.localapplication.data_receiver.decode.protocol;

import io.netty.channel.Channel;
import org.radargps.localapplication.data_receiver.decode.BaseProtocolDecoder;
import org.radargps.localapplication.data_receiver.decode.Protocol;
import org.radargps.localapplication.data_receiver.decode.session.ConnectionManager;
import org.radargps.localapplication.data_receiver.decode.session.DeviceSession;
import org.radargps.localapplication.data_receiver.domain.Data;
import org.springframework.stereotype.Component;


import java.net.SocketAddress;
import java.time.Instant;

public class ScannerProtocolDecoder extends BaseProtocolDecoder {

    public ScannerProtocolDecoder(Protocol protocol, ConnectionManager connectionManager) {
        super(protocol, connectionManager);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        if (sentence.contains("#")) {
            var parts = sentence.split("#");
            if (parts.length == 2) {
                String uniqueId = parts[0];
                String strData = parts[1];

                DeviceSession deviceSession = getDeviceSession(uniqueId);
                if (deviceSession != null) {
                    return new Data(deviceSession.getDeviceId(), uniqueId, Instant.now().getEpochSecond(), strData);
                }
            }
        }
        return null;
    }

}
