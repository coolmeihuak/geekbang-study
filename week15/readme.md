## JVM

### 相关简介

#### JDK、JRE、JVM
* Java技术体系
	- Java程序设计语言
	- 各种硬件平台上的Java虚拟机实现
	- Class文件格式
	- Java类库API
	- 来自商业机构和开源社区的第三方Java类库
* 可以把Java程序设计语言、JVM、Java类库API合并称为JDK，JDK是指Java程序开发的最小环境
* JRE是Java程序运行的最小环境，包含JVM、Java类库API

#### 各类虚拟机
* Sun Classic/Exact VM：世界上第一款商用Java虚拟机
* HotSpot VM：Sun/OracleJDK和OpenJDK中的默认Java虚拟机，也是目前使用范围最广的Java虚拟机
* Mobile/Embedded VM：Java ME中的Java虚拟机
* BEA JRockit/IBM J9 VM：JRockit内部不包含解释器实现，全部代码都靠即时编译器编译后执行
* BEA Liquid VM/Azul VM：Liquid VM不需要操作系统的支持，或者说它自己本身实现了一个专用操作系统的必要功能

### 自动内存管理
#### 运行时数据区域
* 程序计数器：当前线程所执行的字节码的行号指示器
* Java虚拟机栈
	- 线程私有
	- 每个方法被执行的时候，Java虚拟机都会同步创建一个栈帧
	- 栈帧由操作数栈、 局部变量数组以及一个 Class 引用组成
* 本地方法栈
	- 虚拟机栈为虚拟机执行Java方法（也就是字节码）服务
	- 本地方法栈则是为虚拟机使用到的本地（Native） 方法服务
* Java 堆
	- Java堆是被所有线程共享的一块内存区域
	- 可以简单划分为：新生代、幸存代、老年代
	- Java堆可以处于物理上不连续的内存空间中，但在逻辑上它应该 被视为连续的
	- 通过参数-Xmx和-Xms设定
* 方法区
	- 用于存储已被虚拟机加载 的类型信息、常量、静态变量、即时编译器编译后的代码缓存等数据
	- 它却有一个别名叫作“非堆”（Non-Heap），目的是与Java堆区分开来
	- 移除永久代的工作从JDK1.7就开始了。JDK1.7中，存储在永久代的部分数据就已经转移到了Java Heap或者是 Native Heap
	- 譬如符号引用(Symbols)转移到了native heap；字面量(interned strings)转移到了java heap；类的静态变量(class statics)转移到了java heap
	- 在jdk8以后，使用原空间替代了永久代
* 直接内存
	- 直接内存（Direct Memory）并不是虚拟机运行时数据区的一部分，也不是《Java虚拟机规范》中定义的内存区域
	- 能在一些场景中显著提高性能，因为避免了在Java堆和Native堆中来回复制数据
	- 不能在程序中对直接内存操作
	
#### 垃圾收集
* 之所以会有不同类型垃圾收集器，是基于对象分代的假设
* 如何判断对象可回收：引用计数法与可达性分析算法
	- 1
	- 2
* 垃圾收集算法（基于对象分代的思想）
	- 分代收集理论：
	- 标记-清除算法：
	- 标记-复制算法：
	- 标记-整理算法：
* 经典垃圾收集器
	- Serial收集器
	- ParNew收集器
	- Parallel Scavenge收集器
	- Serial Old收集器
	- Parallel Old收集器
	- CMS收集器
	- G1收集器
* 低延迟垃圾收集器
	- Shenandoah收集器
	- ZGC收集器
* 如何选择合适的垃圾收集器
	- 目前绝大部分 Java 应用系统，堆内存并不大比如 2G-4G 以内，而且对 10ms 这种低延迟的 GC 暂停不敏感，也就是说处理一个业务步骤，大概几百毫秒都是可以接受的，GC 暂停 100ms 还是 10ms 没多大区别。另一方面，系统的吞吐量反而往往是我们追求的重点，这时候就需要考虑采用并行 GC
	- 如果堆内存再大一些，可以考虑 G1 GC。如果内存非常大（比如超过 16G，甚至是 64G、128G），或者是对延迟非常敏感 （比如高频量化交易系统），就需要考虑使用本节提到的新 GC（ZGC/Shenandoah）
