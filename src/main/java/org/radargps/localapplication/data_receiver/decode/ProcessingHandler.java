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
package org.radargps.localapplication.data_receiver.decode;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.radargps.localapplication.data_receiver.decode.hadler.BaseDataHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.DatabaseHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.PostProcessHandler;
import org.radargps.localapplication.data_receiver.domain.Data;
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
    private final Map<Long, Queue<Data>> queues = new HashMap<>();

    private synchronized Queue<Data> getQueue(long deviceId) {
        return queues.computeIfAbsent(deviceId, k -> new LinkedList<>());
    }

    public ProcessingHandler(ApplicationContext context) {

        positionHandlers = Stream.of(
                        DatabaseHandler.class
                )
                .map((clazz) -> (BaseDataHandler) context.getBean(clazz))
                .filter(Objects::nonNull)
                .toList();
        postProcessHandler = context.getBean(PostProcessHandler.class);
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
                    }
                }
//                finishedProcessing(ctx, data, filtered);
            }
        });
    }

//    private void finishedProcessing(ChannelHandlerContext ctx, Data data, boolean filtered) {
//        var device = dataCaptureDeviceService.findByUniqueId(data.getUniqueId());
//        if (device.isPresent()) {
//            switch (device.get().getRole()) {
//                case PALLET_STAGE -> palletStageMessagePublisher.publish();
//            }
//
//
//            // if scanner == product.stage
//
//            // if scanner == assign.product.pallet
//
//            // if scanner == unassign.product
//
//            // if scanner == unassign.pallet
//
//            // if scanner == produck.link
//        }
//    }
}
