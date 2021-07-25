#### Condition 接口
JUC在实现 Condition 对象时，其实是通过实现AQS框架，来实现了一个 Condition 等待队列
Condition可以看做是Obejct类的wait()、notify()、 notifyAll()方法的替代品，与Lock配合使用。
当线程执行 condition 对象的 await 方法时，当前线程会立即释放锁，并进入对象的等待区，等待其它线程唤醒或中断。

````
最佳实践 Guarded Suspension 模式
````











