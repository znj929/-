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




















