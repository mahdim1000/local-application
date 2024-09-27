/*
 * Copyright 2015 - 2019 Anton Tananaev (anton@traccar.org)
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
package org.radargps.localapplication.tcp.connection.handler.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.radargps.localapplication.tcp.connection.handler.*;
import org.radargps.localapplication.tcp.connection.handler.session.ConnectionManager;
import org.radargps.localapplication.common.util.config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ScannerProtocol extends BaseProtocol {

    public ScannerProtocol(ApplicationContext context, ConnectionManager connectionManager) {
        addServer(new TrackerServer(context, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CharacterDelimiterFrameDecoder(8048, false, "\r\n", "\n", ";", "*"));
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(scannerProtocolDecoder(ScannerProtocol.this, connectionManager));
            }
        });
        addServer(new TrackerServer(context, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new ScannerProtocolDecoder(ScannerProtocol.this, connectionManager));
            }
        });
    }

    @Bean
    public ScannerProtocolDecoder scannerProtocolDecoder(Protocol protocol, ConnectionManager connectionManager) {
        return new ScannerProtocolDecoder(protocol, connectionManager);
    }
}
