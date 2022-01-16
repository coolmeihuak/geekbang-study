package cc.page.study.week11.practice9;

import com.google.gson.Gson;
import lombok.val;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutorService;

@Component
public class SubscribeUtils {

    private final JedisPool jedisPool;

    private final ExecutorService executorService;

    private final OrderService orderService;

    public SubscribeUtils(JedisPool jedisPool, ExecutorService executorService, OrderService orderService) {
        this.jedisPool = jedisPool;
        this.executorService = executorService;
        this.orderService = orderService;
    }

    public void subscribe() {
        executorService.execute(() -> {
            val pubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if (message.isEmpty()) {
                        System.out.println("SubPub End");
                        System.exit(0);
                    }
                    System.out.printf("Receive message from %s :: %s\n", channel, message);
                    OrderModel model = new Gson().fromJson(message, OrderModel.class);
                    orderService.add(model);
                }
            };
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(pubSub, "orderService#add");
            }
        });
    }
}
