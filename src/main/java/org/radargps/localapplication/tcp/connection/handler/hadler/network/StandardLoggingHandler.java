/*
 * Copyright 2019 - 2024 Anton Tananaev (anton@traccar.org)
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
package org.radargps.localapplication.tcp.connection.handler.hadler.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.radargps.localapplication.tcp.connection.handler.NetworkMessage;
import org.radargps.localapplication.tcp.connection.handler.session.ConnectionKey;
import org.radargps.localapplication.common.util.BufferUtil;
import org.radargps.localapplication.common.util.NetworkUtil;
import org.radargps.localapplication.common.util.config.Config;
import org.radargps.localapplication.common.util.config.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class StandardLoggingHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardLoggingHandler.class);

    private final String protocol;
    private boolean decodeTextData;

    public StandardLoggingHandler(String protocol) {
        this.protocol = protocol;
        this.decodeTextData = Config.getConfig().getBoolean(Keys.LOGGER_TEXT_PROTOCOL);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LogRecord record = createLogRecord(ctx, msg);
        log(ctx, false, record);
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log(ctx, true, createLogRecord(ctx, msg));
        super.write(ctx, msg, promise);
    }

    private LogRecord createLogRecord(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof NetworkMessage networkMessage) {
            if (networkMessage.getMessage() instanceof ByteBuf data) {
                LogRecord record = new LogRecord(ctx.channel().localAddress(), networkMessage.getRemoteAddress());
                record.setProtocol(protocol);
                if (decodeTextData && BufferUtil.isPrintable(data, data.readableBytes())) {
                    record.setData(data.getCharSequence(
                                    data.readerIndex(), data.readableBytes(), StandardCharsets.US_ASCII).toString()
                            .replace("\r", "\\r").replace("\n", "\\n"));
                } else {
                    record.setData(ByteBufUtil.hexDump(data));
                }
                return record;
            }
        }
        return null;
    }

    private void log(ChannelHandlerContext ctx, boolean downstream, LogRecord record) {
        if (record != null) {
            StringBuilder message = new StringBuilder();
            message.append("[").append(NetworkUtil.session(ctx.channel())).append(": ");
            message.append(protocol);
            message.append(downstream ? " > " : " < ");
            message.append(record.getAddress().getHostString());
            message.append("] ");
            message.append(record.getData());
            LOGGER.info(message.toString());
        }
    }

    class LogRecord {

        private final InetSocketAddress localAddress;
        private final InetSocketAddress remoteAddress;

        public LogRecord(SocketAddress localAddress, SocketAddress remoteAddress) {
            this.localAddress = (InetSocketAddress) localAddress;
            this.remoteAddress = (InetSocketAddress) remoteAddress;
        }

        @JsonIgnore
        public InetSocketAddress getAddress() {
            return remoteAddress;
        }

        @JsonIgnore
        public ConnectionKey getConnectionKey() {
            return new ConnectionKey(localAddress, remoteAddress);
        }

        public String getHost() {
            return remoteAddress.getHostString();
        }

        private String protocol;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        private String uniqueId;

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        private long deviceId;

        public long getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(long deviceId) {
            this.deviceId = deviceId;
        }

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }

}
