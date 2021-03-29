package org.jiang.chat.client.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandlerContext;
import org.jiang.chat.client.thread.ReConnectJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public class ReconnectManager {

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * trigger reconnect job
     * @param ctx
     */
    public void reConnect(ChannelHandlerContext ctx) {
        buildExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ReConnectJob(ctx),0,10, TimeUnit.SECONDS);
    }

    /**
     * close reconnect job if reconnect success
     */
    public void reConnectSuccess() {
        scheduledExecutorService.shutdown();
    }

    /**
     * build a thead executor
     */
    private ScheduledExecutorService buildExecutor() {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            ThreadFactory schedule = new ThreadFactoryBuilder()
                    .setNameFormat("reConnect-job-%d")
                    .setDaemon(true)
                    .build();
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, schedule);
        }
        return scheduledExecutorService;
    }
}
