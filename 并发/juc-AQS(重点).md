#### AQS AbstractQueuedSynchronizer 
AQS框架提供了一套通用的机制来管理同步状态（synchronization state）、阻塞/唤醒线程、管理等待队列。它所有的操作都围绕着——同步状态 state 来展开的。

典型的模板方法设计模式：父类（AQS框架）定义好骨架和内部操作细节，具体规则由子类去实现。
#### AQS 解决了什么问题？
1. 资源是可以被同时访问？还是在同一时间只能被一个线程访问？（共享/独占功能）
2. 访问资源的线程如何进行并发管理？（等待队列）
3. 如果线程等不及资源了，如何从等待队列退出？（超时/中断）

#### AQS 常见的同步器？
1. ReentrantLock: 独占锁。State为0表示锁可用；为1表示被占用；为N表示重入的次数
2. CountDownLatch: 倒数计数器。State为0表示计数器归零,所有线程都可以访问资源; 为N表示计数器未归零,所有线程都需要阻塞.
3. Semaphore: 信号量或者令牌。State≤0表示没有令牌可用，所有线程都需要阻塞；大于0表示由令牌可用. 线程每获取一个令牌，State减1，线程没释放一个令牌，State加1。
4. ReentrantReadWriteLock: 共享的读锁和独占的写锁。state逻辑上被分成两个16位的unsigned short，分别记录读锁被多少线程使用和写锁被重入的次数。

#### AQS 如何定义资源是否可以被访问？ 
AQS 暴露一些API来让使用者自己解决 资源是否可以被访问的问题。
1. tryAcquire: 排它获取（资源数）,该方法应该查询对象的状态是否允许以独占模式获取它，如果允许则获取它.
2. tryRelease: 排它释放（资源数）,这个方法总是由执行释放的线程调用
3. tryAcquireShared: 共享获取（资源数）,尝试以共享模式获取。该方法应该查询对象的状态是否允许在共享模式下获取它，如果允许则获取它。
4. tryReleaseShared: 尝试释放资源,这个方法总是由执行释放的线程调用。
5. isHeldExclusively: 是否排它状态,判断当前线程是否获取到了同步状态（也就是锁）,主要用于判断当前调用singal()方法的线程，是否在同步队列中，且已经获取了同步状态。

#### AQS 同步状态的管理？
AQS 使用单个int（32位）private volatile int state; 来保存同步状态。暴露出 getState,setState和compareAndSetState三个操作来读取和更新这个状态。
````
/**
 * 同步状态.
 */
private volatile int state;

protected final int getState() {
    return state;
}

protected final void setState(int newState) {
    state = newState;
}
/**
 * 以原子的方式更新同步状态.
 * 利用Unsafe类实现
 */
protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
}
````
#### AQS 阻塞/唤醒线程的操作？
在JDK1.5之前，除了内置的监视器机制外，没有其它方法可以安全且便捷得阻塞和唤醒当前线程。
JDK1.5以后，java.util.concurrent.locks包提供了LockSupport类来作为线程阻塞和唤醒的工具。


#### AQS 线程等待队列的管理?
AQS 整个框架的关键其实就是如何在并发状态下管理被阻塞的线程。
等待队列是严格的FIFO队列，是Craig，Landin和Hagersten锁（CLH锁）的一种变种，采用双向链表实现，因此也叫CLH队列。












