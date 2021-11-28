package cc.page.study.week4.practice2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Practice2 {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        final int[] result = {0};

        // wait notify 方式
//        waitNotify(result);
        // sleep and sync
//        sleepAndSync(result);
        // sleep and lock
//        sleepAndLock(result);
        // join 方式
//        join(result);
        lockAndCondition(result);

        System.out.println("异步计算结果为：" + result[0]);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 利用 Object 的 wait 和 notify
     */
    private static void waitNotify(int[] result) throws InterruptedException {
        Object o = new Object();
        Thread subT = new Thread(() -> {
            synchronized (o) {
                result[0] = sum();
                o.notify();
            }
        });
        subT.start();
        synchronized (o) {
            o.wait();
        }
    }

    /**
     * 利用 Thread join
     */
    private static void join(int[] result) throws InterruptedException {
        Thread subT = new Thread(() -> {
            result[0] = sum();
        });
        subT.start();
        subT.join();
    }

    private static void sleepAndSync(int[] result) throws InterruptedException {
        Object o = new Object();
        Thread subT = new Thread(() -> {
            synchronized (o) {
                result[0] = sum();
            }
        });
        subT.start();
        // 确保 subT 先加锁执行，正常 subT 开始执行不会超过秒级
        Thread.sleep(3000);
        // synchronized 保证 subT 执行完
        synchronized (o) {
        }
    }

    private static void sleepAndLock(int[] result) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Thread subT = new Thread(() -> {
            try {
                lock.lock();
                result[0] = sum();
            } finally {
                lock.unlock();
            }
        });
        subT.start();
        // 确保 subT 先加锁执行，正常 subT 开始执行不会超过秒级
        Thread.sleep(100);
        try {
            lock.lock();
        } finally {
            lock.unlock();
        }
    }

    private static void lockAndCondition(int[] result) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition main = lock.newCondition();
        Condition sub = lock.newCondition();
        Thread subT = new Thread(() -> {
            try {
                lock.lock();
                result[0] = sum();
                main.signal();
            } finally {
                lock.unlock();
            }
        });
        subT.start();
        try {
            lock.lock();
            main.await();
        } finally {
            lock.unlock();
        }
    }

    // 24157817
    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
