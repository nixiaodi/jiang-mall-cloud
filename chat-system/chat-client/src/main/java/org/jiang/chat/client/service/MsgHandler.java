package org.jiang.chat.client.service;

import org.jiang.chat.client.vo.request.GroupRequestVo;
import org.jiang.chat.client.vo.request.P2PRequestVo;

/**
 * 消息处理器
 */
public interface MsgHandler {
    /**
     * 统一的发送接口，包含群聊和私聊
     */
    void sendMessage(String message);

    /**
     * 群聊
     * @param groupRequestVo 群聊消息 其中的 userId 为发送者的 userID
     * @throws Exception
     */
    void groupChat(GroupRequestVo groupRequestVo) throws Exception;

    /**
     * 私聊
     * @param p2PRequestVo 私聊请求
     * @throws Exception
     */
    void p2pChat(P2PRequestVo p2PRequestVo) throws Exception;

    // TODO: 2018/12/26 后续对消息的处理可以优化为责任链模式
    /**
     * 校验消息
     * @param message 不能为空，后续可以加上一些敏感词
     * @return
     */
    boolean checkMessage(String message);

    /**
     * 执行内部命令
     * @param message
     * @return 是否应当跳过当前消息(包含了":"就需要跳过)
     */
    boolean innerCommand(String message);

    /**
     * 关闭系统
     */
    void shutdown();

    /**
     * 开启AI模式
     */
    void openAIModel();

    /**
     * 关闭AI模式
     */
    void closeAIModel();
}
