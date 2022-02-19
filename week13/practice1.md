## kafka 单机版本
* 下载：https://archive.apache.org/dist/kafka/2.7.0/kafka_2.13-2.7.0.tgz
* 解压完成后，配置环境变量，方便随时切换
``` conf 
KAFKA_2_7_0_HOME=/Applications/kafka/kafka_2.13-2.7.0
KAFKA_3_1_0_HOME=/Applications/kafka/kafka_2.13-3.1.0
KAFKA_HOME=$KAFKA_2_7_0_HOME
export PATH=$PATH:$KAFKA_HOME/bin:.
```
* 修改$KAFKA_HOME/config/server.properties
```shell
listeners=PLAINTEXT://localhost:9092
```
* 启动 zookeeper 
```shell
zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
```
* 启动 kafka 
```shell
kafka-server-start.sh $KAFKA_HOME/config/server.properties
```
* 创建测试 topic testk
```shell
kafka-topics.sh --zookeeper localhost:2181 --create --topic testk --partitions 3 --replication-factor 1
```
* 查看 topic 信息
```shell
kafka-topics.sh --zookeeper localhost:2181 --describe --topic testk
```
* 订阅 topic 
```shell
kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic testk
```
* 发布 topic 
```shell
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic testk
```
* 简单性能测试
```shell
kafka-producer-perf-test.sh --topic testk --num-records 100000 --record-size 100000 --throughput 2000 --producer-props bootstrap.servers=localhost:9092
kafka-consumer-perf-test.sh --bootstrap-server localhost:9092 --topic testk --fetch-size 1048576 --messages 100000 --threads 1
```
``` logs 
10000 records sent, 1999.6 records/sec (1.91 MB/sec), 1.4 ms avg latency, 259.0 ms max latency.
10004 records sent, 2000.8 records/sec (1.91 MB/sec), 0.4 ms avg latency, 5.0 ms max latency.
10000 records sent, 2000.0 records/sec (1.91 MB/sec), 0.3 ms avg latency, 6.0 ms max latency.
10000 records sent, 2000.0 records/sec (1.91 MB/sec), 0.3 ms avg latency, 1.0 ms max latency.
10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.2 ms avg latency, 1.0 ms max latency.
10000 records sent, 2000.0 records/sec (1.91 MB/sec), 0.3 ms avg latency, 7.0 ms max latency.
10003 records sent, 2000.2 records/sec (1.91 MB/sec), 0.3 ms avg latency, 3.0 ms max latency.
10001 records sent, 2000.2 records/sec (1.91 MB/sec), 0.2 ms avg latency, 2.0 ms max latency.
10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.2 ms avg latency, 3.0 ms max latency.
100000 records sent, 1999.600080 records/sec (1.91 MB/sec), 0.39 ms avg latency, 259.00 ms max latency, 0 ms 50th, 1 ms 95th, 2 ms 99th, 15 ms 99.9th.

WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2022-01-30 16:37:36:696, 2022-01-30 16:37:37:307, 96.6072, 158.1133, 100014, 163689.0344, 1643531856994, -1643531856383, -0.0000, -0.0001
```

## kafka 集群
* 在 $KAFKA_HOME 下建立 cluster 目录
* 从 $KAFKA_HOME/config/server.properties copy 三份至 $KAFKA_HOME/cluster 目录
* 分别修改如下：
``` conf 
broker.id=1
listeners=PLAINTEXT://localhost:9001
broker.list=localhost:9001,localhost:9002,localhost:9003
log.dirs=/tmp/kafka/kafka-logs1

broker.id=2
listeners=PLAINTEXT://localhost:9002
broker.list=localhost:9001,localhost:9002,localhost:9003
log.dirs=/tmp/kafka/kafka-logs2

broker.id=3
listeners=PLAINTEXT://localhost:9003
broker.list=localhost:9001,localhost:9002,localhost:9003
log.dirs=/tmp/kafka/kafka-logs3
```
* 创建log文件
```shell
mkdir /tmp/kafka/
mkdir /tmp/kafka/kafka-logs1/
mkdir /tmp/kafka/kafka-logs2/
mkdir /tmp/kafka/kafka-logs3/
```
* 使用 ZooInspector 删除数据
    * https://issues.apache.org/jira/secure/attachment/12436620/ZooInspector.zip
    * 解压后执行：java -jar zookeeper-dev-ZooInspector.jar
    * 连接到 zookeeper 后删除除了 zookeeper 以外的所有文件
