package org.jiang.chat.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.jiang.chat.server.api.vo.request.SendMessageRequestVo;
import org.jiang.chat.server.initializer.CIMServerInitializer;
import org.jiang.chat.server.util.SessionSocketHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Objects;

@Component
@Slf4j
public class CIMServer {

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();

    @Value("${cim.server.port}")
    private int nettyPort;

    /**
     * 启动 cim server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new CIMServerInitializer());

        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("Start cim server success!!!");
        }
    }

    /**
     * 销毁 cim server
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        worker.shutdownGracefully().syncUninterruptibly();
        log.info("Close cim server success!!!");
    }

    /**
     * push message to client
     * @param sendMessageRequestVo
     */
    public void sendMessage(SendMessageRequestVo sendMessageRequestVo) {
        NioSocketChannel channel = SessionSocketHolder.get(sendMessageRequestVo.getUserId());

        if (Objects.isNull(channel)) {
            log.error("client {} offline!", sendMessageRequestVo.getUserId());
            return;
        }

        CIMRequestProto.CIMReqProtocol protocol = CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(sendMessageRequestVo.getUserId())
                .setReqMsg(sendMessageRequestVo.getMessage())
                .setType(Constants.CommandType.MSG)
                .build();

        ChannelFuture future = channel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                log.info("server push msg:[{}]", sendMessageRequestVo.toString()));
    }

}