* 内存分配与回收策略
	- 对象优先在Eden分配：当Eden区没有足够空间进行分配时，虚拟机将发起 一次Minor GC
	- 大对象直接进入老年代：大对象对虚拟机的内存分配来说 就是一个不折不扣的坏消息，比遇到一个大对象更加坏的消息就是遇到一群“朝生夕灭”的“短命大对 象”
	- 长期存活的对象将进入老年代：虚拟机给每个对象定义了一个对 象年龄（Age）计数器，存储在对象头中；对象在Survivor区中每熬过一次Minor GC，年龄就增加1岁，当它的年龄增加到一定程度（默认为15），就会被晋升到老年代中
	- 动态对象年龄判定：如果在Survivor空间中相同年龄所有对象大小的总和大于 Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无须等到-XX： MaxTenuringThreshold中要求的年龄
	- 空间分配担保：在发生Minor GC之前，虚拟机必须先检查老年代最大可用的连续空间是否大于新生代所有对象总 空间，如果这个条件成立，那这一次Minor GC可以确保是安全的；

#### 虚拟机性能监控、故障处理工具
* 基础故障处理工具
	- jps：虚拟机进程状况工具
	- jstat：虚拟机统计信息监视工具
	- jinfo：Java配置信息工具
	- jmap：Java内存映像工具
	- jhat：虚拟机堆转储快照分析工具
	- jstack：Java堆栈跟踪工具
* 可视化故障处理工具
	- JHSDB：基于服务性代理的调试工具
	- JConsole：Java监视与管理控制台
	- VisualVM：多合-故障处理工具
	- Java　Mission　Control：可持续在线的监控工具
* 在线分析
	- GCEasy：GC 日志解读与分析
	- fastthread：JVM 线程堆栈数据分析

### 虚拟机执行子系统
#### 类文件结构
#### 虚拟机类加载机制
* 类加载的时机
	- 当虚拟机启动时，初始化用户指定的主类，就是启动执行的 main 方法所在的类；
	- 当遇到用以新建目标类实例的 new 指令时，初始化 new 指令的目标类，就是 new 一 个类的时候要初始化；
	- 当遇到调用静态方法的指令时，初始化该静态方法所在的类；
	- 当遇到访问静态字段的指令时，初始化该静态字段所在的类；
	- 子类的初始化会触发父类的初始化；
	- 如果一个接口定义了 default 方法，那么直接实现或者间接实现该接口的类的初始化， 会触发该接口的初始化；
	- 使用反射 API 对某个类进行反射调用时，初始化这个类，其实跟前面一样，反射调用要 么是已经有实例了，要么是静态方法，都需要初始化；
	- 当初次调用 MethodHandle 实例时，初始化该 MethodHandle 指向的方法所在的类
* 类加载的过程
	- 加载（Loading）：找 Class 文件
	- 验证（Verification）：验证格式、依赖
	- 准备（Preparation）：静态字段、方法表
	- 解析（Resolution）：符号解析为引用
	- 初始化（Initialization）：构造器、静态变 量赋值、静态代码块


## NIO

#### 五种IO模型
* 阻塞式 IO、BIO
	- 一般通过在 while(true) 循环中服务端会 调用 accept() 方法等待接收客户端的连接 的方式监听请求，请求一旦接收到一个连 接请求，就可以建立通信套接字在这个通 信套接字上进行读写操作，此时不能再接 收其他客户端连接请求，只能等待同当前 连接的客户端的操作执行完成， 不过可以 通过多线程来支持多个客户端的连接
* 非阻塞式 IO
	- 和阻塞 IO 类比，内核会立即返回，返回后 获得足够的 CPU 时间继续做其它的事情
	- 用户进程第一个阶段不是阻塞的,需要不断 的主动询问 kernel 数据好了没有
	- 第二个 阶段依然总是阻塞的
* IO 多路复用(IO multiplexing)
	- 也称事件 驱动 IO(event-driven IO)，就是在单个线 程里同时监控多个套接字，通过 select 或 poll 轮询所负责的所有 socket，当某个 socket 有数据到达了，就通知用户进程
	- 同非阻塞 IO 本质一样，不过利用 了新的 select 系统调用，由内核来负责本 来是请求进程该做的轮询操作。看似比非 阻塞 IO 还多了一个系统调用开销，不过 因为可以支持多路 IO，才算提高了效率
	- 进程先是阻塞在 select/poll 上，再是阻塞在读操作的第二个阶段上
* 信号驱动 I/O
	- 在 IO 执行的数据准备阶段，不需要轮询
* 异步式 IO
	- 异步 IO 真正实现了 IO 全流程的非阻塞。 用户进程发出系统调用后立即返回，内核 等待数据准备完成，然后将数据拷贝到用 户进程缓冲区，然后发送信号告诉用户进 程 IO 操作执行完毕

