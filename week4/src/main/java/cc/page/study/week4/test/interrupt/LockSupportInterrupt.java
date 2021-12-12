package cc.page.study.week4.test.interrupt;

import java.util.concurrent.locks.LockSupport;

public class LockSupportInterrupt {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("thread run");
            System.out.println("thread LockSupport.park in");
            // 可以让LockSupport.park()执行前就被打断
//            while (!Thread.currentThread().isInterrupted()) {
//                System.out.println("while isInterrupted is false");
//            }
            LockSupport.park();
            System.out.println("thread LockSupport.park out");
        });
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
