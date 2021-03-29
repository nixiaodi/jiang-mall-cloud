package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.client.CIMClient;
import org.jiang.chat.client.service.*;
import org.jiang.chat.common.data.construct.RingBufferWheel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ShutdownCommand implements InnerCommand {

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private CIMClient cimClient;

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    @Qualifier("callBackThreadPool")
    private ThreadPoolExecutor callBackExecutor;

    @Autowired
    private EchoService echoService;

    @Autowired
    private ShutdownMsg shutdownMsg;

    @Autowired
    private RingBufferWheel ringBufferWheel;

    @Override
    public void process(String message) {
        echoService.echo("cim client closing...");
        shutdownMsg.shutdown();
        routeRequest.offline();
        msgLogger.stop();
        callBackExecutor.shutdown();
        ringBufferWheel.stop(false);
        try {
            while (!callBackExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                echoService.echo("thread pool closing");
            }
            cimClient.close();
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        echoService.echo("cim close success!");
        System.exit(0);
    }
}
