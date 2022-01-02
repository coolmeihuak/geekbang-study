package cc.page.study.week9.practice2.rpcfx.core.client.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy<T> extends RpcStub<T> implements MethodInterceptor {
    public CglibProxy(Class<T> serviceClass, String url) {
        super(serviceClass, url);
    }

    @Override
    public T create() {
        Enhancer enhancer = new Enhancer();
        // 设置需要代理的对象
        enhancer.setSuperclass(serviceClass.getClass());
        // 设置代理人
//        enhancer.setCallback(new CglibProxy<>(serviceClass, url));
        enhancer.setCallback(this);
        return (T)enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return process(serviceClass, method, objects, url);
    }
}
