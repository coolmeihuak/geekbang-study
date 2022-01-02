package cc.page.study.week9.practice2.rpcfx.core.netty.client;

import cc.page.study.week9.practice2.rpcfx.core.netty.common.RpcDecoder;
import cc.page.study.week9.practice2.rpcfx.core.netty.common.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("Message Encoder", new RpcEncoder());
        pipeline.addLast("Message Decoder", new RpcDecoder());
        pipeline.addLast("clientHandler", new RpcClientSyncHandler());
    }
}
