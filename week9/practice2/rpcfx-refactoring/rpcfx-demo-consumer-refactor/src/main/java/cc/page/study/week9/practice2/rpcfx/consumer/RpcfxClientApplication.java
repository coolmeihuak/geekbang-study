package cc.page.study.week9.practice2.rpcfx.consumer;

import cc.page.study.week9.practice2.rpcfx.api.User;
import cc.page.study.week9.practice2.rpcfx.api.UserService;
import cc.page.study.week9.practice2.rpcfx.core.client.Rpcfx;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxClientApplication {

    public static void main(String[] args) {

        UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());
    }
}