#### Netty
* Netty 特性
	- 高吞吐
	- 低延迟
	- 低开销
	- 零拷贝
	- 可扩容
	- 松耦合: 网络和业务逻辑分离
	- 使用方便、可维护性好


## 并发编程
#### 为什么会有多线程
* 多 CPU 核心意味着同时操作系统有更多的 并行计算资源可以使用
* 操作系统以线程作为基本的调度单元

#### 线程状态
* New
* Runnable
* Running
* Non-Runnable
	- Waiting
	- Timed_Waiting
	- Blocked

#### 线程间发信号
* wait & notify
	- Thread.sleep: 释放 CPU，但不释放锁
	- Object#wait : 释放对象锁
	- obj.notify() 唤醒在此对象监视器上等待的单个线程，选择是任意性的
* Thread 的中断与异常处理
	- 线程内部自己处理异常，不溢出到外层（Future 可以封装）
	- 如果线程被 Object.wait, Thread.join和Thread.sleep 三种方法之一阻塞，此时调用该线程的interrupt() 方法，那么该线程将抛出一个 InterruptedException 中断异常（该线程必须事先预备好处理此异常），从 而提早地终结被阻塞状态。如果线程没有被阻塞，这时调用 interrupt() 将不起作用，直到执行到 wait/sleep/join 时，才马上会抛出InterruptedException
	
#### 线程安全
* 多个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件
* Java角度
	- 类是否是线程安全的类
	- 方法是否是线程安全的方法
	- 程序集是否是线程安全的
* 定义的级别
	- 不可变：不变的对象绝对是线程安全的，不需要线程同步，如String、Long、BigInteger
	- 无条件的线程安全：对象自身做了 足够的内部同步，也不需要外部同步，如 Random 、ConcurrentHashMap、Concurrent集合、atomic
	- 有条件的线程安全：对象的部分方法可以无条件安全使用，但是有些方法需要外部同步，需要Collections.synchronized；有条件线程安全的最常见的例子是遍历由 Hashtable 或者 Vector 或者返回的迭代器
	- 非线程安全(线程兼容)：对象本身不提供线程安全机制，但是通过外部同步，可以在并发环境使用， 如ArrayList HashMap
	- 线程对立：即使外部进行了同步调用，也不能保证线程安全，这种情况非常少，如如System.setOut()、System.runFinalizersOnExit()
* 思考
	- 说类是线程安全的只是说当单次调用该类的方法时是线程安全的
	- 方法是线程安全同上
	- 一般来说具有复合操作的程序集都有可能不是线程安全的，即使涉及到的类是线程安全的
	- 同一个对象的线程安全的不同方法被不同线程调用是线程安全的
	- 复合操作不是线程安全可能是因为线程安全类的复合操作，也可能是程序集使用了共享变量而没有做同步机制

#### 到底什么是锁
* synchronized
* wait/notify 可以看做加锁和解锁
* 更自由的锁: Lock
	1. 使用方式灵活可控
	2. 性能开销小
	3. 锁工具包: java.util.concurrent.locks

#### 无锁技术
* Unsafe API - CompareAndSwap：作为乐观锁实现，通过自旋重试保证写入
* Value 的可见性 - volatile 关键字：volatile 保证读写操作都可见（注意不保证原子）

#### 常用线程安全类型
* CopyOnWriteArrayList
* ConcurrentHashMap
	- Java7 分段锁
	- Java 8为进一步提高并发性，摒弃了分段 锁的方案，而是直接使用一个大的数组

#### 加锁需要考虑的问题
1. 粒度
2. 性能
3. 重入
4. 公平
5. 自旋锁（spinlock）
6. 场景: 脱离业务场景谈性能都是耍流氓

#### 线程间协作与通信
1. 线程间共享:
	- static/实例变量（堆内存）
	- Lock
	- synchronized
2. 线程间协作:
	- Thread#join()
	- Object#wait/notify/notifyAll
	- Future/Callable
	- CountdownLatch
	- CyclicBarrier


## Spring 和 ORM 等框架
#### Spring 框架设计
* 支撑性+扩展性：框架不解决具体的业务功能问题，我们可以在框架的基础上添加各种 具体的业务功能、定制特性，从而形成具体的业务应用系统
* 聚合性+约束性：框架是多种技术点的按照一定规则的聚合体。我们采用了某种框架也 就意味着做出了技术选型的取舍。在很多种可能的技术组合里确定了一种具体的实现方式 ，后续的其他工作都会从这些技术出发，也需要遵循这些规则，所以框架本身影响到研发 过程里的方方面面

