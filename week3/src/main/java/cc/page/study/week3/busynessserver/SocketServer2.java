package cc.page.study.week3.busynessserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer2 {

    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket();
        ServerSocket serverSocket = new ServerSocket(8802);
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("next accept");
            // 本级这里访问一直超时，暂时用单线程代替
//            new Thread(() -> service(socket));
            service(socket);
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("HTTP/1.1 200 OK");
//            out.println("Content-Type:text/html;charset=uft-8");
            out.println("Content-Type:text/html");
            String body = "hello, nio SocketServer2";
            out.println("Content-Length:" + body.getBytes().length);
            out.println();
            out.println(body);
//            out.write(body);
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
