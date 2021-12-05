package cc.page.study.week5.practice1.cglib;

public class Client {
    public static void main(String[] args) {
        Engineer e = new Engineer();
        // 生成 Cglib 代理类
        Engineer engineerProxy = (Engineer) CglibProxy.getProxy(e);
        // 调用相关方法
        engineerProxy.eat();
        engineerProxy.work();
    }
}