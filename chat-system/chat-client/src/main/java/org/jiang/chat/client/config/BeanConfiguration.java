package org.jiang.chat.client.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jiang.chat.client.handler.MsgHandleCaller;
import org.jiang.chat.client.service.impl.MsgCallbackListener;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.data.construct.RingBufferWheel;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
@Slf4j
public class BeanConfiguration {

    @Value("${cim.user.id}")
    private long userId;

    @Value("${cim.callback.thread.queue.size}")
    private int queueSize;

    @Value("${cim.callback.thread.pool.size}")
    private int poolSize;

    /**
     * 创建心跳单例
     * @return
     */
    @Bean(value = "heartBeat")
    public CIMRequestProto.CIMReqProtocol heartBeat() {
        CIMRequestProto.CIMReqProtocol heart = CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(userId)
                .setReqMsg("ping")
                .setType(Constants.CommandType.PING)
                .build();

        return heart;
    }

    /**
     * http client
     * @return
     */
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return builder.build();
    }

    /**
     * 创建回调线程池
     * @return
     */
    @Bean("callBackThreadPool")
    public ThreadPoolExecutor buildCallerThread() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ThreadFactory producter = new ThreadFactoryBuilder()
                .setNameFormat("msg-callback-%d")
                .setDaemon(true)
                .build();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize, 1, TimeUnit.SECONDS, queue, producter);
        return executor;
    }

    /**
     * 创建定时任务线程池
     * @return
     */
    @Bean("scheduledTask")
    public ScheduledExecutorService buildSchedule() {
        ThreadFactory schedule = new ThreadFactoryBuilder()
                .setNameFormat("reConnect-job-%d")
                .setDaemon(true)
                .build();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, schedule);
        return executor;
    }

    /**
     * 消息回调
     * @return
     */
    @Bean
    public MsgHandleCaller buildCaller() {
        MsgHandleCaller msgHandleCaller = new MsgHandleCaller(new MsgCallbackListener());
        return msgHandleCaller;
    }

    @Bean
    public RingBufferWheel bufferWheel() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        return new RingBufferWheel(executorService);
    }


}
