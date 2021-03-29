package org.jiang.chat.client.thread;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.impl.ClientHeartBeatHandlerImpl;
import org.jiang.chat.client.util.SpringBeanFactory;
import org.jiang.chat.common.kit.HeartBeatHandler;

@Slf4j
public class ReConnectJob implements Runnable {
    private ChannelHandlerContext ctx;

    private HeartBeatHandler heartBeatHandler;

    public ReConnectJob(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.heartBeatHandler = SpringBeanFactory.getBean(ClientHeartBeatHandlerImpl.class);
    }

    @Override
    public void run() {
        try {
            heartBeatHandler.process(ctx);
        } catch (Exception e) {
            log.error("Exception",e);
        }
    }
}
