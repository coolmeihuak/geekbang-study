package cc.page.study.week3.router;

import java.util.List;
import java.util.Random;

public class WeightHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> urls) {
        Random random = new Random(System.currentTimeMillis());
        int id = random.nextInt(10);
        int key;
        if (id < 2) {
            // server1 20%
            key = 0;
        } else if (id < 5) {
            // server2 30%
            key = 1;
        } else {
            // server3 50%
            key = 2;
        }
        return urls.get(key);
    }
}
