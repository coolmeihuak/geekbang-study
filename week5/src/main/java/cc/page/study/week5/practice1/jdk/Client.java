package cc.page.study.week5.practice1.jdk;

public class Client {
    public static void main(String[] args) {
        //真实对象
        Subject realSubject = new RealSubject();

        realSubject.call();

        //代理对象
        Subject proxyClass = (Subject) JdkProxy.getProxy(realSubject);

        proxyClass.call();
    }
}