package cc.page.study.week4.test.interrupt.sleep;

public class InterruptSleepWithTime {

    public static void main(String[] args) throws InterruptedException {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                // 这里导致锁不释放，主线程不会触发打断
//                while (!Thread.currentThread().isInterrupted()) {
//                    System.out.println("while isInterrupted is false");
//                }
                System.out.println("exist from loop");
                try {
                    Thread.sleep(10000);
                    System.out.println("sleep end");
                } catch (InterruptedException e) {
//                    System.out.println("interrupted is " + Thread.interrupted());
                    System.out.println("isInterrupted is " + Thread.currentThread().isInterrupted());
                    // 如果不知道做什么设置为true，让外部Thread.currentThread().isInterrupted()获取打断状态
                    Thread.currentThread().interrupt();
                    System.out.println("isInterrupted is " + Thread.currentThread().isInterrupted());
                    System.out.println("InterruptedException");
                }
            }
        });
        thread.start();
        // 让 thread sleep
        Thread.sleep(100);
        System.out.println("main interrupt begin");
        // sleep 的时间还没到但是会马上打断
        thread.interrupt();
        System.out.println("main interrupt end");
    }
}
