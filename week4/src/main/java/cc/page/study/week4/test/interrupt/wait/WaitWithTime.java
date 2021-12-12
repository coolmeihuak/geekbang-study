package cc.page.study.week4.test.interrupt.wait;

public class WaitWithTime {

    public static void main(String[] args) throws InterruptedException {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                try {
                    System.out.println("wait");
                    // 如果不notify或者interrupt会一直waiting
//                    o.wait();
                    // 5秒后自动唤醒，自动竞争获取锁
                    o.wait(5000);
                    System.out.println("wait out");
                } catch (InterruptedException e) {
                    System.out.println("interrupt");
                }
            }
        });

        thread.start();
        // 让thread先执行，先加锁
        Thread.sleep(1000);
        synchronized (o) {
            System.out.println("main synchronized");
            // 不释放锁，thread会一直waiting，即使时间到了
//            for(;;);
        }
    }
}
