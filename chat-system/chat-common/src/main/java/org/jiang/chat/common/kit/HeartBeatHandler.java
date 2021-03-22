package org.jiang.chat.common.kit;

import io.netty.channel.ChannelHandlerContext;

public interface HeartBeatHandler {

    /**
     * 处理心跳
     */
    void process(ChannelHandlerContext ctx) throws Exception;
}