#### Spring framework 6 大模块
1. Core：Bean/Context/AOP
2. Testing：Mock/TestContext
3. DataAccess: Tx/JDBC/ORM
4. Spring MVC/WebFlux: web
5. Integration: remoting/JMS/WS
6. Languages: Kotlin/Groovy

#### Spring AOP
* IoC-控制反转
	- 也称为DI（Dependency Injection）依赖注入。对象装配思路的改进
	- 可以实现在不修改代码的情况，修改配置文件，即可以运行时替换成注入IB接口另 一实现类C的一个对象实例
* 实现
	- com.sun.proxy.$Proxy
	- EnhancerBySpringCGLIB
	- ...

#### Spring Bean 生命周期
* Bean 的加载过程：构造函数 => 依赖注入 => BeanNameAware => BeanFactoryAware => ApplicationContextAware => BeanPostProcessor前置方法 => InitializingBean => 自定义init方法 => BeanPostProcessor后置方法 => 使用 => DisposableBean => 自定义destroy方法
* 为啥设计的这么复杂：为了兼容各种类型的Bean

#### Spring Boot 的出发点
* Spring 臃肿以后的必然选择
	- 让开发变简单
	- 让配置变简单
	- 让运行变简单
	- 约定大于配置
* 如何做到简化
	1. Spring 本身技术的成熟与完善，各方面第三方组件的成熟集成 
	2. Spring 团队在去 web 容器化等方面的努力 
	3. 基于 MAVEN 与 POM 的 Java 生态体系，整合 POM 模板成为可能 
	4. 避免大量 maven 导入和各种版本冲突

#### Spring Boot 两大核心原理
* 自动化配置：简化配置核心 基于 Configuration，EnableXX，Condition
* spring-boot-starter：脚手架核心 整合各种第三方类库，协同工具

#### ORM-Hibernate/MyBatis
* Hibernate 是一个开源的对象关系映射框架，它对 JDBC 进行了非常轻量级的对象封装，它将 POJO 与数 据库表建立映射关系，是一个全自动的 orm 框架， hibernate 可以自动生成 SQL 语句，自动执行，使得 Java 程序员可以使用面向对象的思维来操纵数据库
* MyBatis 是一款优秀的持久层框架，它支 持定制化 SQL、存储过程以及高级映射。 MyBatis 避免了几乎所有的 JDBC 代码和 手动设置参数以及获取结果集
* MyBatis-半自动化 ORM
* MyBatis 与 Hibernate 的区别与联系
	- Mybatis 优点：原生SQL（XML 语法），直观，对 DBA 友好
	- Hibernate 优点：简单场景不用写 SQL（HQL、Cretiria、SQL）
	- Mybatis 缺点：繁琐，可以用 MyBatis-generator、MyBatis-Plus 之类的插件
	- Hibernate 缺点：对DBA 不友好


## MySQL 数据库和 SQL
#### MySQL 存储
* 独占模式
	- 日志组文件：ib_logfile0 和 ib_logfile1，默认均为5M
	- 表结构文件：*.frm
	- 独占表空间文件：*.ibd
	- 字符集和排序规则文件：db.opt
	- binlog 二进制日志文件：记录主数据库服务器的 DDL 和 DML 操作
	- 二进制日志索引文件：master-bin.index
* 共享模式
	- innodb_file_per_table=OFF
	- 数据都在 ibdata1
#### MySQL 执行流程
* connect => 查询缓存 => 解析器 => 预处理器 => 查询优化器 => 执行计划 => 查询执行引擎 => 存储引擎 => 数据
* 引擎层：undolog => change buffer => redo log => binlog => 提交事务 => 刷redo log 盘 => 刷 binlog

#### SQL 执行顺序
* from => on => join => where => group by => having => select => order by => limit
* 实际上这个过程也并不是绝对这样的，中间 mysql 会有部分的优化以达到最佳的优化效 果，比如在 select 筛选出找到的数据集

#### MySQL 索引原理
* 数据是按页来分块的，当一个数据被用到时， 其附近的数据也通常会马上被使用
* InnoDB 使用 B+ 树实现聚集索引

#### Mysql 优化
* client层优化
  * Connecting
  * Sending query to server
  * combine many small operations into a single large operation、
  * prepare statement
