package cc.page.study.week5.practice1.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements InvocationHandler {
    /**
     * 因为需要处理真实角色，所以要把真实角色传进来
     */
    private final Object proxyObject;

    public JdkProxy(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用代理类 开始");
        Object result = method.invoke(proxyObject, args);
        System.out.println("调用代理类 结束");
        return result;
    }

    public static Object getProxy(Object proxyObject) {
        JdkProxy jdkProxy = new JdkProxy(proxyObject);
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), proxyObject.getClass().getInterfaces(), jdkProxy);
    }
}