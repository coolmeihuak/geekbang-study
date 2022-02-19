package cc.page.study.week13.core;

import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Kmq {

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqQueue(capacity);
    }

    private String topic;

    private int capacity;

    private KmqQueue queue;

    public boolean send(KmqMessage message) {
        return queue.offer(message);
    }

    public KmqMessage poll() {
        return queue.poll();
    }

    public KmqMessage poll(String consumer) {
        return queue.poll(consumer);
    }

    @SneakyThrows
    public KmqMessage poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

}
