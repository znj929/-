package jucLocks示例;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: znj
 * @Date: 2021/7/23 0023 11:23
 */
public class TestLockInterruptibly {
    public void test() throws Exception {
        final Lock lock = new ReentrantLock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted.");
                }
            }
        }, "child thread -1");

        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000000);
    }

    public static void main(String[] args) throws Exception {
        new TestLockInterruptibly().test();
    }
}
