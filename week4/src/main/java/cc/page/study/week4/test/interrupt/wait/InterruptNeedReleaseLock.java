package cc.page.study.week4.test.interrupt.wait;

public class InterruptNeedReleaseLock {

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
                    o.wait();
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
        // 让 thread 先拿到锁
        Thread.sleep(3000);
        synchronized (o) {
            System.out.println("main interrupt begin");
            thread.interrupt();
            // 这里导致锁不被释放，即使thread被打算也不会执行异常后续的代码
            // 所以打断也是先把thread移至等待队列，而只有锁被释放后，才会从等待队列移至同步队列
            // 竞争获取到锁后才会执行异常后续的代码
            for (; ; ) {
            }
//            System.out.println("main interrupt end");
        }

    }
}
