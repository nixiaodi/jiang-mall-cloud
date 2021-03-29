package org.jiang.chat.client.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.client.CIMClient;
import org.jiang.chat.client.config.AppConfiguration;
import org.jiang.chat.client.service.*;
import org.jiang.chat.client.vo.request.GroupRequestVo;
import org.jiang.chat.client.vo.request.P2PRequestVo;
import org.jiang.chat.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MsgHandlerImpl implements MsgHandler {

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    @Qualifier("callBackThreadPool")
    private ThreadPoolExecutor executor;

    @Autowired
    private CIMClient cimClient;

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private InnerCommandContext innerCommandContext;

    private boolean aiModel = false;



    @Override
    public void sendMessage(String message) {
        if (aiModel) {
            aiChat(message);
        } else {
            normalChat(message);
        }
    }

    /**
     * normal chat
     * @param message
     */
    private void normalChat(String message) {
        String[] totalMessage = message.split(";;");
        if (totalMessage.length > 1) {
            // 私聊
            P2PRequestVo p2PRequestVo = new P2PRequestVo();
            p2PRequestVo.setUserId(appConfiguration.getUserId());
            p2PRequestVo.setReceiveUserId(Long.parseLong(totalMessage[0]));
            p2PRequestVo.setMsg(totalMessage[1]);
            try {
                p2pChat(p2PRequestVo);
            } catch (Exception e) {
                log.error("Exception", e);
            }
        } else {
            // 群聊
            GroupRequestVo groupRequestVo = new GroupRequestVo(appConfiguration.getUserId(), message);
            try {
                groupChat(groupRequestVo);
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
    }

    /**
     * AI model
     * @param message
     */
    private void aiChat(String message) {
        message = message.replace("吗", "");
        message = message.replace("嘛", "");
        message = message.replace("?", "!");
        message = message.replace("？", "!");
        message = message.replace("你", "我");
        System.out.println("AI:\033[31;4m" + message + "\033[0m");
    }

    @Override
    public void groupChat(GroupRequestVo groupRequestVo) throws Exception {
        routeRequest.sendGroupMessage(groupRequestVo);
    }

    @Override
    public void p2pChat(P2PRequestVo p2PRequestVo) throws Exception {
        routeRequest.sendP2PMessage(p2PRequestVo);
    }

    @Override
    public boolean checkMessage(String message) {
        if (StringUtil.isEmpty(message)) {
            log.warn("不能发送空消息！");
            return true;
        }
        return false;
    }

    @Override
    public boolean innerCommand(String message) {
        if (message.startsWith(":")) {
            InnerCommand instance = innerCommandContext.getInstance(message);
            instance.process(message);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭系统
     */
    @Override
    public void shutdown() {
        log.info("系统关闭中...");
        routeRequest.offline();
        msgLogger.stop();
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                log.info("线程池关闭中...");
            }
            cimClient.close();
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        System.exit(0);
    }

    @Override
    public void openAIModel() {
        aiModel = true;
    }

    @Override
    public void closeAIModel() {
        aiModel = false ;
    }
}
