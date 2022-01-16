package cc.page.study.week11.practice8;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DistributedLock {

    private final String lockKey;

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private final JedisPool jedisPool = new JedisPool();

    public DistributedLock(String lockKey) {
        this.lockKey = lockKey;
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        lock.lock();
                        try {
                            System.out.printf("Receive message from channel(%s) :: message(%s)%n", channel, message);
                            condition.signal();
                        } finally {
                            lock.unlock();
                        }
                    }
                }, channelName(lockKey));
            }
        }, "subscriberThread").start();
    }

    public void lock() {
        try (Jedis jedis = jedisPool.getResource()) {
            Thread thread = Thread.currentThread();
            for (;;) {
                // 加锁标志
                boolean lockFlag = "OK".equals(jedis.set(lockKey, lockKey, "NX", "EX", 1));
                System.out.printf("Thread(%s) lock lockKey(%s) %s%n", thread.getName(), lockKey, lockFlag);
                if (!lockFlag) {
                    lock.lock();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    break;
                }
            }
        }
    }

    public void unlock() {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " + "return redis.call('del',KEYS[1]) else return 0 end";
        try (Jedis jedis = jedisPool.getResource()) {
            for (;;) {
                boolean unlockFlag = jedis.eval(luaScript, Collections.singletonList(lockKey), Collections.singletonList(lockKey)).equals(1L);
                Thread thread = Thread.currentThread();
                System.out.printf("Thread(%s) unlock lockKey(%s) %s%n", thread.getName(), lockKey, unlockFlag);
                if (unlockFlag) {
                    // 通知因加锁失败而阻塞的线程
                    jedis.publish(channelName(lockKey), "unlock");
                    break;
                }
            }
        }
    }

    private String channelName(String lockKey) {
        return "channel" + lockKey;
    }
}
