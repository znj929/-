#### dubbo 的工作原理？注册中心挂了可以继续通信吗？说说一次 rpc 请求的流程？

https://www.processon.com/view/link/605446fe07912967c5023174

注册中心挂了可以继续通信,因为刚开始初始化的时候，消费者会将提供者的地址等信息拉取到本地缓存，
所以注册中心挂了可以继续通信。

#### dubbo 支持哪些通信协议？支持哪些序列化协议？
通信协议：

1. dubbo 协议
   默认就是走 dubbo 协议，单一长连接，进行的是 NIO 异步通信，
   基于 hessian 作为序列化协议。使用的场景是：传输数据量小（每次请求在 100kb 以内），但是并发量很高。
2. rmi 协议
   Java 二进制序列化，多个短连接，适合消费者和提供者数量差不多的情况，适用于文件的传输，一般较少用。
3. hessian 协议
   hessian 序列化协议，多个短连接，适用于提供者数量比消费者数量还多的情况，适用于文件的传输，一般较少用。
4. http 协议
   json 序列化。
5. webservice
   SOAP 文本序列化。
   
dubbo 支持的序列化协议
dubbo 支持 hession、Java 二进制序列化、json、SOAP 文本序列化多种序列化协议。但是 hessian 是其默认的序列化协议。

#### dubbo 负载均衡策略和集群容错策略都有哪些？动态代理策略呢？
##### dubbo 负载均衡策略：
1. random loadbalance
   默认情况下，dubbo 是 random load balance ，
   即随机调用实现负载均衡，可以对 provider 不同实例设置不同的权重，会按照权重来负载均衡，权重越大分配流量越高，一般就用这个默认的就可以了。
2. roundrobin loadbalance
    平均
3. leastactive loadbalance
    自动感知
4. consistanthash loadbalance
   一致性 Hash 算法，相同参数的请求一定分发到一个 provider 上去，
   provider 挂掉的时候，会基于虚拟节点均匀分配剩余的流量，抖动不会太大。
   如果你需要的不是随机负载均衡，是要一类请求都到一个节点，那就走这个一致性 Hash 策略。
   
##### dubbo 集群容错策略
1. failover cluster 模式
   
   失败自动切换，自动重试其他机器，默认就是这个，常见于读操作。（失败重试其它机器）
   配置方式：
   ````
   <dubbo:service retries="2" />
   或者
   <dubbo:reference retries="2" />
   或者
   <dubbo:reference>
   <dubbo:method name="findFoo" retries="2" />
   ````
2. failfast cluster 模式
   
   一次调用失败就立即失败，常见于非幂等性的写操作，比如新增一条记录（调用失败就立即失败）
   
3. failsafe cluster 模式
   
   出现异常时忽略掉，常用于不重要的接口调用，比如记录日志。
   配置方式：
   ````
   <dubbo:service cluster="failsafe" />
   或者
   <dubbo:reference cluster="failsafe" />
   ````
4. failback cluster 模式
   
   失败了后台自动记录请求，然后定时重发，比较适合于写消息队列这种。
5. forking cluster 模式
   
   并行调用多个 provider，只要一个成功就立即返回。常用于实时性要求比较高的读操作，但是会浪费更多的服务资源，可通过 forks="2" 来设置最大并行数。
6. broadcacst cluster
   
   逐个调用所有的 provider。任何一个 provider 出错则报错（从2.1.0 版本开始支持）。通常用于通知所有提供者更新缓存或日志等本地资源信息。

##### dubbo动态代理策略

默认使用 javassist 动态字节码生成，创建代理类。但是可以通过 spi 扩展机制配置自己的动态代理策略。

#### 如何基于 dubbo 进行服务治理、服务降级、失败重试以及超时重试？

1. 调用链路自动生成
2. 服务访问压力以及时长统计

#### 设计一个类似dubbo的rpc框架?






























