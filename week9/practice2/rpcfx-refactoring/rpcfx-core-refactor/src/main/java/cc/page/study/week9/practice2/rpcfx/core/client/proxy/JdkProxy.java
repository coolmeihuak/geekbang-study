package cc.page.study.week9.practice2.rpcfx.core.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy<T> extends RpcStub<T> implements InvocationHandler {
    public JdkProxy(Class<T> serviceClass, String url) {
        super(serviceClass, url);
    }

    @Override
    public T create() {
        ClassLoader loader = JdkProxy.class.getClassLoader();
        Class<T>[] classes = new Class[]{serviceClass};
        return (T)Proxy.newProxyInstance(loader, classes, new JdkProxy<T>(serviceClass, url));
//        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), serviceClass.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return process(serviceClass, method, args, url);
    }
}
