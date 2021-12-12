package cc.page.study.week4.test.notify;

/**
 * notify 是否会立即获取锁
 */
public class NotifyWait {

    public static void main(String[] args) throws InterruptedException {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                try {
                    System.out.println("thread in sleep");
                    Thread.sleep(3000);
                    System.out.println("wait in");
                    o.wait();
                    System.out.println("wait out");
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException");
                }
            }
        });

        Thread third = new Thread(() -> {
            synchronized (o) {
                System.out.println("third thread");
                try {
                    Thread.sleep(5000);
                    System.out.println("third thread over");
                } catch (InterruptedException e) {
                    System.out.println("third thread InterruptedException");
                }
            }
        });

        Thread notify = new Thread(() -> {
            // 不获取o的锁就无法通知wait的线程继续执行
            // 获取到o的锁后，
            synchronized (o) {
                System.out.println("main synchronized (o) in");
                o.notify();
                System.out.println("main synchronized (o) out");
            }
        });

        thread.start();
        System.out.println("thread.start();");

        // 这里 third 先阻塞，notify 后阻塞，但是notify先获取到锁
        third.start();
        System.out.println("third.start();");

        notify.start();
        System.out.println("notify.start();");

        // console：notify 退出后 third 先执行，wait 的最后执行
        // 说明 notify 的唤醒的 wait 线程不一定马上执行，也要竞争获取锁
        /**
        thread.start();
        thread in sleep
        third.start();
        notify.start();
        wait in
        main synchronized (o) in
        main synchronized (o) out
        third thread
        third thread over
        wait out
         */
    }
}
