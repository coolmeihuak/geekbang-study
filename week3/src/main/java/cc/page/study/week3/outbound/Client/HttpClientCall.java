package cc.page.study.week3.outbound.Client;

import io.netty.util.CharsetUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;

public class HttpClientCall extends AbstractClientCall {

    private final HttpClient httpclient;
    private final HttpGet httpGet;

    public HttpClientCall() {
        httpclient = HttpClientBuilder.create().build();
        httpGet = new HttpGet();
    }

    @Override
    public void setHeader(String name, String value) {
        httpGet.setHeader(name, value);
    }

    @Override
    public void execute(String url, Consumer<byte[]> consumer) {
        httpGet.setURI(URI.create(url));
        try {
            HttpResponse response = httpclient.execute(httpGet);
            Header[] hs = response.getAllHeaders();
            for (Header h : hs) {
                this.header.put(h.getName(), h.getValue());
            }
            if (Objects.nonNull(consumer)) {
                consumer.accept(EntityUtils.toByteArray(response.getEntity()));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
