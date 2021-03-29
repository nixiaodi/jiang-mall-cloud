package org.jiang.chat.client.service;

/**
 * 自定义消息回调
 */
public interface CustomerMsgHandleListener {

    /**
     * 消息回调
     */
    void handle(String message);
}
