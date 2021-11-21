package cc.page.study.week3.busynessserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer3 {

    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2);
        ServerSocket serverSocket = new ServerSocket(8803);
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("next accept");
            executorService.execute(() -> service(socket));
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("HTTP/1.1 200 OK");
//            out.println("Content-Type:text/html;charset=uft-8");
            out.println("Content-Type:text/html");
            String body = "hello, nio SocketServer3";
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
