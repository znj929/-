#### ReadWriteLock 接口
提供了获取读锁和写锁的方法。
读锁用于只读操作，写锁用于写入操作。读锁可以由多个线程同时保持，而写锁是独占的，只能由一个线程获取。

读写锁比较适用于以下情形：

1. 高频次的读操作，相对较低频次的写操作；
2. 读操作所用时间不会太短。（否则读写锁本身的复杂实现所带来的开销会成为主要消耗成本）

#### ReetrantReadWriteLock 的特性？
1. 非公平模式（默认）
2. 公平模式
3. 可重入
4. 支持锁降级
````
/**锁降级**/
import java.util.concurrent.locks.ReentrantReadWriteLock;
public class Test2 {
    public static void main(String[] args) {
        ReentrantReadWriteLock rtLock = new ReentrantReadWriteLock();  
        rtLock.writeLock().lock();  
        System.out.println("writeLock");  
        rtLock.readLock().lock();  
        System.out.println("get read lock");  
    }
}
````
5. 读写锁互斥
