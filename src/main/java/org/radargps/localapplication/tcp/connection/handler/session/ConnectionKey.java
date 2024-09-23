package org.radargps.localapplication.tcp.connection.handler.session;

import io.netty.channel.Channel;

import java.net.SocketAddress;

public record ConnectionKey(SocketAddress localAddress, SocketAddress remoteAddress) {
    public ConnectionKey(Channel channel, SocketAddress remoteAddress) {
        this(channel.localAddress(), remoteAddress);
    }
}
