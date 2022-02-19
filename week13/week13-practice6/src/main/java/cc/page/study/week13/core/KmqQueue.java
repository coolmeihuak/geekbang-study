package cc.page.study.week13.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KmqQueue {

    private ArrayList<KmqMessage> messages;

    // 消息写入位置
    private int location;

    private Map<String, Integer> consumerIndexMap;

    private static final String DEFAULT_CONSUMER = "defaultConsumer";

    public KmqQueue() {
        this(10);
    }

    public KmqQueue(int capacity) {
        messages = new ArrayList(capacity);
        location = 0;
        consumerIndexMap = new HashMap<>();
    }

    public boolean offer(KmqMessage message) {
        int old = messages.size();
        location = old;
        return messages.add(message);
    }

    public KmqMessage poll() {
        return poll(DEFAULT_CONSUMER);
    }

    public KmqMessage poll(long timeout, TimeUnit unit) {
        return poll(DEFAULT_CONSUMER);
    }

    /**
     * 对于每个命名消费者，用指针记录消费位置
     */
    public KmqMessage poll(String consumer) {
        Integer index = 0;
        if (consumerIndexMap.containsKey(consumer)) {
            index = consumerIndexMap.get(consumer);
        }
        KmqMessage message = messages.get(index);
        consumerIndexMap.put(consumer, index + 1);
        return message;
    }
}
