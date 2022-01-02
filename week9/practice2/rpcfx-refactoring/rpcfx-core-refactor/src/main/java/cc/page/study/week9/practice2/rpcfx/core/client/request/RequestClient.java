package cc.page.study.week9.practice2.rpcfx.core.client.request;

import cc.page.study.week9.practice2.rpcfx.core.api.RpcRequest;
import cc.page.study.week9.practice2.rpcfx.core.api.RpcResponse;
import okhttp3.MediaType;

public interface RequestClient {

    MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    RpcResponse post(RpcRequest req, String url);

}
