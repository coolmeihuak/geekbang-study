package cc.page.study.week11.practice8;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        int size = 6;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        OrderService orderService = new OrderService(countDownLatch);
        System.out.println("begin number = " + orderService.getNumber());
        for (int i = 0; i < size; i++) {
            executorService.execute(orderService::reduceOrder);
        }
        countDownLatch.await();
        System.out.println("end number = " + orderService.getNumber());
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
