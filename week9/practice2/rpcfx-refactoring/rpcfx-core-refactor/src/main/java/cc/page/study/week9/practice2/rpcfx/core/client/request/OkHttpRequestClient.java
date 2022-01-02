package cc.page.study.week9.practice2.rpcfx.core.client.request;

import cc.page.study.week9.practice2.rpcfx.core.api.RpcRequest;
import cc.page.study.week9.practice2.rpcfx.core.api.RpcResponse;
import cc.page.study.week9.practice2.rpcfx.core.exception.RpcException;
import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class OkHttpRequestClient implements RequestClient {

    @Override
    public RpcResponse post(RpcRequest req, String url) {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = null;
        try {
            respJson = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new RpcException(e.getMessage(), e);
        }
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcResponse.class);
    }
}
