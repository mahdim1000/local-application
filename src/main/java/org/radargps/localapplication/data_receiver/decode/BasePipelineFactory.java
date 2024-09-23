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

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.network.NetworkMessageHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.network.OpenChannelHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.network.RemoteAddressHandler;
import org.radargps.localapplication.data_receiver.decode.hadler.network.StandardLoggingHandler;
import org.radargps.localapplication.util.config.Config;
import org.radargps.localapplication.util.config.Keys;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public abstract class BasePipelineFactory extends ChannelInitializer<Channel> {
    private final ApplicationContext context;
    private final TrackerConnector connector;
    private final Config config;
    private final String protocol;
    private final int timeout;

    public BasePipelineFactory(ApplicationContext context, TrackerConnector connector, String protocol) {
        this.context = context;
        this.connector = connector;
        this.config = Config.getConfig();
        this.protocol = protocol;
        int timeout = config.getInteger(Keys.PROTOCOL_TIMEOUT.withPrefix(protocol));
        if (timeout == 0) {
            this.timeout = config.getInteger(Keys.SERVER_TIMEOUT);
        } else {
            this.timeout = timeout;
        }
    }

    protected abstract void addTransportHandlers(PipelineBuilder pipeline);

    protected abstract void addProtocolHandlers(PipelineBuilder pipeline);

    @SuppressWarnings("unchecked")
    public static <T extends ChannelHandler> T getHandler(ChannelPipeline pipeline, Class<T> clazz) {
        for (Map.Entry<String, ChannelHandler> handlerEntry : pipeline) {
            ChannelHandler handler = handlerEntry.getValue();
            if (handler instanceof WrapperInboundHandler wrapperHandler) {
                handler = wrapperHandler.getWrappedHandler();
            } else if (handler instanceof WrapperOutboundHandler wrapperHandler) {
                handler = wrapperHandler.getWrappedHandler();
            }
            if (clazz.isAssignableFrom(handler.getClass())) {
                return (T) handler;
            }
        }
        return null;
    }

    @Override
    protected void initChannel(Channel channel) {
        final ChannelPipeline pipeline = channel.pipeline();

        addTransportHandlers(pipeline::addLast);

        if (timeout > 0 && !connector.isDatagram()) {
            pipeline.addLast(new IdleStateHandler(timeout, 0, 0));
        }
        pipeline.addLast(new OpenChannelHandler(connector));
        pipeline.addLast(new NetworkMessageHandler());
        pipeline.addLast(new StandardLoggingHandler(protocol));

        addProtocolHandlers(handler -> {
            if (handler instanceof BaseProtocolDecoder) {
                // nothing
            } else {
                if (handler instanceof ChannelInboundHandler channelHandler) {
                    handler = new WrapperInboundHandler(channelHandler);
                } else if (handler instanceof ChannelOutboundHandler channelHandler) {
                    handler = new WrapperOutboundHandler(channelHandler);
                }
            }
            pipeline.addLast(handler);
        });

        pipeline.addLast(context.getBean(RemoteAddressHandler.class));
        pipeline.addLast(context.getBean(ProcessingHandler.class));
    }

}
