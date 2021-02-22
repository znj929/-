package 线程池;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 119503
 */
public class ThreadPoolDemo {

    static class FixedThreadPoolDemo{
        /**
         * 固定线程的线程池
         * 10个线程
         */
        static ExecutorService executorService = Executors.newFixedThreadPool(10);

        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "-> 执行");
                });
            }
            // 关闭线程池
            executorService.shutdown();
        }
    }


    /**
     * Executors.newCachedThreadPool()
     * corePoolSize:0
     * maximumPoolSize:Integer.MAX_VALUE
     * keepAliveTime:60L
     * TimeUnit:TimeUnit.SECONDS
     */
    static class CachedThreadPoolDemo{
        /**
         * 伸缩性，60s后回收
         */
        static ExecutorService executorService = Executors.newCachedThreadPool();

        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                executorService.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"执行");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }



}
