package cc.page.study.week9.practice2.rpcfx.core.client.proxy;

import cc.page.study.week9.practice2.rpcfx.core.api.RpcRequest;
import cc.page.study.week9.practice2.rpcfx.core.api.RpcResponse;
import cc.page.study.week9.practice2.rpcfx.core.client.request.NettyRequestClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public abstract class RpcStub<T> {

    protected final Class<T> serviceClass;
    protected final String url;

    public RpcStub(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public abstract T create();

    public Object process(Class<T> service, Method method, Object[] params, String url) {
        // 构建 rpc 请求
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceClass(service.getName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setParams(params);


        RpcResponse rpcResponse = NettyRequestClient.getInstance().post(rpcRequest, url);

        if (!rpcResponse.getStatus()) {
            log.info("Client receive exception");
            rpcResponse.getException().printStackTrace();
            return null;
        }

        // 序列化成对象返回
        log.info("Response:: " + rpcResponse.getResult());
        return JSON.parse(rpcResponse.getResult().toString());
    }
}
