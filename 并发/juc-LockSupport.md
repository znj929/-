#### LockSupport 类
用来创建锁和其他同步类的基本线程阻塞;

LockSupport类的核心方法:
1. park(): 用来阻塞当前调用线程
2. unpark(): 用于唤醒指定线程

https://baijiahao.baidu.com/s?id=1666548481761194849&wfr=spider&for=pc

#### 基于 JDK1.8 分析
1. park() 方法
````
public static void park(Object blocker) {
    Thread t = Thread.currentThread();
    setBlocker(t, blocker);
    UNSAFE.park(false, 0L);
    setBlocker(t, null);
}
核心的方法: UNSAFE.park(false, 0L)。

每个线程都有一个 Parker 实例，内部维护了一个由 volatile 修饰的变量 _counter 字段,用来记录所谓的“许可”。
1. 当调用 park 时，先尝试能否直接拿到“许可”，也就是 _counter>0 时，如果成功，则把 _counter 设置为0，并返回。
2. 如果不成功，则构造一个ThreadBlockInVM,然后检查 _counter 是不是>0,如果是则把 _counter 设置为0, unlock mutex 并返回。
3. 否则(没有成功拿到“许可”,第二次检查 _counter没有大于0 ), 的情况下，再判断等待的时间，然后再调用 pthread_cond_wait 函数等待，如果等待返回,则把 _counter 设置为0,unlock mutex 并返回

````
2. unpark() 方法
````
public static void unpark(Thread thread) {
    if (thread != null)
        UNSAFE.unpark(thread);
}
核心的方法: UNSAFE.unpark(thread);
给线程设置一个生产许可证。

1. 直接设置_counter为1，再unlock mutext返回。如果 _counter 之前的值是0，则还要调用 pthread_cond_signal 唤醒在park中等待的线程

````
#### LockSupport 使用

