package cc.page.study.week11.practice9;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;

@Component
public class PublishUtils {

    private final JedisPool jedisPool;

    private final ExecutorService executorService;

    public PublishUtils(JedisPool jedisPool, ExecutorService executorService) {
        this.jedisPool = jedisPool;
        this.executorService = executorService;
    }

    public void publish(OrderModel model) {
        System.out.println("Start publisher order");
        try (Jedis jedis = jedisPool.getResource()) {
            String message = new Gson().toJson(model);
            jedis.publish("orderService#add", message);
        }
    }
}
