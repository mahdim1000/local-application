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
package org.radargps.localapplication.tcp.connection.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
import org.radargps.localapplication.tcp.connection.handler.hadler.BaseDataHandler;
import org.radargps.localapplication.tcp.connection.handler.hadler.BufferingHandler;
import org.radargps.localapplication.tcp.connection.handler.hadler.DatabaseHandler;
import org.radargps.localapplication.tcp.connection.handler.hadler.PostProcessHandler;
import org.radargps.localapplication.captured.data.domain.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingHandler.class);
    private ApplicationContext context;
    private final List<BaseDataHandler> positionHandlers;
    private final PostProcessHandler postProcessHandler;
    private final ScannerInternalService scannerInternalService;
    private final Map<Long, Queue<Data>> queues = new HashMap<>();

    private synchronized Queue<Data> getQueue(long deviceId) {
        return queues.computeIfAbsent(deviceId, k -> new LinkedList<>());
    }

    public ProcessingHandler(ApplicationContext context, ScannerInternalService scannerInternalService) {

        positionHandlers = Stream.of(
                        DatabaseHandler.class,
                        BufferingHandler.class
                )
                .map((clazz) -> (BaseDataHandler) context.getBean(clazz))
                .filter(Objects::nonNull)
                .toList();
        postProcessHandler = context.getBean(PostProcessHandler.class);
        this.scannerInternalService = scannerInternalService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Data data) {
            processPositionHandlers(ctx, data);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    private void processPositionHandlers(ChannelHandlerContext ctx, Data data) {
        var iterator = positionHandlers.iterator();
        iterator.next().handleReceivedData(data, new BaseDataHandler.Callback() {
            @Override
            public void processed(boolean filtered) {
                if (!filtered) {
                    if (iterator.hasNext()) {
                        iterator.next().handleReceivedData(data, this);
                    } else {
                        postProcessHandler.handleReceivedData(data, (f) -> {
                            System.out.println("postprocess: " + f);
                        });
                    }
                }
            }
        });
        finishedProcessing(ctx, data, true);
    }

    private void finishedProcessing(ChannelHandlerContext ctx, Data data, boolean filtered) {
//        scannerInternalService.updateLatestDeviceData(data.getUniqueId(), data);
        scannerInternalService.processAndPublish(data);
    }
}
