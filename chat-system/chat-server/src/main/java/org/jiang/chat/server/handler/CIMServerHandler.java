package org.jiang.chat.server.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.kit.HeartBeatHandler;
import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.jiang.chat.common.utils.NettyAttributeUtil;
import org.jiang.chat.server.kit.RouteHandler;
import org.jiang.chat.server.kit.ServerHeartBeatHandlerImpl;
import org.jiang.chat.server.util.SessionSocketHolder;
import org.jiang.chat.server.util.SpringBeanFactory;

@Slf4j
@ChannelHandler.Sharable
public class CIMServerHandler extends SimpleChannelInboundHandler<CIMRequestProto.CIMReqProtocol> {

    /**
     * 取消绑定
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发channelInactive
        CIMUserInfo userInfo = SessionSocketHolder.getUserId(((NioSocketChannel) ctx.channel()));
        if (userInfo != null) {
            log.error("[{}] trigger channelInactive offline!",userInfo.getUserName());
        }

        //Clear route info and offline
        RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
        routeHandler.userOffline(userInfo, ((NioSocketChannel) ctx.channel()));
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("定时检测客户端端是否存活");
                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CIMRequestProto.CIMReqProtocol msg) throws Exception {
        log.info("received msg=[{}]", msg.toString());

        if (msg.getType() == Constants.CommandType.LOGIN) {
            //保存客户端与 Channel 之间的关系
            SessionSocketHolder.put(msg.getRequestId(), ((NioSocketChannel) ctx.channel()));
            SessionSocketHolder.saveSession(msg.getRequestId(),msg.getReqMsg());
            log.info("client [{}] online success!!", msg.getReqMsg());
        }

        // 心跳更新时间
        if (msg.getType() == Constants.CommandType.PING) {
            NettyAttributeUtil.updateReaderTime(ctx.channel(),System.currentTimeMillis());
            // 向客户端响应 pong 消息
            CIMRequestProto.CIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat", CIMRequestProto.CIMReqProtocol.class);
            ctx.writeAndFlush(heartBeat).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    log.error("IO error,close Channel");
                    future.channel().close();
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ChatException.isResetByPeer(cause.getMessage())) {
            return;
        }
        log.error(cause.getMessage(), cause);
    }
}
