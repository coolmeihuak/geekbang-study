package cc.page.study.netty.server;

public class NettyServerApplication {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        try {
            server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            server.destroy();
        }
    }
}
