package org.jiang.chat.client.handler;

import com.vdurmont.emoji.EmojiParser;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.ReconnectManager;
import org.jiang.chat.client.service.ShutdownMsg;
import org.jiang.chat.client.service.impl.EchoServiceImpl;
import org.jiang.chat.client.util.SpringBeanFactory;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.jiang.chat.common.protocol.CIMResponseProto;
import org.jiang.chat.common.utils.NettyAttributeUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@ChannelHandler.Sharable
@Slf4j
public class CIMClientHandler extends SimpleChannelInboundHandler<CIMResponseProto.CIMResProtocol> {

    private MsgHandleCaller msgHandleCaller;

    private ThreadPoolExecutor threadPoolExecutor;

    private ScheduledExecutorService scheduledExecutorService;

    private ReconnectManager reconnectManager;

    private ShutdownMsg shutdownMsg;

    private EchoService echoService;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                CIMRequestProto.CIMReqProtocol heartBeat = SpringBeanFactory.getBean("heartBeat", CIMRequestProto.CIMReqProtocol.class);
                ctx.writeAndFlush(heartBeat).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        log.error("IO error,close Channel");
                        future.channel().close();
                    }
                });
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端和服务端建立连接时调用
        log.info("cim server connect success!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (shutdownMsg == null) {
            shutdownMsg = SpringBeanFactory.getBean(ShutdownMsg.class);
        }

        // 用户主动退出,不执行重连逻辑
        if (shutdownMsg.checkStatus()) {
            return;
        }

        if (scheduledExecutorService == null) {
            scheduledExecutorService = SpringBeanFactory.getBean("scheduledTask",ScheduledExecutorService.class);
            reconnectManager = SpringBeanFactory.getBean(ReconnectManager.class);
        }
        log.info("客户端断开了,重新连接!");
        reconnectManager.reConnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CIMResponseProto.CIMResProtocol msg) throws Exception {
        if (echoService == null) {
            echoService = SpringBeanFactory.getBean(EchoServiceImpl.class);
        }

        // 更新心跳时间
        if (msg.getType() == Constants.CommandType.PING) {
            NettyAttributeUtil.updateReaderTime(ctx.channel(),System.currentTimeMillis());
        }

        if (msg.getType() != Constants.CommandType.PING) {
            // 回调消息
            callBackMsg(msg.getResMsg());
            // 将消息中的 emoji 表情转化为 Unicode 编码以便在终端可以展示
            String response = EmojiParser.parseToUnicode(msg.getResMsg());
            echoService.echo(response);
        }
    }

    /**
     * 回调消息
     * @param msg
     */
    private void callBackMsg(String msg) {
        threadPoolExecutor = SpringBeanFactory.getBean("callBackThreadPool",ThreadPoolExecutor.class);
        threadPoolExecutor.execute(() -> {
            msgHandleCaller = SpringBeanFactory.getBean(MsgHandleCaller.class);
            msgHandleCaller.getMsgHandleListener().handle(msg);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常时断开连接
        cause.printStackTrace();
        ctx.close();
    }
}
