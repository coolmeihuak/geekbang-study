package cc.page.jvmcore;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;

public class OkHttpClientTest {

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8801")
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (Objects.nonNull(body)) {
                String responseStr = body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
