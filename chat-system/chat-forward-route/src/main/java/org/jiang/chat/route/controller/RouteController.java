package org.jiang.chat.route.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.common.model.RouteInfo;
import org.jiang.chat.common.response.BaseResponse;
import org.jiang.chat.common.response.NullBody;
import org.jiang.chat.common.route.algorithm.RouteHandler;
import org.jiang.chat.common.utils.RouteInfoParseUtil;
import org.jiang.chat.route.api.RouteApi;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.jiang.chat.route.api.vo.request.LoginRequestVo;
import org.jiang.chat.route.api.vo.request.P2PRequestVo;
import org.jiang.chat.route.api.vo.request.RegisterInfoRequestVo;
import org.jiang.chat.route.api.vo.response.CIMServerResponseVo;
import org.jiang.chat.route.api.vo.response.RegisterInfoResponseVo;
import org.jiang.chat.route.cache.ServerCache;
import org.jiang.chat.route.service.AccountService;
import org.jiang.chat.route.service.CommonBizService;
import org.jiang.chat.route.service.UserInfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/")
@Slf4j
public class RouteController implements RouteApi {

    @Autowired
    private ServerCache serverCache;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private CommonBizService commonBizService;

    @Autowired
    private RouteHandler routeHandler;

    @ApiOperation("群聊 API")
    @PostMapping("/groupRoute")
    @Override
    public BaseResponse<NullBody> groupRoute(@RequestBody ChatRequestVo groupRequestVo) throws Exception {
        BaseResponse<NullBody> response = new BaseResponse<>();

        log.info("msg=[{}]",groupRequestVo.toString());

        // 获取所有的服务列表
        Map<Long, CIMServerResponseVo> cimServerResponseVoMap = accountService.loadRouteRelated();
        for (Map.Entry<Long, CIMServerResponseVo> cimServerResponseVoEntry : cimServerResponseVoMap.entrySet()) {
            Long userId = cimServerResponseVoEntry.getKey();
            CIMServerResponseVo cimServerResponseVo = cimServerResponseVoEntry.getValue();
            if (userId.equals(groupRequestVo.getUserId())) {
                // 过滤掉自我
                CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(userId);
                log.info("过滤掉了发送者 userId={}",cimUserInfo.toString());
                continue;
            }

            // 推送消息
            ChatRequestVo chatRequestVo = new ChatRequestVo(userId, groupRequestVo.getMessage());
            accountService.pushMsg(cimServerResponseVo,groupRequestVo.getUserId(),chatRequestVo);

        }

        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());
        return response;
    }

    @ApiOperation("私聊 API")
    @PostMapping("/p2pRoute")
    @Override
    public BaseResponse<NullBody> p2pRoute(@RequestBody P2PRequestVo p2PRequestVo) throws Exception {
        BaseResponse<NullBody> response = new BaseResponse<>();

        try {
            // 获取接收消息用户的路由信息
            CIMServerResponseVo cimServerResponseVo = accountService.loadRouteRelatedByUserId(p2PRequestVo.getUserId());

            // p2pRequest.getReceiveUserId() ==> 接收消息的userId
            ChatRequestVo chatRequestVo = new ChatRequestVo(p2PRequestVo.getReceiveUserId(), p2PRequestVo.getMessage());
            accountService.pushMsg(cimServerResponseVo,p2PRequestVo.getUserId(),chatRequestVo);

            response.setCode(StatusEnum.SUCCESS.getCode());
            response.setMessage(StatusEnum.SUCCESS.getMessage());
        } catch (ChatException e) {
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @ApiOperation("客户端下线")
    @PostMapping("/offline")
    @Override
    public BaseResponse<NullBody> offLine(@RequestBody ChatRequestVo groupRequestVo) throws Exception {
        BaseResponse<NullBody> response = new BaseResponse<>();

        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(groupRequestVo.getUserId());

        log.info("user [{}] offline!", cimUserInfo.toString());
        accountService.offline(groupRequestVo.getUserId());

        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());

        return response;
    }

    /**
     * 获取一台CIM server
     * @param loginRequestVo
     * @return
     * @throws Exception
     */
    @ApiOperation("登录并获取服务器")
    @PostMapping("/login")
    @Override
    public BaseResponse<CIMServerResponseVo> login(@RequestBody LoginRequestVo loginRequestVo) throws Exception {
        BaseResponse<CIMServerResponseVo> response = new BaseResponse<>();

        // check server available
        String server = routeHandler.routeServer(serverCache.getServerList(), String.valueOf(loginRequestVo.getUserId()));
        log.info("userName=[{}] route server info=[{}]", loginRequestVo.getUserName(), server);

        RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
        commonBizService.checkServerAvailable(routeInfo);

        // 登录效验
        StatusEnum status = accountService.login(loginRequestVo);
        if (status == StatusEnum.SUCCESS) {
            // 保存路由信息
            accountService.saveRouteInfo(loginRequestVo,server);

            CIMServerResponseVo cimServerResponseVo = new CIMServerResponseVo(routeInfo);
            response.setDataBody(cimServerResponseVo);
        }

        response.setCode(status.getCode());
        response.setMessage(status.getMessage());

        return response;
    }

    /**
     * 注册账号
     * @param registerInfoRequestVo
     * @return
     * @throws Exception
     */
    @ApiOperation("注册账号")
    @PostMapping("/registerAccount")
    @Override
    public BaseResponse<RegisterInfoResponseVo> registerAccount(@RequestBody RegisterInfoRequestVo registerInfoRequestVo) throws Exception {
        BaseResponse<RegisterInfoResponseVo> response = new BaseResponse<>();

        long userId = System.currentTimeMillis();
        RegisterInfoResponseVo info = new RegisterInfoResponseVo(userId, registerInfoRequestVo.getUserName());
        info = accountService.register(info);

        response.setDataBody(info);
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());

        return response;
    }

    /**
     * 获取所有在线用户
     * @return
     * @throws Exception
     */
    @ApiOperation("获取所有在线用户")
    @PostMapping("/onlineUser")
    @Override
    public BaseResponse<Set<CIMUserInfo>> onlineUser() throws Exception {
        BaseResponse<Set<CIMUserInfo>> response = new BaseResponse<>();

        Set<CIMUserInfo> cimUserInfos = userInfoCacheService.onlineUser();
        response.setDataBody(cimUserInfos);
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());

        return response;
    }
}
