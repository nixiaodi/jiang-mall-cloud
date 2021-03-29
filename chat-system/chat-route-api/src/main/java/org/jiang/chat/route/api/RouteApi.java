package org.jiang.chat.route.api;

import org.jiang.chat.common.response.BaseResponse;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.jiang.chat.route.api.vo.request.LoginRequestVo;
import org.jiang.chat.route.api.vo.request.P2PRequestVo;
import org.jiang.chat.route.api.vo.request.RegisterInfoRequestVo;
import org.jiang.chat.route.api.vo.response.RegisterInfoResponseVo;

/**
 * @author 蒋小胖
 */
public interface RouteApi {

    /**
     * group chat
     * @param groupRequestVo
     * @return
     * @throws Exception
     */
    Object groupRoute(ChatRequestVo groupRequestVo) throws Exception;

    /**
     * point to point chat
     * @param p2PRequestVo
     * @return
     * @throws Exception
     */
    Object p2pRoute(P2PRequestVo p2PRequestVo) throws Exception;

    /**
     * offline account
     * @param groupRequestVo
     * @return
     * @throws Exception
     */
    Object offLine(ChatRequestVo groupRequestVo) throws Exception;

    /**
     * login account
     * @param loginRequestVo
     * @return
     * @throws Exception
     */
    Object login(LoginRequestVo loginRequestVo) throws Exception;

    /**
     * register account
     * @param registerInfoRequestVo
     * @return
     * @throws Exception
     */
    BaseResponse<RegisterInfoResponseVo> registerAccount(RegisterInfoRequestVo registerInfoRequestVo) throws Exception;

    /**
     * get all online users
     * @return
     * @throws Exception
     */
    Object onlineUser() throws Exception;
}
