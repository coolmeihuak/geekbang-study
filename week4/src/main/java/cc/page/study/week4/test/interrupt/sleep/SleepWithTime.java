package cc.page.study.week4.test.interrupt.sleep;

public class SleepWithTime {

    public static void main(String[] args) throws InterruptedException {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                try {
                    System.out.println("sleep");
                    // 5秒后自动唤醒，自动竞争获取锁
                    Thread.sleep(5000);
                    System.out.println("sleep out");
                } catch (InterruptedException e) {
                    System.out.println("interrupt");
                }
            }
        });

        thread.start();
        // 让thread先执行，先加锁
        Thread.sleep(1000);
        synchronized (o) {
            // 5秒后才打印，说明sleep不释放锁，直到synchronized块执行完
            System.out.println("main synchronized");
            // 不释放锁，thread会一直waiting，即使时间到了
            for(;;);
        }
    }
}
