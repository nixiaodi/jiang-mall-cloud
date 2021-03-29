package org.jiang.chat.client.service;

public interface MsgLogger {

    /**
     * 异步写入消息
     */
    void log(String message);

    /**
     * 停止写入
     */
    void stop();

    /**
     * 查询聊天记录
     * @param key 关键字
     * @return
     */
    String query(String key);

}
