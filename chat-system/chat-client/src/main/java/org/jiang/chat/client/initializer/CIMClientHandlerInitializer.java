package org.jiang.chat.client.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.jiang.chat.client.handler.CIMClientHandler;
import org.jiang.chat.common.protocol.CIMResponseProto;

public class CIMClientHandlerInitializer extends ChannelInitializer<Channel> {

    private final CIMClientHandler cimClientHandler = new CIMClientHandler();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 10s内没发送消息，将IdleStateHandler 添加到 ChannelPipeline 中
                .addLast(new IdleStateHandler(0,10,0))
                // google Protobuf 编解码
                //拆包解码
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(CIMResponseProto.CIMResProtocol.getDefaultInstance()))
                //
                //拆包编码
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(cimClientHandler);
    }
}