* server层优化
* engine引擎层优化
  * undo log
  * engine层内存缓存
  * 数据磁盘刷新
  * redo log
  * bin log
* 其他
  * innodb_autoinc_lock_mode
  * max_allowed_packet
  * bulk_insert_buffer_size


## 分库分表

#### 单机Mysql的几个问题
* 随着数据量增大，读写并发的增加，系统可用性要求的提升，问题出来了
* 容量有限，难以扩容
* 读写压力，QPS过大，分析类需求影响业务事务
* 可用性不足，宕机问题

#### 主从复制原理
* 核心
	- 主库写binlog
	- 从库relay log
* 大致步骤
	- 主库 master库 写binlog
	- 从库 slave库 拉主库 master库 订阅主库 master库 的binlog
	- binlog dump thread write slave库
	- 从库 I/O thread 写入 relay log
	- sql thread 写入从库数据

#### 主从复制的局限性
* 主从延迟问题
* 应用层需要配合读写分离框架
* 不解决高可用问题

#### 技术演进
* 读写压力
	- 多机集群
	- 主从复制：一台写、多台查、主写复制到从查
* 高可用性
	- 故障转移：主故障后
	- 主从切换：从切换
* 容量问题
	- 数据库拆分，垂直拆分：拆分数据库表
	- 分库分表，水平拆分：拆分数据库表数据、取模
* 拆分带来的问题
	- 分布式事务


## RPC 和微服务
#### 基本概念
* RPC 是远程过程调用（Remote Procedure Call）的缩写形式
* 简单来说，就是“像调用本地方法一样调用远程方法”
* 核心是代理机制
	- 本地代理存根: Stub
	- 本地序列化反序列化
	- 网络通信
	- 远程序列化反序列化
	- 远程服务存根: Skeleton
	- 调用实际业务服务
	- 原路返回服务结果
	- 返回给本地调用方

#### RPC 原理
* 共享：POJO 实体类定义，接口定义
	- 另一种选择：WSDL/WADL/IDL
* 代理可以选择动态代理，或者 AOP 实现
	- C# 直接有远程代理
	- Flex 可以使用动态方法和属性
* 序列化和反序列化的选择
	- 语言原生的序列化，RMI，Remoting
	- 二进制平台无关，Hessian，avro，kyro，fst 等
	- 文本，JSON、XML 等
* 网络传输
	- TCP/SSL/TLS
	- HTTP/HTTPS
* 查找实现类
	- 一般是注册方式，例如 dubbo 默认将接口和实现类配置到 Spring

#### 常见的 RPC 技术
* Corba/RMI/.NET Remoting
* JSON RPC, XML RPC，WebService(Axis2, CXF)
* Hessian, Thrift, Protocol Buffer, gRPC

#### 从 RPC 走向服务化微服务架构
* 多个相同服务如何管理？ 
* 服务的注册发现机制？ 
* 如何负载均衡，路由等集群功能？ 
* 熔断，限流等治理能力。 
* 重试等策略。 
* 高可用、监控、性能等等。



## 分布式缓存

#### 缓存的使用场景
* 静态数据：一般不变，类似于字典表
* 准静态数据：变化频率很低，部门结构设置，全国行政区划数据等
* 中间状态数据：一些计算的可复用中间数据，变量副本，配置中心的本地副本
* 读写比较大：读的频率 >> 写的频率
* 广义上来说，为了加速数据处理，让业务更快访问的临时存放冗余数据，都是缓存
* 狭义上，现在我们一般在分布式系统里把缓存到内存的数据叫做内存缓存
* 对于 数据一致性，性能，成本 的综合衡量，是引入缓存的必须指标
* 如何评价缓存的有效性
	- 读写比：对数据的写操作导致数据变动，意味着维护成本。 N : 1
	- 命中率：命中缓存意味着缓存数据被使用，意味着有价值。 90%+

#### 缓存使用不当导致的问题
* 系统预热导致启动慢
	- 系统不能做到快速应对故障宕机等问题
* 系统内存资源耗尽

#### 本地缓存有什么缺点
* 在多个集群环境同步？当集群规模增大，缓存的读写放大
* 在JVM中长期占用内存？如果是堆内存，总是会影响GC
* 缓存数据的调度处理，影响执行业务的线程，抢资源

#### 缓存常见问题
* 缓存穿透
	- 大量并发查询不存在的KEY，导致都直接将压力透传到数据库
	- 需要注意让缓存能够区分KEY不存在和查询到一个空值
	- ：缓存空值的KEY，这样第一次不存在也会被加载会记录，下次拿到有这个KEY
	- ：Bloom过滤或RoaringBitmap 判断KEY是否存在
	- ：完全以缓存为准，使用 延迟异步加载 的策略2，这样就不会触发更新
