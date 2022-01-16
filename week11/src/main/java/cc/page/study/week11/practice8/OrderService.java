package cc.page.study.week11.practice8;

import java.util.concurrent.CountDownLatch;

public class OrderService {

    private static int number = 100;

    private static final DistributedLock lock = new DistributedLock("order");

    private final CountDownLatch countDownLatch;

    public OrderService(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void reduceOrder() {
        lock.lock();
        number--;
        System.out.println("reduceOrder, number = " + number);
        lock.unlock();
        countDownLatch.countDown();
    }

    public int getNumber() {
        return number;
    }
}
