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
public class VolatileT extends Thread {
    int a = 0;
    boolean flag = false;

    public void run(){
        while (!flag) {
            a++;
            System.out.println(a);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        VolatileT volatileT = new VolatileT();
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























































