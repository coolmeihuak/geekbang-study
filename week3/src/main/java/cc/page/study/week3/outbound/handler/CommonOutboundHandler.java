package cc.page.study.week3.outbound.handler;

import cc.page.study.week3.filter.HttpRequestFilter;
import cc.page.study.week3.filter.HttpResponseFilter;
import cc.page.study.week3.filter.MyHttpResponseFilter;
import cc.page.study.week3.outbound.Client.ClientCall;
import cc.page.study.week3.outbound.NamedThreadFactory;
import cc.page.study.week3.router.HttpEndpointRouter;
import cc.page.study.week3.router.WeightHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.protocol.HTTP;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class CommonOutboundHandler {

    private final ClientCall clientCall;
    private final ExecutorService proxyService;
    private final List<String> backendUrls;

    HttpResponseFilter filter = new MyHttpResponseFilter();
    HttpEndpointRouter router = new WeightHttpEndpointRouter();

    public CommonOutboundHandler(List<String> backends, ClientCall clientCall) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
        this.clientCall = clientCall;

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        this.proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        clientCall.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        clientCall.setHeader("mao", inbound.headers().get("mao"));

        clientCall.execute(url, bytes -> handleResponse(inbound, ctx, bytes));
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final byte[] bytes) {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));

            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(this.clientCall.getHeader("Content-Length")));

            filter.filter(response);
//            ctx.write(response);
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
