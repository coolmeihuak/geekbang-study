package cc.page.study.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public void run() throws InterruptedException {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(10);

        ServerBootstrap server = new ServerBootstrap();
//        server.option(ChannelOption.SO_BACKLOG, 128)
//                .childOption(ChannelOption.TCP_NODELAY, true)
//                .childOption(ChannelOption.SO_KEEPALIVE, true)
//                .childOption(ChannelOption.SO_REUSEADDR, true)
//                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
//                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
//                .childOption(EpollChannelOption.SO_REUSEPORT, true)
//                .childOption(ChannelOption.SO_KEEPALIVE, true)
//                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        server.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServerInboundHandler());
                        pipeline.addLast(new ServerOutboundHandler());
                        pipeline.addLast(new Server1InboundHandler());
                        pipeline.addLast(new Server1OutboundHandler());
                    }
                });
        int port = 8080;
        // 对 sync() 方法的调用将导致当前 Thread 阻塞，一直到绑定操作完成为止
        Channel channel = server.bind(port).sync().channel();
        // 该应用程序将会阻塞等待直到服务器的 Channel 关闭（因为你在 Channel 的 CloseFuture 上调用了 sync() 方法）
        channel.closeFuture().sync();
        System.out.println();
    }

    public void destroy() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
