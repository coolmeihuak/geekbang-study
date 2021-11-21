package cc.page.study.week3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

    public static void main(String[] args) {
        HttpGet httpGet = new HttpGet("http://localhost:8901/");
        // 过滤器
        httpGet.setHeader("page-pwd", "x-page");
        int cores = Runtime.getRuntime().availableProcessors();
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
        httpclient.execute(httpGet, new FutureCallback<HttpResponse>(){

            @lombok.SneakyThrows
            @Override
            public void completed(HttpResponse httpResponse) {
                String body = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(body);
                String xGeek = httpResponse.getFirstHeader("x-geek").getValue();
                System.out.println(xGeek);
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