* 重新启动 zookeeper 
```shell
zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
```
* 分别启动三个 Kafka 
```shell
kafka-server-start.sh $KAFKA_HOME/cluster/9001.properties
kafka-server-start.sh $KAFKA_HOME/cluster/9002.properties
kafka-server-start.sh $KAFKA_HOME/cluster/9003.properties
```
* 创建 topic
```shell
kafka-topics.sh --zookeeper localhost:2181 --create --topic test32 --partitions 3 --replication-factor 2
```
* 查看 topic 
```shell
kafka-topics.sh --zookeeper localhost:2181 --describe --topic test32
```
``` logs
Topic: test32	PartitionCount: 3	ReplicationFactor: 2	Configs:
	Topic: test32	Partition: 0	Leader: 1	Replicas: 1,2	Isr: 1,2
	Topic: test32	Partition: 1	Leader: 2	Replicas: 2,3	Isr: 2,3
	Topic: test32	Partition: 2	Leader: 3	Replicas: 3,1	Isr: 3,1
```
* 测试下发布订阅
```shell
kafka-console-consumer.sh --bootstrap-server localhost:9001  --topic test32
kafka-console-consumer.sh --bootstrap-server localhost:9002  --topic test32
kafka-console-consumer.sh --bootstrap-server localhost:9003  --topic test32

kafka-console-producer.sh --bootstrap-server localhost:9001 --topic test32
kafka-console-producer.sh --bootstrap-server localhost:9002 --topic test32
kafka-console-producer.sh --bootstrap-server localhost:9003 --topic test32
```
我们在任意节点发布消息，任意订阅节点都能收到消息
* 性能测试
```shell
kafka-producer-perf-test.sh --topic test32 --num-records 100000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=localhost:9002 
kafka-consumer-perf-test.sh --bootstrap-server localhost:9002 --topic test32 -fetch-size 1048576 --messages 100000 --threads 1
```
``` logs
100000 records sent, 31181.789835 records/sec (29.74 MB/sec), 760.56 ms avg latency, 1147.00 ms max latency, 822 ms 50th, 1044 ms 95th, 1115 ms 99th, 1144 ms 99.9th.

WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2022-01-30 22:28:43:827, 2022-01-30 22:28:44:459, 95.3674, 150.8979, 100003, 158232.5949, 1643552924149, -1643552923517, -0.0000, -0.0001
```

## Spring boot 使用 Kafka
* bootstrap-servers集群: http://localhost:9001,http://localhost:9002,http://localhost:9003
* consumer：cc.page.study.week13.KafkaConsumer
* producer：cc.page.study.week13.KafkaProducer
* 启动cc.page.study.week13.KafkaApplication，开启consumer监听
* 执行test，KafkaProducerApplicationTests::send，生产消息
``` log 
2022-02-19 17:21:14.059  INFO 8625 --- [ntainer#0-0-C-1] cc.page.study.week13.KafkaConsumer       : ----------------- record =ConsumerRecord(topic = topic-1, partition = 0, leaderEpoch = 0, offset = 0, CreateTime = 1645262374839, serialized key size = -1, serialized value size = 112, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = {"id":"b9857aab-ffe3-4875-942e-e678325a0e1a","content":"this is kafka message","time":"Feb 19, 2022 5:19:34 PM"})
2022-02-19 17:21:14.059  INFO 8625 --- [ntainer#0-0-C-1] cc.page.study.week13.KafkaConsumer       : ------------------ message ={"id":"b9857aab-ffe3-4875-942e-e678325a0e1a","content":"this is kafka message","time":"Feb 19, 2022 5:19:34 PM"}
```