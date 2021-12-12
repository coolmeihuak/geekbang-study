package cc.page.study.week4;

import java.util.Random;

public class MyPractice1 {

    private volatile static int cot;

    public static void main(String[] args) throws InterruptedException {
//        test1();
        test2();
    }

    private static void test2() throws InterruptedException {
        Runnable write = () -> {
            for(;;) {
//                Random r = new Random();
//                int time = r.nextInt(5) * 100;
                cot++;
                System.out.println("write thread, count = " + cot);
//                try {
//                    Thread.sleep(time);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        };
        Thread wt1 = new Thread(write);
        Thread wt2 = new Thread(write);
        Thread wt3 = new Thread(write);

        Thread rt1 = new Thread(() -> {
            for(;;) {
//                Random r = new Random();
//                int time = r.nextInt(5) * 1000;
                System.out.println("read thread, count = " + cot);
//                try {
//                    Thread.sleep(time);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        rt1.start();
        wt1.start();
        wt2.start();
        wt3.start();
        rt1.join();
        wt1.join();
        wt2.join();
        wt3.join();
    }

    private static void test1() throws InterruptedException {
        final int[] count = {0};
        Thread wt1 = new Thread(() -> {
            for(;;) {
//                Random r = new Random();
//                int time = r.nextInt(5) * 1000;
                count[0] = count[0] + 1;
                System.out.println("write thread, count = " + count[0]);
//                try {
//                    Thread.sleep(time);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });

        Thread rt1 = new Thread(() -> {
            for(;;) {
//                Random r = new Random();
//                int time = r.nextInt(5) * 1000;
                System.out.println("read thread, count = " + count[0]);
//                try {
//                    Thread.sleep(time);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        rt1.start();
        wt1.start();
        rt1.join();
        wt1.join();
    }
}
