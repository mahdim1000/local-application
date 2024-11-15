/*
 * Copyright 2015 - 2023 Anton Tananaev (anton@traccar.org)
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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.radargps.localapplication.util.config.Config;

import java.net.SocketAddress;
import java.util.Collection;

public abstract class ExtendedObjectDecoder extends ChannelInboundHandlerAdapter {

    private Config config = Config.getConfig();

    public Config getConfig() {
        return config;
    }

    /**
     * Method called when config is initialized.
     */
    protected void init() {
    }

    private void saveOriginal(Object decodedMessage, Object originalMessage) {
//        if (getConfig().getBoolean(Keys.DATABASE_SAVE_ORIGINAL) && decodedMessage instanceof Position position) {
//            if (originalMessage instanceof ByteBuf buf) {
//                position.set(Position.KEY_ORIGINAL, ByteBufUtil.hexDump(buf, 0, buf.writerIndex()));
//            } else if (originalMessage instanceof String stringMessage) {
//                position.set(Position.KEY_ORIGINAL, DataConverter.printHex(
//                        stringMessage.getBytes(StandardCharsets.US_ASCII)));
//            }
//        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NetworkMessage networkMessage = (NetworkMessage) msg;
        Object originalMessage = networkMessage.getMessage();
        try {
            Object decodedMessage = decode(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage);
            onMessageEvent(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage, decodedMessage);
            if (decodedMessage == null) {
                decodedMessage = handleEmptyMessage(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage);
            }
            if (decodedMessage != null) {
                if (decodedMessage instanceof Collection collection) {
                    for (Object o : collection) {
                        saveOriginal(o, originalMessage);
                        ctx.fireChannelRead(o);
                    }
                } else {
                    saveOriginal(decodedMessage, originalMessage);
                    ctx.fireChannelRead(decodedMessage);
                }
            }
        } finally {
            ReferenceCountUtil.release(originalMessage);
        }
    }

    protected void onMessageEvent(
            Channel channel, SocketAddress remoteAddress, Object originalMessage, Object decodedMessage) {
    }

    protected Object handleEmptyMessage(Channel channel, SocketAddress remoteAddress, Object msg) {
        return null;
    }

    protected abstract Object decode(Channel channel, SocketAddress remoteAddress, Object msg) throws Exception;

}
