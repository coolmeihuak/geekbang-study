package cc.page.study.week4.test.notify;

public class NotifyWaitWithTime {

    public static void main(String[] args) throws InterruptedException {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println("wait in");
                try {
                    o.wait(10000);
                    System.out.println("wait end");
                } catch (InterruptedException e) {
                    System.out.println("isInterrupted is " + Thread.currentThread().isInterrupted());
                    // 如果不知道做什么设置为true，让d外部Thread.currentThread().isInterrupted()获取打断状态
                    Thread.currentThread().interrupt();
                    System.out.println("isInterrupted is " + Thread.currentThread().isInterrupted());
                    System.out.println("InterruptedException");
                }
            }
        });
        thread.start();
        // 让 thread 先执行wait
        Thread.sleep(1000);
        synchronized (o) {
            System.out.println("main interrupt begin");
            // wait的时间还没到但是会马上打断
            o.notify();
            System.out.println("main interrupt end");
        }
    }
}
