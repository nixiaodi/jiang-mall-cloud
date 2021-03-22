package org.jiang.chat.server.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.jiang.chat.common.protocol.CIMRequestProto;
import org.jiang.chat.server.handler.CIMServerHandler;

/**
 * @author 蒋小胖
 */
public class CIMServerInitializer extends ChannelInitializer<Channel> {

    private final CIMServerHandler cimServerHandler = new CIMServerHandler();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
            //11 秒没有向客户端发送消息就发生心跳
            .addLast(new IdleStateHandler(11,0,0))
            // google Protobuf 编解码
            .addLast(new ProtobufVarint32FrameDecoder())
            .addLast(new ProtobufDecoder(CIMRequestProto.CIMReqProtocol.getDefaultInstance()))
            .addLast(new ProtobufEncoder())
            .addLast(cimServerHandler);
    }
}
