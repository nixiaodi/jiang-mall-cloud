package org.jiang.chat.client.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jiang.chat.client.config.AppConfiguration;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.RouteRequest;
import org.jiang.chat.client.thread.ContextHolder;
import org.jiang.chat.client.vo.request.GroupRequestVo;
import org.jiang.chat.client.vo.request.LoginRequestVo;
import org.jiang.chat.client.vo.request.P2PRequestVo;
import org.jiang.chat.client.vo.response.CIMServerResponseVo;
import org.jiang.chat.client.vo.response.OnlineUserResponseVo;
import org.jiang.chat.common.core.proxy.ProxyManager;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.response.BaseResponse;
import org.jiang.chat.route.api.RouteApi;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.util.List;

@Service
@Slf4j
public class RouteRequestImpl implements RouteRequest {

    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${cim.route.url}")
    private String routeUrl;

    @Autowired
    private EchoService echoService;

    @Autowired
    private AppConfiguration appConfiguration;

    @Override
    public void sendGroupMessage(GroupRequestVo groupRequestVo) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        ChatRequestVo chatRequestVo = new ChatRequestVo(groupRequestVo.getUserId(),groupRequestVo.getMsg());
        Response response = null;
        try {
            response = (Response) routeApi.groupRoute(chatRequestVo);
        } catch (Exception e) {
            log.error("exception",e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public void sendP2PMessage(P2PRequestVo p2PRequestVo) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        org.jiang.chat.route.api.vo.request.P2PRequestVo vo = new org.jiang.chat.route.api.vo.request.P2PRequestVo();
        vo.setMessage(p2PRequestVo.getMsg());
        vo.setReceiveUserId(p2PRequestVo.getReceiveUserId());
        vo.setUserId(p2PRequestVo.getUserId());
        Response response = null;
        try {
            response = ((Response) routeApi.p2pRoute(vo));
            String json = response.body().string();
            BaseResponse baseResponse = JSON.parseObject(json, BaseResponse.class);
            // account offline
            if (baseResponse.getCode().equals(StatusEnum.OFF_LINE.getCode())) {
                log.error(p2PRequestVo.getReceiveUserId() + ":" + StatusEnum.OFF_LINE.getMessage());
            }
        } catch (Exception e) {
            log.error("exception",e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public CIMServerResponseVo.ServerInfo getCIMServer(LoginRequestVo loginRequestVo) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        org.jiang.chat.route.api.vo.request.LoginRequestVo vo = new org.jiang.chat.route.api.vo.request.LoginRequestVo();
        vo.setUserId(loginRequestVo.getUserId());
        vo.setUserName(loginRequestVo.getUserName());

        Response response = null;
        CIMServerResponseVo cimServerResponseVo = null;

        try {
            response = ((Response) routeApi.login(vo));
            String json = response.body().string();
            cimServerResponseVo = JSON.parseObject(json, CIMServerResponseVo.class);

            // 重复失败
            if (!cimServerResponseVo.getCode().equals(StatusEnum.SUCCESS.getCode())) {
                echoService.echo(cimServerResponseVo.getMessage());

                // when client in reConnect state,could not exit
                if (ContextHolder.getReconnect()) {
                    echoService.echo("###{}###", StatusEnum.RECONNECT_FAIL.getMessage());
                    throw new ChatException(StatusEnum.RECONNECT_FAIL);
                }

                System.exit(-1);
            }
        } catch (Exception e) {
            log.error("exception",e);
        } finally {
            response.body().close();
        }
        return cimServerResponseVo.getDataBody();
    }

    @Override
    public List<OnlineUserResponseVo.DataBodyBean> onlineUsers() throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();

        Response response = null;
        OnlineUserResponseVo onlineUserResponseVo = null;

        try {
            response = ((Response) routeApi.onlineUser());
            String json = response.body().string();
            onlineUserResponseVo = JSON.parseObject(json,OnlineUserResponseVo.class);
        } catch (Exception e) {
            log.error("exception",e);
        } finally {
            response.body().close();
        }

        return onlineUserResponseVo.getDataBody();
    }

    @Override
    public void offline() {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, routeUrl, okHttpClient).getInstance();
        ChatRequestVo vo = new ChatRequestVo(appConfiguration.getUserId(), appConfiguration.getUserName());

        Response response = null;
        try {
            response = ((Response) routeApi.offLine(vo));
        } catch (Exception e) {
            log.error("exception",e);
        } finally {
            response.body().close();
        }
    }
}
