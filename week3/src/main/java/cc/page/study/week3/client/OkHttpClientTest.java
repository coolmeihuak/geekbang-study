package cc.page.study.week3.client;

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
                .url("http://localhost:8901/")
                // 过滤器
                .header("page-pwd", "x-page")
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (Objects.nonNull(body)) {
                String responseStr = body.string();
                System.out.println(responseStr);
            }
            String xGeek = response.headers().get("x-geek");
            System.out.println(xGeek);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