* 缓存击穿
	- 某个KEY失效的时候，正好有大量并发请求访问这个KEY
	- ：KEY的更新操作添加全局互斥锁
	- ：完全以缓存为准，使用 延迟异步加载 的策略2，这样就不会触发更新
* 缓存雪崩
	- 当某一时刻发生大规模的缓存失效的情况，会有大量的请求进来直接打到数据库，导致数据 库压力过大升值宕机
	- ：更新策略在时间上做到比较均匀
	- ：使用的热数据尽量分散到不同的机器上
	- ：多台机器做主从复制或者多副本，实现高可用
	- ：实现熔断限流机制，对系统进行负载能力控制

#### Redis
* 5 种基本数据结构
	- 字符串（string）~ 简单来说就是三种：int、string、byte[]
	- 散列（hash）- Map ~ Pojo Class
	- 列表（list）~ java 的 LinkedList
	- 集合（set）~ java 的 set，不重复的 list
	- 有序集合（sorted set）
* 3 种高级数据结构
	- Bitmaps：setbit/getbit/bitop/bitcount/bitpos
	- Hyperloglogs：pfadd/pfcount/pfmerge
	- GEO：geoadd/geohash/geopos/geodist/georadius/georadiusbymember
* Redis 到底是单线程，还是多线程
	- IO线程：redis 6之前（2020年5月），单线程；redis 6之后，多线程，NIO 模型 ==> 主要的性能提升点
	- 内存处理线程：单线程 ==> 高性能的核心
* 六大使用场景
	- 业务数据缓存
	- 业务数据处理
	- 全局一致计数
	- 高效统计计数
	- 发布订阅与 Stream
	- 分布式锁
* Redis 的 Java 客户端
	- Jedis
	- Lettuce
	- Redission


## 分布式消息队列
#### 一种系统间通信方式
* 各个模式的缺点
	- 文件: 明显不方便，不及时
	- Socket：使用麻烦，多数情况下不如 RPC
	- 数据库：不实时，但是经常有人拿数据库来模拟消息队列
	- RPC：调用关系复杂，同步处理，压力大的时候无法缓冲
* 期望的通信方式
	- 可以实现异步的消息通信
	- 可以简化参与各方的复杂依赖关系
	- 可以在请求量很大的时候，缓冲一下 > 类比线程池里的Queue
	- 某些情况下能保障消息的可靠性，甚至顺序
* 消息处理模式
	- 点对点：PTP，Point-To-Point 对应于Queue
	- 发布订阅：PubSub，Publish-Subscribe， 对应于 Topic

#### 开源消息中间件 / 消息队列
* 一代：ActiveMQ/RabbitMQ
* 二代：Kafka/RocketMQ
* 三代：Apache Pulsar

#### Kafka
* 设计目标
	- 以时间复杂度为 O(1) 的方式提供消息持久化能力，即使对 TB 级以上数据也能保证常数时间复 杂度的访问性能
	- 高吞吐率。即使在非常廉价的商用机器上也能做到单机支持每秒 100K 条以上消息的传输
	- 支持 Kafka Server 间的消息分区，及分布式消费，同时保证每个 Partition 内的消息顺序传输
	- 同时支持离线数据处理和实时数据处理
	- Scale out：支持在线水平扩展
* 基本概念
	- Broker：Kafka 集群包含一个或多个服务器，这种服务器被称为 broker
	- Topic：每条发布到 Kafka 集群的消息都有一个类别，这个类别被称为 Topic
	- Partition：Partition 是物理上的概念，每个 Topic 包含一个或多个 Partition
	- Producer：负责发布消息到 Kafka broker
	- Consumer：消息消费者，向 Kafka broker 读取消息的客户端
	- Consumer Group：每个 Consumer 属于一个特定的 Consumer Group
* Topic 特性
	- 通过 partition 增加可扩展性
	- 通过顺序写入达到高吞吐
	- 多副本增加容错性

#### EIP
* 集成领域的两大法宝，就是 RPC 和 Messaging
* EIP 里，所有的处理，都可以看做是
	- 数据从一个输入源头出发
	- 数据在一个管道流动
	- 经过一些处理节点，数据被过滤器处理，增强，或者转换，或者做个业务处理等等
	- 最后，数据输出到一个目的地
