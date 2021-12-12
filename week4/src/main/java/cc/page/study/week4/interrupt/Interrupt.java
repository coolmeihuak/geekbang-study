package cc.page.study.week4.interrupt;

import java.util.concurrent.TimeUnit;

public class Interrupt {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        System.out.println("will sleep");
                        this.wait();
                        System.out.println("sleep");
                    }
                } catch (InterruptedException e) {
                    boolean isI = Thread.currentThread().isInterrupted();
                    System.out.println("i am interrupt, flag = " + isI);
                }
            }
        }, "ttttttttt");
        System.out.println(t1);
        System.out.println(t1.isInterrupted());
//        System.out.println(Thread.currentThread());
//        System.out.println(Thread.currentThread().isInterrupted());
        t1.start();
        Thread.sleep(5000);
        System.out.println(t1);
        System.out.println(t1.isInterrupted());
        System.out.println(Thread.currentThread());
        System.out.println(Thread.currentThread().isInterrupted());
        t1.interrupt();
        System.out.println(t1);
        System.out.println(t1.isInterrupted());
        System.out.println(Thread.currentThread());
        System.out.println(Thread.currentThread().isInterrupted());

        TimeUnit.SECONDS.sleep(1);
    }
}