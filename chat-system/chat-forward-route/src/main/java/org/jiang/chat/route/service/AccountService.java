package org.jiang.chat.route.service;

import com.sun.org.apache.bcel.internal.ExceptionConst;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.jiang.chat.route.api.vo.request.LoginRequestVo;
import org.jiang.chat.route.api.vo.request.RegisterInfoRequestVo;
import org.jiang.chat.route.api.vo.response.CIMServerResponseVo;
import org.jiang.chat.route.api.vo.response.RegisterInfoResponseVo;

import java.util.Map;

public interface AccountService {

    /**
     * 注册用户
     * @param info 用户信息
     * @return
     * @throws Exception
     */
    RegisterInfoResponseVo register(RegisterInfoResponseVo info) throws Exception;

    /**
     * 登录服务
     * @param loginRequestVo 登录信息
     * @return
     * @throws Exception
     */
    StatusEnum login(LoginRequestVo loginRequestVo) throws Exception;

    /**
     * 保存路由信息
     * @param loginRequestVo 用户信息
     * @param msg 服务器信息
     * @throws Exception
     */
    void saveRouteInfo(LoginRequestVo loginRequestVo,String msg) throws Exception;

    /**
     * 加载所有用户的路由关系
     * @return
     */
    Map<Long, CIMServerResponseVo> loadRouteRelated();

    /**
     * 获取某个用户的路由关系
     * @param userId 用户id
     * @return
     */
    CIMServerResponseVo loadRouteRelatedByUserId(Long userId);

    /**
     * 推送消息
     * @param cimServerResponseVo
     * @param sendUserId 发送者ID
     * @param groupRequestVo
     * @throws Exception
     */
    void pushMsg(CIMServerResponseVo cimServerResponseVo, Long sendUserId, ChatRequestVo groupRequestVo) throws Exception;

    /**
     * 用户下线
     * @param userId
     * @throws Exception
     */
    void offline(Long userId) throws Exception;
}
