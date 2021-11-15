## 整体上的总结
* 第一次 GC 的时候，Young 区 committed 的大小和整个堆内存 committed 的大小是一样的，说明第一次 GC 之前是没有对象进入 Old 区的。而且第一次 GC 是 Young GC ，说明对象是先进入到 Young 区，然后到一定程度触发 Young GC ，之后 Old 区开始有使用，说明有对象晋升到 Old 区了。但是第一次一定是 Young GC 吗？不一定，目前知道晋升次数设置有关。还有，如果是大对象是直接到 Old 区。
* 堆内存像一个蓄水池，-Xmx、-Xms就像以后可以用的最高水位和开始可以用的最高水位。当它们设置的越大时，GC 次数变少，甚至没有 Full GC，由于 GC 集中了，GC 的时间也变长。当它们设置的越小时，GC 次数变多了，Full GC 也变的多了起来，由于 GC 分散了，GC 的时间也会变短，如果特别小，会触发OOM。
* 把 -Xms 和 -Xmx 设置的一样，有个好处就是当前可用的“蓄水池”一上来就很高，避免提升“蓄水池”导致的系统消耗
* 在调试的时候发现堆内存一直在变，这才知道JVM是默认把堆内存大小自动调整打开的。
* 在调试的时候可以加上 -XX:+PrintGCDateStamps 参数，这样可以看到 GC 发生的时间。


## 各个 GC 策略的总结
* 本机压测配置
  * 我用的JDK8，它的默认 GC 策略是 +UseParallelGC，并行收集器，是Young(Parallel Scavenge)和Old(Parallel Old)的组合。
  * 我的电脑是6核，在使用 wrk 工具的时候，用了6*3=18个线程模拟并发压测的。
* 压测用例
  * 用18个线程，400次请求，30秒时间压测不同的 GC 策略和不同的堆大小配置
  * GC 策略：+UseSerialGC、+UseConcMarkSweepGC、+UseParallelGC、+UseG1GC
  * 堆大小：256M、512M、1G、4G
  * 总共16次测试
* 比较参数(使用 gceasy.io 评估，也使用它提供的分析比较)
  * 平均 GC 时间
  * 最大 GC 时间
  * GC 次数
  * 处理请求次数
* 测试过程出现：unable to create thread: Too many open files
  * 在用户目录下的.bash_profile中的末尾加入ulimit -n 10240
  * 保存后再执行 source ~/.bash_profile 即可
  * 报错：ulimit:124: setrlimit failed: invalid argument
  * sysctl -w kern.maxfiles=65536
  * sysctl -w kern.maxfilesperproc=65536

|-Xmx|参数|+UseSerialGC|+UseConcMarkSweepGC|+UseParallelGC|+UseG1GC|
|:--|:--|:--|:--|:--|:--|
|256M|Minor GC Count|551|491|458| Total 224|
||Full GC Count|1|0|1||
||Avg Pause GC Time|4.67 ms|2.51 ms|2.03 ms|2.45 ms|
||Max Pause GC Time|60.0 ms|30.0 ms|50.0 ms|20.0 ms|
||requests|1462198|1280960|1439775|1176116|
|512M|Minor GC Count|212|260|242| Total 123|
||Full GC Count|1|0|0||
||Avg Pause GC Time|5.40 ms|4.23 ms|2.15 ms|3.45 ms|
||Max Pause GC Time|60.0 ms|20.0 ms|10.0 ms|20.0 ms|
||requests|1210397|1373843|1452618|1343819|
|1G|Minor GC Count|||86||
||Full GC Count|||1||
||Avg Pause GC Time|||2.99 ms||
||Max Pause GC Time|||50.0 ms||
||requests|||||
|4G|Minor GC Count|||||
||Full GC Count|||1||
||Avg Pause GC Time|||||
||Max Pause GC Time|||||
||requests|||||
注：由于时间来不及，暂时只做了512M这组，晚点有时间继续测试更新

* +UseSerialGC
  * Serial + Serial Old 收集器的组合
  * 由于是单线程的，收集时间更长、效率低，处理请求能力和响应都低和慢
* +UseParallelGC
  * Parallel Scavenge + Parallel Old 收集器的组合
  * 处理请求能力比CMS好，但是响应会慢一些，并发性能最好
* +UseConcMarkSweepGC
  * ParNew + CMS 收集器的组合
  * CMS GC的过程会伴随着一次至多次YoungGC
  * 可以看到CMS GC的6个阶段：初始标记Initial Mark、并发标记Concurrent Mark、并发预清理Concurrent Preclean、最终标记Final Remark、并发清理Concurrent Sweep、并发重制Concurrent Reset
  * 处理请求能力比ParallelGC慢，但是响应会快一些
* +UseG1GC
  * G1是CMS算法的一个升级
  * 阶段：纯年轻代模式转移暂停Evacuation Pause：Young、并发标记Concurrent Mark、阶段1初始标记Initial Mark、阶段2Root扫描Root Region Scan、阶段3并发标记Concurrent Mark、阶段4再次标记Remark、阶段5清理Cleanup、转移暂停：混合模式Evacuation Pause（mixed）
  * G1 某些情况会退化成串行化GC
  * GC 的次数最少，并发和响应都比较适中