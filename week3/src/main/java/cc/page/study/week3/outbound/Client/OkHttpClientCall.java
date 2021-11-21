package cc.page.study.week3.outbound.Client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class OkHttpClientCall extends AbstractClientCall {

    private static final OkHttpClient client = new OkHttpClient();

    private final Request.Builder builder;

    public OkHttpClientCall() {
        builder = new Request.Builder();
    }

    @Override
    public void setHeader(String name, String value) {
        builder.addHeader(name, value);
    }

    @Override
    public void execute(String url, Consumer<byte[]> consumer) {
        Request request = builder.url(url).build();
        try (Response response = client.newCall(request).execute()){
            ResponseBody body = response.body();
            if (Objects.nonNull(body) && Objects.nonNull(consumer)) {
                consumer.accept(body.bytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
