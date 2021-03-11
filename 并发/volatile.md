### 谈谈你对Java内存模型的理解

把整个方法运行在java内存模型中的步骤抽象成6个步骤，read、load、use、assign、store、write
更简单来说可以这么理解：read操作是将主存中的数据读取到cpu的cache中,write将cpu cache中数据写回到主存中 load操作将cpu的cache中数据加载到jvm的寄存器中,store将jvm寄存器中数据存储到cpu的cache中 assign:接收到赋值指令时给工作内存变量赋值

“https://www.processon.com/view/link/5ffdb63a1e0853437c3fd44c”


### 你知道Java内存模型中的原子性、有序性、可见性是什么吗？

#### Java的内存模型是分为主内存和线程的工作内存两部分进行工作的，工作内存从主内存read数据，load到工作内存中，线程对数据进行use，然后将数据assign到工作内存，从工作内存store处理过的数据，最后将新数据write进主内存。 
默认的这种情况下，不同线程并发对同一个数据进行操作是没有可见性的的，会发生数据错误的情况。 如果能保证原子性就是在线程1修改了数据后，线程2立马能将自己工作线程的数据刷新进行后续操作。 

原子性是指：线程1对数据读取并操作是一个原子过程，线程1在处理过程中，其他线程不能对数据进行操作 

java虚拟机会对写好的代码进行指令重排，在多线程情况就可能会因为代码顺序调整出现问题。比如线程1判断flag准备数据，线程2判断flag确定是否准备好，依赖准备好的数据进行业务操作。
如果指令重排序就可能导致线程1还未准备好数据，线程2就开始执行业务操作，发生错误。有序性是指：通过一定手段，保证不会对代码进行指令重排序


### 聊聊 volatile 关键字的原理

理解 volatile 之前要先了解java的内存模型运行原理（详细见：谈谈你对Java内存模型的理解）。
可以看下这段代码

``` 代码块
public class volatile示例.VolatileT extends Thread {
    int a = 0;
    boolean flag = false;

    public void run(){
        while (!flag) {
            a++;
            System.out.println(a);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        volatile示例.VolatileT volatileT = new volatile示例.VolatileT();
        volatileT.start();
        Thread.sleep(2000);
        volatileT.flag = true;
        System.out.println(+ volatileT.a);
    }
}
```
执行上面代码块发现，控制台已经有了输出，但是程序并没有退出，volatileT.flag = true;并没有起到作用

为什么会出现这样的问题，了解了我们的java内存模型运行原理就知道，每次线程是从自己的“工作内存”中取的值！
这也是JVM为了提供性能而做的优化。
那我们如何能让线程每次判断flag的时候都强制它去主内存中取值呢。这就是 volatile 关键字的作用。

###  指令重排以及happens-before原则是什么？

（有点像背书似的）
happens-before原则:
1. 程序次序规则：一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作

2. 锁定规则：一个unLock操作先行发生于后面对同一个锁的lock操作，比如说在代码里有先对一个lock.lock()，lock.unlock()，lock.lock()

3. volatile变量规则：对一个volatile变量的写操作先行发生于后面对这个volatile变量的读操作，volatile变量写，再是读，必须保证是先写，再读

4. 传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C

5. 线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作，thread.start()，thread.interrupt()

6. 线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生

7. 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行

8. 对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始

###  volatile底层是如何基于内存屏障保证可见性和有序性的?

volatile不能保证原子性，只是在一些很极端的场景下原子性操作。

内存屏障避免了指令重排。

可见性：lock前缀指令 + MESI缓存一致性协议
对volatile修饰的变量，执行写操作的话，JVM会发送一条lock前缀指令给CPU，CPU在计算完之后会立即将这个值写回主内存，同时因为有MESI缓存一致性协议，所以各个CPU都会对总线进行嗅探，自己本地缓存中的数据是否被别人修改
如果发现别人修改了某个缓存的数据，那么CPU就会将自己本地缓存的数据过期掉，然后这个CPU上执行的线程在读取那个变量的时候，就会从主内存重新加载最新的数据了

MESI缓存一致性协议解释：
````
实现MESI协议，有两个配套的专业机制要给大家说一下：flush处理器缓存、refresh处理器缓存。
flush处理器缓存，他的意思就是把自己更新的值刷新到高速缓存里去（或者是主内存），因为必须要刷到高速缓存（或者是主内存）里，才有可能在后续通过一些特殊的机制让其他的处理器从自己的高速缓存（或者是主内存）里读取到更新的值
除了flush以外，他还会发送一个消息到总线（bus），通知其他处理器，某个变量的值被他给修改了
refresh处理器缓存，他的意思就是说，处理器中的线程在读取一个变量的值的时候，如果发现其他处理器的线程更新了变量的值，必须从其他处理器的高速缓存（或者是主内存）里，读取这个最新的值，更新到自己的高速缓存中
所以说，为了保证可见性，在底层是通过MESI协议、flush处理器缓存和refresh处理器缓存，这一整套机制来保障的
要记住，flush和refresh，这两个操作，flush是强制刷新数据到高速缓存（主内存），不要仅仅停留在写缓冲器里面；refresh，是从总线嗅探发现某个变量被修改，必须强制从其他处理器的高速缓存（或者主内存）加载变量的最新值到自己的高速缓存里去
内存屏障的使用，在底层硬件级别的原理，其实就是在执行flush和refresh，MESI协议是如何与内存屏障搭配使用的（flush、refresh）
````
  

