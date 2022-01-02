package cc.page.study.week9.practice2.rpcfx.core.client;


import cc.page.study.week9.practice2.rpcfx.core.api.*;
import cc.page.study.week9.practice2.rpcfx.core.client.proxy.CglibProxy;
import cc.page.study.week9.practice2.rpcfx.core.client.proxy.JdkProxy;
import cc.page.study.week9.practice2.rpcfx.core.client.proxy.RpcStub;
import cc.page.study.week9.practice2.rpcfx.core.client.request.RequestClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class Rpcfx {

    public static <T> T create(final Class<T> serviceClass, final String url) {

        return new JdkProxy<T>(serviceClass, url).create();
    }
}
