#### java.util.concurrent.locks 包的结构划分

#### java.util.concurrent.locks.Lock 接口 
接口提供了限时锁等待，锁中断，锁尝试 等功能
1. lock() 方法类似 synchronized 关键字加锁，如果当前锁不可用，当前线程在获取锁之前一直会处于休眠状态。
   ````
    . 如果该锁没有被另一个线程保持，则获取该锁并立即返回，将锁的保持计数设置为 1。
    . 如果当前线程已经保持该锁，则将保持计数加 1(重入)，并且该方法立即返回。
    . 如果该锁被另一个线程保持，则出于线程调度的目的，禁用当前线程，并且在获得锁之前，该线程将直处于休眠状态，此时锁保持计数被设置为 1。
   
    ·指定者： 接口 Lock 中的 lock
   ````
2. lockInterruptibly() 方法，Interruptibly 中断的意思，如果当前锁不可用，那么正在等待的线程是可以被中断的，比 synchronized 更加灵活。
   ````
    . 如果当前线程未被中断，则获取锁。
    . 如果该锁没有被另一个线程保持，则获取该锁并立即返回，将锁的保持计数设置为 1。 
    . 如果当前线程已经保持此锁，则将保持计数加 1，并且该方法立即返回。
    和 lock() 的区别：
        . 如果锁被另一个线程保持，则出于线程调度目的，禁用当前线程，并且在发生以下两种情况之一以前，该线程将一直处于休眠状态：
            1.锁由当前线程获得；2.其他某个线程中断当前线程。 
        . 如果当前线程获得该锁，则将锁保持计数设置为 1。 
        . 如果当前线程： 在进入此方法时已经设置了该线程的中断状态；或者 在等待获取锁的同时被中断。则抛出 InterruptedException，并且清除当前线程的已中断状态。
        . 此方法是一个显式中断点，所以要优先考虑响应中断，而不是响应锁的普通获取或重入获取。
    
   
   . 指定者： 接口 Lock 中的 lockInterruptibly
   . 抛出：   InterruptedException   如果当前线程已中断。
   ````
3. tryLock() 方法： 调用时锁未被另一个线程保持的情况下，才获取该锁。
   ````
    . 如果该锁没有被另外线程持有，并且立即返回 true 值，则将锁的保持计数设置为 1.
    . 如果当前线程已经保持此锁，则将保持计数加 1，该方法将返回 true。
    . 如果锁被另一个线程保持，则此方法将立即返回 false 值。
   
    指定者： 接口 Lock 中的  tryLock
    返回： 如果锁是自由的并且被当前线程获取，或者当前线程已经保持该锁，则返回 true；否则返回 false
   ````

#### lock() ,tryLock() 和 lockInterruptibly() 这两个方法的区别？
1. lock(), 拿不到lock就不罢休，不然线程就一直block。。
2. tryLock()，马上返回，拿到lock就返回true，不然返回false。带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false。
3. lockInterruptibly() 
   ````
   先说说线程的打扰机制，每个线程都有一个 打扰 标志。这里分两种情况，
    1. 线程在sleep或wait,join， 此时如果别的进程调用此进程的 interrupt（）方法，此线程会被唤醒并被要求处理InterruptedException；(thread在做IO操作时也可能有类似行为，见java thread api)
    2. 此线程在运行中， 则不会收到提醒。但是 此线程的 “打扰标志”会被设置， 可以通过isInterrupted()查看并 作出处理。
   
   lockInterruptibly() 和上面的第一种情况是一样的， 线程在请求lock并被阻塞时，如果被interrupt，则“此线程会被唤醒并被要求处理InterruptedException”。
   并且如果线程已经被interrupt，再使用lockInterruptibly的时候，此线程也会被要求处理interruptedException
   
   
   ````










