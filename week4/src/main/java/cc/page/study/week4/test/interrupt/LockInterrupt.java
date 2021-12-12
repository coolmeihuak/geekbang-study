package cc.page.study.week4.test.interrupt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterrupt {

    public static void main(String[] args) throws InterruptedException {

        Lock lock = new ReentrantLock();

        Thread thread = new Thread(() -> {
            System.out.println("thread run");
            // 让主线程先执行打断
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("while isInterrupted is false");
            }
            System.out.println("thread will lock.lock");
            lock.lock();
            System.out.println("thread in lock.lock");
            lock.unlock();
            System.out.println("thread lock.lock out");
        });
        thread.start();
        // 让thread先运行
        Thread.sleep(5);
        lock.lock();
        System.out.println("main thread lock.lock in");
        thread.interrupt();
//        lock.unlock();
//        System.out.println("main thread lock.lock out");
        for (; ; ) {
            //
        }
//        lock.unlock();
//        System.out.println("main thread lock.lock out");
    }
}
