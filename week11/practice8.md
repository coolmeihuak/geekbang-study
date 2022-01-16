见package：cc.page.study.week11.practice8

* 分布式锁实现思路
    * 假设有多个部署在不同节点的服务1，服务2...
    * Java在同一个服务下的锁在实现时，有等待队列和同步队列的概念，并且解锁时会通知处于同步队列的阻塞线程，让其竞争获取锁
    * set key value [NX] [EX] 命令：当有值时，会失败
    * 在分布式环境下，各个服务之间是感知不到对方的，我们利用【redis的发布订阅通知各个服务】+【循环set key value [NX] [EX]失败时阻塞】
    * ``` if redis.call('get',KEYS[1]) == ARGV[1] then " + "return redis.call('del',KEYS[1]) else return 0 end ```命令解锁