package org.jiang.chat.client.service;

import org.springframework.stereotype.Component;

@Component
public class ShutdownMsg {
    private boolean isCommand;

    /**
     * 置为用户主动退出状态
     */
    public void shutdown() {
        isCommand = true;
    }

    /**
     * 检查退出状态
     */
    public boolean checkStatus() {
        return isCommand;
    }
}
