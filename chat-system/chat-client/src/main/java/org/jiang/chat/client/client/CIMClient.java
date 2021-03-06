package org.jiang.chat.client.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.config.AppConfiguration;
import org.jiang.chat.client.initializer.CIMClientHandlerInitializer;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.MsgHandler;
import org.jiang.chat.client.service.ReconnectManager;
import org.jiang.chat.client.service.RouteRequest;
import org.jiang.chat.client.service.impl.ClientInfo;
import org.jiang.chat.client.thread.ContextHolder;
import org.jiang.chat.client.vo.request.GoogleProtocolVo;
import org.jiang.chat.client.vo.request.LoginRequestVo;
import org.jiang.chat.client.vo.response.CIMServerResponseVo;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Slf4j
public class CIMClient {

    private EventLoopGroup group = new NioEventLoopGroup(0,new DefaultThreadFactory("cim-work"));

    @Value("${cim.user.id}")
    private long userId;

    @Value("${cim.user.userName}")
    private String userName;

    private SocketChannel socketChannel;

    @Autowired
    private EchoService echoService;

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private ReconnectManager reconnectManager;

    /**
     * ????????????
     */
    private int errorCount;

    @PostConstruct
    public void start() throws Exception {

        // ?????? + ?????????????????????????????? ip+port
        CIMServerResponseVo.ServerInfo cimServer = userLogin();

        // ???????????????
        startClient(cimServer);

        // ??????????????????
        loginCIMServer();
    }

    /**
     * ???????????????
     * @param cimServer
     */
    private void startClient(CIMServerResponseVo.ServerInfo cimServer) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new CIMClientHandlerInitializer());

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(cimServer.getIp(), cimServer.getCimServerPort()).sync();
        } catch (Exception e) {
            errorCount++;
            if (errorCount >= appConfiguration.getErrorCount()) {
                log.error("??????????????????????????????[{}]???", errorCount);
                msgHandler.shutdown();
            }
            log.error("Connect fail!", e);
        }
        if (channelFuture.isSuccess()) {
            echoService.echo("Start cim client success!");
            log.info("?????? cim client ??????");
        }
        socketChannel = ((SocketChannel) channelFuture.channel());
    }

    /**
     * ?????? + ???????????????
     * @return ?????????????????????
     */
    private CIMServerResponseVo.ServerInfo userLogin() {
        LoginRequestVo loginVo = new LoginRequestVo(userId, userName);
        CIMServerResponseVo.ServerInfo cimServer = null;
        try {
            cimServer = routeRequest.getCIMServer(loginVo);

            // ??????????????????
            clientInfo.saveServiceInfo(cimServer.getIp() + ":" + cimServer.getCimServerPort())
                    .saveUserInfo(userId,userName);

            log.info("cimServer=[{}]", cimServer.toString());
        } catch (Exception e) {
            errorCount++;

            if (errorCount >= appConfiguration.getErrorCount()) {
                echoService.echo("The maximum number of reConnections has been reached[{}]times, close cim client!", errorCount);
                msgHandler.shutdown();
            }
            log.error("login fail", e);
        }
        return cimServer;
    }

    /**
     * ????????????
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }

    /**
     * ??????????????????
     */
    private void loginCIMServer() {
        CIMRequestProto.CIMReqProtocol login = CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(userId)
                .setReqMsg(userName)
                .setType(Constants.CommandType.LOGIN)
                .build();

        ChannelFuture future = socketChannel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture -> {
            echoService.echo("Registry cim server success!");
        });
    }

    /**
     * ?????????????????????
     */
    public void sendStringMsg(String msg) {
        ByteBuf message = Unpooled.buffer(msg.getBytes().length);
        message.writeBytes(msg.getBytes());
        ChannelFuture future = socketChannel.writeAndFlush(message);
        future.addListener((ChannelFutureListener) channelFuture -> {
            log.info("??????????????????????????????={}", msg);
        });
    }

    /**
     * ?????? google protocol ??????????????????
     */
    public void sendGoogleProtocolMsg(GoogleProtocolVo googleProtocolVo) {
        CIMRequestProto.CIMReqProtocol protocol = CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(googleProtocolVo.getRequestId())
                .setReqMsg(googleProtocolVo.getMsg())
                .setType(Constants.CommandType.MSG)
                .build();

        ChannelFuture future = socketChannel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture -> {
            log.info("????????????????????? Google Protocol ??????={}", googleProtocolVo.toString());
        });
    }

    /**
     * 1???clear route information
     * 2???reconnect
     * 3???shutdown reconnect job
     * 4???reset reconnect state
     * @throws Exception
     */
    public void reconnect() throws Exception {
        if (socketChannel != null && socketChannel.isActive()) {
            return;
        }

        // ????????????????????????,??????
        routeRequest.offline();

        echoService.echo("cim server shutdown, reconnecting....");
        start();
        echoService.echo("Great! reConnect success!!!");
        reconnectManager.reConnectSuccess();
        ContextHolder.clear();
    }
}
