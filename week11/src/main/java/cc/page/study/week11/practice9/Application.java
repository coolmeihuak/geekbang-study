package cc.page.study.week11.practice9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(8);
    }
}
