package org.jiang.chat.client.handler;

import org.jiang.chat.client.service.CustomerMsgHandleListener;

/**
 * 消息回调
 */
public class MsgHandleCaller {
    /**
     * 回调接口
     */
    private CustomerMsgHandleListener msgHandleListener;

    public MsgHandleCaller(CustomerMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }

    public CustomerMsgHandleListener getMsgHandleListener() {
        return msgHandleListener;
    }

    public void setMsgHandleListener(CustomerMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }
}
