package cc.page.jvmcore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8801");
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
    }
}
