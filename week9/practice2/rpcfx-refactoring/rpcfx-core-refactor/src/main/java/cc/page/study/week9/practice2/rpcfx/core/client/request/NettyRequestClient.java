package cc.page.study.week9.practice2.rpcfx.core.client.request;

import cc.page.study.week9.practice2.rpcfx.core.api.RpcRequest;
import cc.page.study.week9.practice2.rpcfx.core.api.RpcResponse;
import cc.page.study.week9.practice2.rpcfx.core.exception.RpcException;
import cc.page.study.week9.practice2.rpcfx.core.netty.client.RpcClientInitializer;
import cc.page.study.week9.practice2.rpcfx.core.netty.client.RpcClientSyncHandler;
import cc.page.study.week9.practice2.rpcfx.core.netty.common.RpcProtocol;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class NettyRequestClient implements RequestClient {

    private static class NettyRequestClientInstance {
        public static NettyRequestClient instance = new NettyRequestClient();
    }

    public static NettyRequestClient getInstance() {
        return NettyRequestClientInstance.instance;
    }

    private NettyRequestClient() {
    }

    private ConcurrentHashMap<String, Channel> channelPool = new ConcurrentHashMap<>();
    private EventLoopGroup clientGroup = new NioEventLoopGroup(new ThreadFactoryBuilder().setNameFormat("client work-%d").build());

    @Override
    public RpcResponse post(RpcRequest req, String url) {
        RpcProtocol request = convertNettyRequest(req);

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RpcException(e.getMessage(), e);
        }
        String cacheKey = uri.getHost() + ":" + uri.getPort();

        // 查看缓存池中是否有可重用的channel
        if (channelPool.containsKey(cacheKey)) {
            Channel channel = channelPool.get(cacheKey);
            if (!channel.isActive() || !channel.isWritable() || !channel.isOpen()) {
                log.debug("Channel can't reuse");
            } else {
                try {
                    RpcClientSyncHandler handler = new RpcClientSyncHandler();
                    handler.setLatch(new CountDownLatch(1));
                    channel.pipeline().replace("clientHandler", "clientHandler", handler);
                    channel.writeAndFlush(request).sync();
                    return handler.getResponse();
                } catch (Exception e) {
                    log.debug("channel reuse send msg failed!");
                    channel.close();
                    channelPool.remove(cacheKey);
                }
                log.debug("Handler is busy, please user new channel");
            }
        }

        // 没有或者不可用则新建
        // 并将最终的handler添加到pipeline中，拿到结果后返回
        RpcClientSyncHandler handler = new RpcClientSyncHandler();
        handler.setLatch(new CountDownLatch(1));

        Channel channel = null;
        try {
            channel = createChannel(uri.getHost(), uri.getPort());
        } catch (InterruptedException e) {
            throw new RpcException(e.getMessage(), e);
        }
        channel.pipeline().replace("clientHandler", "clientHandler", handler);
        channelPool.put(cacheKey, channel);

        try {
            channel.writeAndFlush(request).sync();
        } catch (InterruptedException e) {
            throw new RpcException(e.getMessage(), e);
        }
        try {
            return handler.getResponse();
        } catch (InterruptedException e) {
            throw new RpcException(e.getMessage(), e);
        }
    }

    private Channel createChannel(String address, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
        return bootstrap.connect(address, port).sync().channel();
    }

    private RpcProtocol convertNettyRequest(RpcRequest rpcRequest) {
        RpcProtocol request = new RpcProtocol();
        String requestJson = JSON.toJSONString(rpcRequest);
        request.setLen(requestJson.getBytes(CharsetUtil.UTF_8).length);
        request.setContent(requestJson.getBytes(CharsetUtil.UTF_8));
        return request;
    }
}
