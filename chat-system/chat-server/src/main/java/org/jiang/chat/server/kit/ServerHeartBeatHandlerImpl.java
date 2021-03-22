package org.jiang.chat.server.kit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.kit.HeartBeatHandler;
import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.common.utils.NettyAttributeUtil;
import org.jiang.chat.server.config.AppConfiguration;
import org.jiang.chat.server.util.SessionSocketHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler {
    @Autowired
    private RouteHandler routeHandler;

    @Autowired
    private AppConfiguration appConfiguration;


    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        long heartBeatTime = appConfiguration.getHeartBeatTime() * 1000;

        Long lastReadTime = NettyAttributeUtil.getReaderTime(ctx.channel());
        long now = System.currentTimeMillis();
        if (lastReadTime != null && now - lastReadTime > heartBeatTime) {
            CIMUserInfo userInfo = SessionSocketHolder.getUserId(((NioSocketChannel) ctx.channel()));
            if (userInfo != null) {
                log.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!",userInfo.getUserName(),now - lastReadTime);
            }
            routeHandler.userOffline(userInfo, ((NioSocketChannel) ctx.channel()));
            ctx.channel().close();
        }
    }
}
