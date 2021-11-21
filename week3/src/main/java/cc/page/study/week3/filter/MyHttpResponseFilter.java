package cc.page.study.week3.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class MyHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("x-geek", "hello, geek MyHttpResponseFilter");
    }
}
