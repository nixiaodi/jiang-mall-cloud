package org.jiang.chat.client.service.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.client.CIMClient;
import org.jiang.chat.client.thread.ContextHolder;
import org.jiang.chat.common.kit.HeartBeatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientHeartBeatHandlerImpl implements HeartBeatHandler {
    @Autowired
    private CIMClient cimClient;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        // 重连
        ContextHolder.setReconnect(true);
        cimClient.reconnect();
    }
}
