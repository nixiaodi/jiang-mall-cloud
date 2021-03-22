package org.jiang.chat.server.kit;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jiang.chat.common.core.proxy.ProxyManager;
import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.route.api.RouteApi;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.jiang.chat.server.config.AppConfiguration;
import org.jiang.chat.server.util.SessionSocketHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RouteHandler {
    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private AppConfiguration appConfiguration;

    /**
     * 用户下线
     * @param userInfo
     * @param channel
     */
    public void userOffline(CIMUserInfo userInfo, NioSocketChannel channel) {
        if (userInfo != null) {
            log.info("Account [{}] offline", userInfo.getUserName());
            SessionSocketHolder.removeSession(userInfo.getUserId());
            // 清除路由关系
            clearRouteInfo(userInfo);
        }
        SessionSocketHolder.remove(channel);
    }

    /**
     * 清除路由关系
     * @param userInfo
     */
    public void clearRouteInfo(CIMUserInfo userInfo) {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, appConfiguration.getRouteUrl(), okHttpClient).getInstance();
        Response response = null;
        ChatRequestVo vo = new ChatRequestVo(userInfo.getUserId(), userInfo.getUserName());
        try {
            response = ((Response) routeApi.offLine(vo));
        } catch (Exception e) {
            log.info("Exception",e);
        } finally {
            response.body().close();
        }
    }
}
