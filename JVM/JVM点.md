#### java 有哪些类加载器？
1. 启动类加载器
    BootStrap ClassLoader
    加载java安装目录下的“lib”目录中的核心类库
2. 扩展类加载器
    Extension ClassLoader
    加载java安装目录下的“lib\ext”目录中的核心类库
3. 应用程序加载器
    Application ClassLoader
   加载"ClassPath" 环境变量所指定的路径中的类，可以理解为加载自己写好的java代码。
4. 自定义类加载器
   
##### 理解 双亲委派机制
类加载顺序 4->3->2->1 一层一层往上推，然后由1开始往下找 1-》2-》3-》4


#### 理解 jvm内存分区流程

https://www.processon.com/view/link/606070da7d9c08555e657809


#### JVM的垃圾回收机制是用来干嘛的？为什么要垃圾回收？
##### 什么时候需要垃圾回收：
https://www.processon.com/view/link/6060811e5653bb2225f580ec
##### 为什么要垃圾回收？
耗内存

#### JVM分代模型：年轻代、老年代、永久代

1. 年轻代: 基本方法执行完，对象实例短时间内就会回收
2. 老年代: 对象实例存活时间较长的对象实例
3. 永久代: 方法区的类信息等 会存放在永久代

##### 方法区内会不会进行垃圾回收
会的
1. 首先该类的所有实例对象都已经从java堆内存里被回收
2. 其次加载这个类的Classloader已经被回收
3. 最后该类的Class对象没有任何引用

#### JVM内存核心参数
1. -Xms:java堆内存大小
2. -Xmx:java堆内存的最大大小
3. -Xmn:java堆内存中新生代大小，扣除新生代剩下的就是老年代的内存大小
4. -XX:PermSize:永久代大小 1.8替换成了 -XX:MetaspaceSize
5. -XX:MaxPermSize:永久代最大大小 1.8替换成了 -XX:MaxMetaspaceSize
6. -Xss:每个线程的栈内存大小 每个线程都要自己的虚拟机栈

#### 强引用 软引用 弱引用

1. 强引用：垃圾回收不会回收这个对象
   如：
````
public class test{
    public static A a = new A();
}
````

2. 软引用：垃圾回收后发现内存空间还是不够，就会回收这个软引用对象。
   如：
   
````
public class test{
    public static SoftReference<A> a1 = new SoftReference<>(new A());
}
````
3. 弱引用： 只要发生垃圾回收，就会把这个对象回收掉。
    如：
````
public class test{
    /**
     * 弱引用
     */
    public static WeakReference<A> a2 = new WeakReference<>(new A());
}
````

#### JVM垃圾回收算法

https://www.processon.com/view/link/606344177d9c0805fd376530

#### JVM中都有哪些常见的垃圾回收器，各自的特点是什么？
Serial和Serial Old垃圾回收器，
ParNew和CMS垃圾回收器，
G1垃圾回收器

#### Stop the World”问题分析

##### 为什么会产生 stop the world 问题？
````
在垃圾回收GC的时候，JVM不能再创建新的对象，JVM会在后台直接进入一个“stop the world”状态。也就是说它会直接停止我们的java所有的工作线程，然后让垃圾回收线程进行垃圾回收工作。
垃圾回收完毕后，就继续恢复java程序。
````
##### stop the world 会对系统造成什么影响？
````
由上面的问题可以看出，stop the world  会造成系统的卡顿，卡顿时间长由你们的GC时间长决定。
所以说，无论是新生代GC还是老年代GC，都尽量不要让频率过高，也避免持续时间过长，避免影响系统正常运行，这也是使用JVM过程中一个最需要优化的地方，也是最大的一个痛点。
````
##### 不同的垃圾回收器的不同的影响

Serial 垃圾回收器：就是用一个线程进行垃圾回收
ParNew 垃圾回收器：针对服务器一般都是多核CPU做了优化，他是支持多线程个垃圾回收的
CMS 垃圾回收器：专门负责老年代的垃圾回收，也是基于多线程的

#### JVM的年轻代垃圾回收器 ParNew 是如何工作的？
复制算法
````
设置 “-XX:+UseParNewGC” 选项，加入这个选项，jvm启动之后就会使用 ParNew 垃圾回收器回收了。
ParNew 垃圾回收器会使用多线程来进行垃圾回收。
通过设置 “-XX:ParallelGCThreads” 参数，可以调节ParNew的垃圾回收线程数量。一般不要自己设置，默认会根据计算机CPU数量来决定。如4核CPU 默认就是4个线程数量。
````

#### CMS 垃圾回收器
标记清理算法
先通过追踪 GC Roots 的方法，看看各个对象是否被 GC Roots 给引用，如果是，那就是存活对象，否则就是垃圾对象。将垃圾对象标记出来，然后一次性把垃圾对象回收掉。

````
CMS 垃圾回收器采取的是 垃圾回收线程和系统工作线程尽量同时执行的模式进行
````
图解：
https://www.processon.com/view/link/6071afec079129117f22f9db

##### CMS 垃圾回收器 回收引发的问题：
1. 并发回收垃圾导致 CPU 资源紧张
   ````
   CMS 默认启用的垃圾回收线程数量是 （CPU核数+3）/4.
   假设 2核CPU （2+3）/4=1 个垃圾回收线程去占用。
   ````
2. Concurrent Mode Failure 问题
   ````
   一般为了保证CMS 垃圾回收期间，还有一定的内存让一些对象可以进入老年代，一般会预留一些空间。
   如果CMS 垃圾回收期间，系统程序要放入老年代的对象大于可用内存空间。这个时候就会发生 Concurrent Mode Failure ，就是说内存不够了。
   这个时候就会自动用 “Serial Old” 垃圾回收器 代替CMS，就是直接让系统处于 “Stop The World” 状态，重新进行长时间的 GC Roots追踪，
   标记出来全部垃圾对象，不允许新对象产生，然后再一次性把垃圾对象回收掉，完事再恢复系统线程。
   ````
3. 内存碎片问题
    ````
    老年代使用CMS 标记清理算法回收掉对象后，会导致大量的内存碎片产生。如果内存碎片太多，会导致后续对象进入老年代找不到可用的连续空间，然后触发Full GC.
    CMS 可用通过设置一个参数：“-XX:UseCMSCompactAtFullCollection”,这个参数的含义就是 在Full GC之后要再进行一次 “Stop The World”,
    停止工作线程，然后进行碎片整理，就是把存活对象挪到一起，空出连续的内存空间，避免内存碎片。
    还有一个参数：“-XX:CMSFullGCsBeforeCompaction” ,这个参数可以设置执行多少次 Full GC 之后再执行一次内存碎片整理的工作，默认是0每次都要。
    
    ````








































