package cc.page.study.week3.outbound.Client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class HttpAsyncClientCall extends AbstractClientCall {

    private final CloseableHttpAsyncClient httpclient;

    private final HttpGet httpGet;

    public HttpAsyncClientCall() {
        httpGet = new HttpGet();
        int cores = Runtime.getRuntime().availableProcessors();
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();
        httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
    }

    @Override
    public void setHeader(String name, String value) {
        httpGet.setHeader(name, value);
    }

    @Override
    public String getHeader(String name) {
        if (Objects.isNull(header)) {
            Header h = this.httpGet.getFirstHeader(name);
            if (Objects.nonNull(h))
                return h.getValue();
            return null;
        }
        return header.get(name);
    }

    @Override
    public void execute(String url, Consumer<byte[]> consumer) {
        URI uri = URI.create(url);
        httpGet.setURI(uri);
        httpclient.execute(httpGet, new FutureCallback<HttpResponse>(){

            @lombok.SneakyThrows
            @Override
            public void completed(HttpResponse httpResponse) {
                Header[] oh = httpResponse.getAllHeaders();
                for (Header h : oh) {
                    header.put(h.getName(), h.getValue());
                }
                byte[] body = EntityUtils.toByteArray(httpResponse.getEntity());
                if (Objects.nonNull(consumer)) {
                    consumer.accept(body);
                }
            }

            @Override
            public void failed(Exception e) {
                httpGet.abort();
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }
}
