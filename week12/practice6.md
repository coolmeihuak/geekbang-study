## 准备
docker参考地址：https://hub.docker.com/r/webcenter/activemq
``` shell 
docker pull webcenter/activemq:latest

docker create \
-p 8161:8161 \
-p 61616:61616 \
-p 61613:61613 \
--name activemq \
-v /Users/xuqingbin/Documents/docker-conf/activeMQ/data:/data \
-v /Users/xuqingbin/Documents/docker-conf/activeMQ/logs:/var/log/activemq \
webcenter/activemq:latest

docker start activemq
```

启动后，查看控制台：http://localhost:8161/  
user:admin  
pwd:admin  

## queues
先运行【cc.page.study.week12.queues.JMSConsumer】
然后运行【cc.page.study.week12.queues.JMSProducer】
可以看到有10条消息，被1个consumer消费

## topic
运行【cc.page.study.week12.topic.JMSConsumer】
运行【cc.page.study.week12.topic.JMSConsumer1】
模拟两个消费者
然后运行【cc.page.study.week12.topic.JMSProducer】
可以看到有10条消息，被2个消费者消费了20条消息


