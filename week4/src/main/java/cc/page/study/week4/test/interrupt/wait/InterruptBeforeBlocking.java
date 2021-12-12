package cc.page.study.week4.test.interrupt.wait;

/**
 * 线程阻塞之前被打断
 * 打断标志被设为
 */
public class InterruptBeforeBlocking {

    public static void main(String[] args) {

        Object o = new Object();

        Thread thread = new Thread(() -> {
            synchronized (o) {
//                for (int i = 0; i < 10000; i++) {
//                    System.out.println("for (int i = 0; i < 10000; i++)");
//                }
                // interrupted 会重置打断标志
//                while (!Thread.interrupted()) {
//                    System.out.println("while interrupted is false");
//                }
                // 不会重置
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("while isInterrupted is false");
                }
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
        System.out.println("main interrupt begin");
        thread.interrupt();
        System.out.println("main interrupt end");
    }
}
