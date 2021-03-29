package org.jiang.chat.client.service.impl;

import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
import org.jiang.chat.client.service.CustomerMsgHandleListener;
import org.jiang.chat.client.service.MsgLogger;
import org.jiang.chat.client.util.SpringBeanFactory;

/**
 * 自定义收到消息回调
 */
public class MsgCallbackListener implements CustomerMsgHandleListener {

    private MsgLogger msgLogger;

    public MsgCallbackListener() {
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class);
    }

    @Override
    public void handle(String message) {
        msgLogger.log(message);
    }
}
