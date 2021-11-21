package cc.page.study.week3.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.Objects;

public class MyHttpRequestFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        HttpHeaders headers = fullRequest.headers();
        String pwd = headers.get("page-pwd");
        if (Objects.isNull(pwd) || !"x-page".equals(pwd)) {
            throw new IllegalArgumentException("request not allowed");
        }
    }
}
