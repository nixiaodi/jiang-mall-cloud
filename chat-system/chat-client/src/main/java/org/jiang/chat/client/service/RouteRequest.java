package org.jiang.chat.client.service;

import org.jiang.chat.client.vo.request.GroupRequestVo;
import org.jiang.chat.client.vo.request.LoginRequestVo;
import org.jiang.chat.client.vo.request.P2PRequestVo;
import org.jiang.chat.client.vo.response.CIMServerResponseVo;
import org.jiang.chat.client.vo.response.OnlineUserResponseVo;

import java.util.List;

public interface RouteRequest {

    /**
     * 群发消息
     * @param groupRequestVo 消息
     * @throws Exception
     */
    void sendGroupMessage(GroupRequestVo groupRequestVo) throws Exception;

    /**
     * 私聊
     * @param p2PRequestVo
     * @throws Exception
     */
    void sendP2PMessage(P2PRequestVo p2PRequestVo) throws Exception;

    /**
     * 获取登录用户服务器
     * @param loginRequestVo
     * @return
     * @throws Exception
     */
    CIMServerResponseVo.ServerInfo getCIMServer(LoginRequestVo loginRequestVo) throws Exception;

    /**
     * 获取所有在线用户
     * @return
     * @throws Exception
     */
    List<OnlineUserResponseVo.DataBodyBean> onlineUsers() throws Exception;

    /**
     * 下线
     */
    void offline();
}
