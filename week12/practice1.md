## Redis 主从复制
#### 使用 docker 准备两个实例 
``` shell 
# 下载 image
docker pull redis
# 查看当前版本
docker image inspect redis:latest|grep -i version
```
在本机上创建两个目录（我一般把配置文件和数据的目录映射到本机目录上）,copy 课件上的两个redis conf文件
``` dir
redis
    --last   
        --6379  
            --conf  
                redis6379.conf
            --data  
        -- 6380
            --conf 
                redis6380.conf
            --data   
```
创建 redis server 主、从容器
``` shell 
# 6379端口 redis server
docker create \
-p 6379:6379 \
--name redis-6.2.6-6379 \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6379/conf/redis6379.conf:/etc/redis/redis.conf \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6379/data:/data \
redis redis-server /etc/redis/redis.conf \
--appendonly yes 

# 6380端口 redis server
docker create \
-p 6380:6380 \
--name redis-6.2.6-6380 \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6380/conf/redis6380.conf:/etc/redis/redis.conf \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6380/data:/data \
redis redis-server /etc/redis/redis.conf \
--appendonly yes
```
注意如果使用 docker run 来创建 container，需要加 -d 参数，以6380为例
``` shell 
# 6380端口 redis server
docker run \
-p 6380:6380 \
--name redis-6.2.6-6380 \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6380/conf/redis6380.conf:/etc/redis/redis.conf \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/6380/data:/data \
-d redis redis-server /etc/redis/redis.conf \
--appendonly yes
```
启动 redis 主从
``` shell
docker start redis-6.2.6-6379
docker start redis-6.2.6-6380
```
这个时候会报错
``` log
1:M 20 Jan 2022 15:25:40.244 # Warning: Could not create server TCP listening socket ::1:6379: bind: Cannot assign requested address
1:M 20 Jan 2022 15:25:40.244 # Failed listening on port 6379 (TCP), aborting.
```
分别打开6379和6380的配置文件，找到 dir 和 bind ，分别注释。因为我们这里用的是 docker 启动。
注释完之后就可以启动了
``` conf
# bind 127.0.0.1 ::1
# dir "/Users/kimmking/logs/redis0"
```
查看我们启动的docker container
``` shell
docker ps
```
可以看到 ```0.0.0.0:6380->6380/tcp```，6380端口已经绑定成功
``` logs
CONTAINER ID   IMAGE     COMMAND                  CREATED        STATUS         PORTS                              NAMES
6bf32f82b9c7   redis     "docker-entrypoint.s…"   40 hours ago   Up 2 seconds   6379/tcp, 0.0.0.0:6380->6380/tcp   redis-6.2.6-6380
33ed3b0ef153   redis     "docker-entrypoint.s…"   40 hours ago   Up 5 seconds   0.0.0.0:6379->6379/tcp             redis-6.2.6-6379
```
这个时候我们可以进入 docker redis server 执行操作了
``` shell
docker exec -it redis-6.2.6-6379 /bin/bash
redis-cli -p 6379

docker exec -it redis-6.2.6-6380 /bin/bash
redis-cli -p 6380
```
将6380改为6379的从库，因为用的是 docker 所以使用局域网IP
``` shell 
# 查看主 redis 的ip 
docker inspect redis-6.2.6-6379
# 进入从 redis，并且指向主 redis 
docker exec -it redis-6.2.6-6380 /bin/bash
redis-cli -p 6380
# 老版本设置方式
slaveof 172.17.0.2 6379
# 新版本设置方式
# replicaof 172.17.0.2 6379
```
这个时候查看 docker 日志，发现有错误
``` log 
1:S 22 Jan 2022 08:49:41.381 # Error reply to PING from master: '-DENIED Redis is running in protected mode because protected mode is enabled, no bind address was specified, no authentication password is requested to clients. In this mode connections are only accepted from the loopback interface. If you want to connect'
```
redis 默认局域网访问是关闭的，我们关闭保护，打开访问
``` conf 
protected-mode no
```

#### 测试
进入主 redis，随便添加个key 
``` shell 
docker exec -it redis-6.2.6-6379 /bin/bash
redis-cli -p 6379
set page 111
get page
```
进入从 redis，查看，发现已经同步过来了，并且从 redis 不允许在写了
``` shell 
docker exec -it redis-6.2.6-6380 /bin/bash
redis-cli -p 6380
get page
set page 999
# (error) READONLY You can't write against a read only replica.
```

## sentinel 高可用
在上面的基础上，在增加一个新的redis server 6381，这里 docker 里的端口保留6379配置
``` logs 
CONTAINER ID   IMAGE     COMMAND                  CREATED        STATUS       PORTS                              NAMES
baa3c76915b6   redis     "docker-entrypoint.s…"   5 hours ago    Up 5 hours   0.0.0.0:6381->6379/tcp             redis-6.2.6-6381
```
增加三个 sentinel 配置文件
``` conf 
# 唯一
sentinel myid 8d992c54df8f8677b0b345825f61fb733c73d14c
sentinel deny-scripts-reconfig yes
# 监听主节点 
sentinel monitor mymaster 172.17.0.2 6379 3
# 主节点 down 了之后，10秒切换
sentinel down-after-milliseconds mymaster 10000
# Generated by CONFIG REWRITE
protected-mode no
port 26379
user default on nopass sanitize-payload ~* &* +@all
```
``` conf 
sentinel myid 8d992c54df8f8677b0b345825f61fb733c73d14d
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 172.17.0.2 6379 3
port 26380
sentinel down-after-milliseconds mymaster 10000
# Generated by CONFIG REWRITE
protected-mode no
user default on nopass sanitize-payload ~* &* +@all
```
``` conf 
sentinel myid 8d992c54df8f8677b0b345825f61fb733c73d14e
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 172.17.0.2 6379 3
sentinel down-after-milliseconds mymaster 10000
# Generated by CONFIG REWRITE
protected-mode no
port 26381
user default on nopass sanitize-payload ~* &* +@all
```
copy 到 docker 内
``` bash 
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6379/conf/sentinel0.conf redis-6.2.6-6379:/etc/redis/sentinel0.conf
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6380/conf/sentinel1.conf redis-6.2.6-6380:/etc/redis/sentinel1.conf
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6381/conf/sentinel2.conf redis-6.2.6-6381:/etc/redis/sentinel2.conf
```
分别启动各自docker 下的 sentinel 
``` shell 
docker exec -it redis-6.2.6-6379 /bin/bash
redis-cli -p 6379
nohup redis-sentinel /etc/redis/sentinel0.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs
```
``` shell 
docker exec -it redis-6.2.6-6380 /bin/bash
redis-cli -p 6380
nohup redis-sentinel /etc/redis/sentinel1.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs
```
``` shell 
docker exec -it redis-6.2.6-6381 /bin/bash
redis-cli -p 6379
nohup redis-sentinel /etc/redis/sentinel2.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs
```
查看 各自的 conf 文件，有被 sentinel 改写
``` 
cat /etc/redis/sentinel0.conf
cat /etc/redis/sentinel1.conf
cat /etc/redis/sentinel2.conf
```
查看各自追加的部分，可以看到三个 redis 已经被互相感知到了
* 6379
``` logs 
dir "/etc/redis"
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 0
sentinel known-replica mymaster 172.17.0.3 6380
sentinel current-epoch 0
sentinel known-replica mymaster 172.17.0.4 6379
sentinel known-sentinel mymaster 172.17.0.3 26380 8d992c54df8f8677b0b345825f61fb733c73d14d
sentinel known-sentinel mymaster 172.17.0.4 26381 8d992c54df8f8677b0b345825f61fb733c73d14e
```
* 6380
``` logs 
dir "/"
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 0
sentinel known-replica mymaster 172.17.0.3 6380
sentinel current-epoch 0
sentinel known-replica mymaster 172.17.0.4 6379
sentinel known-sentinel mymaster 172.17.0.4 26381 8d992c54df8f8677b0b345825f61fb733c73d14e
sentinel known-sentinel mymaster 172.17.0.2 26379 8d992c54df8f8677b0b345825f61fb733c73d14c
```
* 6381
``` logs 
dir "/"
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 0
sentinel known-replica mymaster 172.17.0.3 6380
sentinel current-epoch 0
sentinel known-replica mymaster 172.17.0.4 6379
sentinel known-sentinel mymaster 172.17.0.2 26379 8d992c54df8f8677b0b345825f61fb733c73d14c
sentinel known-sentinel mymaster 172.17.0.3 26380 8d992c54df8f8677b0b345825f61fb733c73d14d
```
我们断开主节点 6379 看看会发生什么
``` shell 
docker stop redis-6.2.6-6379
```
查看 docker 后台日志
``` logs 
1:S 22 Jan 2022 13:39:32.968 * Connecting to MASTER 172.17.0.2:6379
1:S 22 Jan 2022 13:39:32.968 * MASTER <-> REPLICA sync started
1:S 22 Jan 2022 13:39:35.103 # Error condition on socket for SYNC: No route to host
```
我们发现并没有切换主从，而是一直卡在连接 【172.17.0.2:6379】上

思来想去，参考资料上直接是 shutdown redis server 的，而我这里是stop container，而sentinel是在container里启动的。
由于我们 sentinel.conf文件配置的选举数是 3。
想到有两种方式改进：
* 在docker里直接 kill redis server 
* 将选举数改为 2 

我这里先用方式2，该配置文件，这里只给出其中一个，其他类似
``` conf 
# 唯一
sentinel myid 8d992c54df8f8677b0b345825f61fb733c73d14c
sentinel deny-scripts-reconfig yes
# 监听主节点，这里改为 2
sentinel monitor mymaster 172.17.0.2 6379 2
# 主节点 down 了之后，10秒切换
sentinel down-after-milliseconds mymaster 10000
# Generated by CONFIG REWRITE
protected-mode no
port 26379
user default on nopass sanitize-payload ~* &* +@all
```
我们重新执行docker
``` shell 
# 停docker container
docker stop redis-6.2.6-6379
docker stop redis-6.2.6-6380
docker stop redis-6.2.6-6381 

# 重新覆盖 conf （也可以选择直接在 docker 里 vim 修改）
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6379/conf/sentinel0.conf redis-6.2.6-6379:/etc/redis/sentinel0.conf
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6380/conf/sentinel1.conf redis-6.2.6-6380:/etc/redis/sentinel1.conf
docker cp /Users/xuqingbin/Documents/docker-conf/redis/last/6381/conf/sentinel2.conf redis-6.2.6-6381:/etc/redis/sentinel2.conf

# 重新启动
docker start redis-6.2.6-6379
docker start redis-6.2.6-6380
docker start redis-6.2.6-6381

# 6379
docker exec -it redis-6.2.6-6379 /bin/bash
nohup redis-sentinel /etc/redis/sentinel0.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs

# 6380 
docker exec -it redis-6.2.6-6380 /bin/bash
redis-cli -p 6380 
slaveof 172.17.0.2 6379
exit 
nohup redis-sentinel /etc/redis/sentinel1.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs

# 6381 
docker exec -it redis-6.2.6-6381 /bin/bash
redis-cli -p 6379 
slaveof 172.17.0.2 6379
exit 
nohup redis-sentinel /etc/redis/sentinel2.conf > /logs/sentinel.logs 2>&1 &
tail -f /logs/sentinel.logs
```
再次停用主节点 
``` shell 
docker stop redis-6.2.6-6379
```
10秒之后可以在docker后台日志看到
``` logs 
1:M 22 Jan 2022 15:17:55.727 # Setting secondary replication ID to aa0fa7371410a28c1e8ee985e32b7c40bf1cb71e, valid up to offset: 57996. New replication ID is 123f0f969814e57005138fae5359b28422c3c12e
1:M 22 Jan 2022 15:17:55.727 * MASTER MODE enabled (user request from 'id=8 addr=172.17.0.3:47858 laddr=172.17.0.3:6380 fd=9 name=sentinel-8d992c54-cmd age=300 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=188 qbuf-free=40766 argv-mem=4 obl=45 oll=0 omem=0 tot-mem=61468 events=r cmd=exec user=default redir=-1')
1:M 22 Jan 2022 15:17:55.734 # Could not create tmp config file (Permission denied)
1:M 22 Jan 2022 15:17:55.734 # CONFIG REWRITE failed: Permission denied
1:M 22 Jan 2022 15:17:55.901 * Replica 172.17.0.4:6379 asks for synchronization
1:M 22 Jan 2022 15:17:55.902 * Partial resynchronization request from 172.17.0.4:6379 accepted. Sending 158 bytes of backlog starting from offset 57996.
```
查看各个 redis server info，发现6380已经变成 master节点了
``` shell 
redis-cli -p 6380 
info 
```
然后我们在启动 6379 节点，看看会发生什么
``` shell 
docker start redis-6.2.6-6379
docker exec -it redis-6.2.6-6379 /bin/bash
redis-cli -p 6379
info 
```
这个发现 6379 已经自动变为 slave 节点了，这个时候我们还没启动 6379 的 sentinel 服务，但是也照样生效了
这也从间接说明 sentinel 关联着所有之前配置好的 redis server 节点
变为 salve 的节点已经不能在写了

#### java 使用 
todo 

## Cluster 集群
参考：https://www.cnblogs.com/niceyoo/p/13011626.html

#### 准备3个主从节点，6个 redis server
目录如下：
``` dir 
cluster-test
    -- node1
        -- 6400
        -- 6401
    -- node2
        -- 6410
        -- 6411
    -- node3
        -- 6420
        -- 6421
```
一个节点的创建过程
``` shell 
# 创建节点-主
docker create \
-p 6400:6379 \
--name redis-6.2.6-6400 \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6400/conf/redis.conf:/etc/redis/redis.conf \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6400/data:/data \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6400/conf:/etc/redis \
redis redis-server /etc/redis/redis.conf \
--appendonly yes

# 启动
docker start redis-6.2.6-6400

# 查看 docker ip
docker inspect redis-6.2.6-6400 | grep IPAddress
```
``` logs 
"SecondaryIPAddresses": null,
"IPAddress": "172.17.0.5",
        "IPAddress": "172.17.0.5",
```
修改主节点 sentinel.conf、从节点 redis.conf 配置、从节点 sentinel.conf
``` conf 
# 主节点 sentinel.conf
sentinel monitor mymaster 172.17.0.5 6379 1
# 从节点 redis.conf
replicaof 172.17.0.5 6379
# 从节点 sentinel.conf
sentinel monitor mymaster 172.17.0.5 6379 1
```
启动从节点 6401 
``` shell 
# 创建节点-主
docker create \
-p 6401:6379 \
--name redis-6.2.6-6401 \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6401/conf/redis.conf:/etc/redis/redis.conf \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6401/data:/data \
-v /Users/xuqingbin/Documents/docker-conf/redis/last/cluster-test/node1/6401/conf:/etc/redis \
redis redis-server /etc/redis/redis.conf \
--appendonly yes

# 启动
docker start redis-6.2.6-6401
```
6401启动报错
``` logs 
replicaof directive not allowed in cluster mode
```
将 6401 的 cluster-enabled 关闭或者改为 no。
开始我以为后续模拟当6400主节点挂掉后，6401从节点通过 sentinel 切换为主节点，所以 6401 应该也要打开 cluster-enabled。
重新启动 6401，成功
``` conf 
# cluster-enabled yes
cluster-enabled no
```

启动 sentinel 高可用
``` shell 
docker exec -it redis-6.2.6-6400 /bin/bash
nohup redis-sentinel /etc/redis/sentinel.conf > sentinel.log 2>&1 &
tail -f sentinel.log
```

#### 关联设置 redis cluster 
进入 6400 节点
``` shell 
# --cluster-replicas 1 表示我们希望为集群中的每个主节点创建一个从节点
redis-cli --cluster create 172.17.0.5:6379 172.17.0.7:6379 172.17.0.9:6379 --cluster-replicas 1
```
``` logs 
*** ERROR: Invalid configuration for cluster creation.
*** Redis Cluster requires at least 3 master nodes.
*** This is not possible with 3 nodes and 1 replicas per node.
*** At least 6 nodes are required.
```
--cluster-replicas 改为 0，成功，从上面日志大概可以看出，cluster需要至少3个节点。
如果设置 --cluster-replicas 1，表示3个节点，3个从节点
``` shell 
redis-cli --cluster create 172.17.0.5:6379 172.17.0.7:6379 172.17.0.9:6379 --cluster-replicas 0
```
``` logs 
>>> Performing hash slots allocation on 3 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
M: 6fcbb3c6eb324e620b52d038e257cd94d74051ad 172.17.0.5:6379
   slots:[0-5460] (5461 slots) master
M: 439a4a22402d03a34db9cf96a4065a20a51ce21f 172.17.0.7:6379
   slots:[5461-10922] (5462 slots) master
M: f1662449781abaadecd5c74dd5b1ce51e1bed489 172.17.0.9:6379
   slots:[10923-16383] (5461 slots) master
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 172.17.0.5:6379)
M: 6fcbb3c6eb324e620b52d038e257cd94d74051ad 172.17.0.5:6379
   slots:[0-5460] (5461 slots) master
M: 439a4a22402d03a34db9cf96a4065a20a51ce21f 172.17.0.7:6379
   slots:[5461-10922] (5462 slots) master
M: f1662449781abaadecd5c74dd5b1ce51e1bed489 172.17.0.9:6379
   slots:[10923-16383] (5461 slots) master
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
#### 测试 cluster 集群
进入 6400 节点
``` shell 
docker exec -it redis-6.2.6-6400 /bin/bash
redis-cli -c -p 6379
set page 879898421
get page
set kk 1
```
进入 6410 节点
``` shell 
docker exec -it redis-6.2.6-6410 /bin/bash
redis-cli -c -p 6379
get page
```
可以看到从 .0.5节点拿到数据
``` logs 
-> Redirected to slot [144] located at 172.17.0.5:6379
"879898421"
```
当时当我把 6400 节点 down 掉后，其他节点都不能使用了
#### 问题
* 搞错了一个概念，以为是 Redis+Sentinel+Cluster，但其实要么是方案A（Redis+Cluster）要么是方案B（Redis+Sentinel）
* 我们重新配置

关闭之前的 redis server，修改6401、6402、6411、6421配置，并重新启动 
``` shell 
# 注释所有 replicaof
# replicaof 172.17.0.5 6379
# 打开所有 cluster
cluster-enabled yes
```
重启服务
``` shell 
# 重启服务
docker start redis-6.2.6-6400 redis-6.2.6-6401 redis-6.2.6-6402 redis-6.2.6-6410 redis-6.2.6-6411 redis-6.2.6-6420 redis-6.2.6-6421
# 查看IP
docker inspect redis-6.2.6-6400 redis-6.2.6-6401 redis-6.2.6-6402 redis-6.2.6-6410 redis-6.2.6-6411 redis-6.2.6-6420 redis-6.2.6-6421 | grep IPAddress
# 随便进入一个节点
docker exec -it redis-6.2.6-6400 /bin/bash
# 设置 cluster 
redis-cli --cluster create 172.17.0.5:6379 172.17.0.6:6379 172.17.0.7:6379 172.17.0.8:6379 172.17.0.9:6379 172.17.0.10:6379 172.17.0.11:6379 --cluster-replicas 1
```
``` logs 
>>> Performing hash slots allocation on 7 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 172.17.0.9:6379 to 172.17.0.5:6379
Adding replica 172.17.0.10:6379 to 172.17.0.6:6379
Adding replica 172.17.0.11:6379 to 172.17.0.7:6379
Adding extra replicas...
Adding replica 172.17.0.8:6379 to 172.17.0.5:6379
M: 70ef6ae4dde2be70a7af7e5cada5667d245c14cb 172.17.0.5:6379
   slots:[0-5460] (5461 slots) master
M: 6470093d1bb72f6ef9ed4e676e7d4971ae57f941 172.17.0.6:6379
   slots:[5461-10922] (5462 slots) master
M: ef925856f181dfcb29ad04922f592bba90566500 172.17.0.7:6379
   slots:[10923-16383] (5461 slots) master
S: c727402020a9a8dd242787fc7d61b692a6616766 172.17.0.8:6379
   replicates 70ef6ae4dde2be70a7af7e5cada5667d245c14cb
S: 99178726528c2103e14e527fcda17bda73cdad9c 172.17.0.9:6379
   replicates 70ef6ae4dde2be70a7af7e5cada5667d245c14cb
S: 4ccd8dd75924f9c4247f4b0340ebf70b802ba264 172.17.0.10:6379
   replicates 6470093d1bb72f6ef9ed4e676e7d4971ae57f941
S: f4413a779691b2bd454c5b16a6e64ee37955fb05 172.17.0.11:6379
   replicates ef925856f181dfcb29ad04922f592bba90566500
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 172.17.0.5:6379)
M: 70ef6ae4dde2be70a7af7e5cada5667d245c14cb 172.17.0.5:6379
   slots:[0-5460] (5461 slots) master
   2 additional replica(s)
M: 6470093d1bb72f6ef9ed4e676e7d4971ae57f941 172.17.0.6:6379
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: f4413a779691b2bd454c5b16a6e64ee37955fb05 172.17.0.11:6379
   slots: (0 slots) slave
   replicates ef925856f181dfcb29ad04922f592bba90566500
S: 4ccd8dd75924f9c4247f4b0340ebf70b802ba264 172.17.0.10:6379
   slots: (0 slots) slave
   replicates 6470093d1bb72f6ef9ed4e676e7d4971ae57f941
S: 99178726528c2103e14e527fcda17bda73cdad9c 172.17.0.9:6379
   slots: (0 slots) slave
   replicates 70ef6ae4dde2be70a7af7e5cada5667d245c14cb
S: c727402020a9a8dd242787fc7d61b692a6616766 172.17.0.8:6379
   slots: (0 slots) slave
   replicates 70ef6ae4dde2be70a7af7e5cada5667d245c14cb
M: ef925856f181dfcb29ad04922f592bba90566500 172.17.0.7:6379
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
可以看到生成了三个M，四个S，关系为
* M 172.17.0.5:6379
    * S 172.17.0.8:6379
    * S 172.17.0.9:6379
* M 172.17.0.6:6379
    * S 172.17.0.10:6379
* M 172.17.0.7:6379
    * S 172.17.0.11:6379

此外，我们可以使用以下命令随时查看节点情况
``` shell 
redis-cli -c cluster nodes
```
``` logs 
6470093d1bb72f6ef9ed4e676e7d4971ae57f941 172.17.0.6:6379@16379 master - 0 1642946733961 2 connected 5461-10922
c727402020a9a8dd242787fc7d61b692a6616766 172.17.0.8:6379@16379 master - 0 1642946729000 8 connected 0-5460
f4413a779691b2bd454c5b16a6e64ee37955fb05 172.17.0.11:6379@16379 slave ef925856f181dfcb29ad04922f592bba90566500 0 1642946732940 3 connected
70ef6ae4dde2be70a7af7e5cada5667d245c14cb 172.17.0.5:6379@16379 master,fail - 1642946208729 1642946204678 1 connected
4ccd8dd75924f9c4247f4b0340ebf70b802ba264 172.17.0.10:6379@16379 slave 6470093d1bb72f6ef9ed4e676e7d4971ae57f941 0 1642946732000 2 connected
99178726528c2103e14e527fcda17bda73cdad9c 172.17.0.9:6379@16379 slave c727402020a9a8dd242787fc7d61b692a6616766 0 1642946731918 8 connected
ef925856f181dfcb29ad04922f592bba90566500 172.17.0.7:6379@16379 myself,master - 0 1642946732000 3 connected 10923-16383
```

测试下设置值，正常使用
``` shell 
# 6400 
set page 1111

# 6401、6402、6410、6411 
get page 
set page 2222
```
``` logs 
-> Redirected to slot [144] located at 172.17.0.5:6379
"1111"
```

关闭 6400 节点（172.17.0.5:6379），可以看到主节点自动做了切换（6410），并且可以正常使用

6410 节点（172.17.0.8:6379）
``` shell
1:S 23 Jan 2022 13:57:07.542 # Starting a failover election for epoch 8.
1:S 23 Jan 2022 13:57:07.552 # Failover election won: I'm the new master.
1:S 23 Jan 2022 13:57:07.552 # configEpoch set to 8 after successful failover
1:M 23 Jan 2022 13:57:07.552 * Discarding previously cached master state.
1:M 23 Jan 2022 13:57:07.552 # Setting secondary replication ID to 684f9b48a6a29138c3cac9531b46e0b00dd48628, valid up to offset: 1075. New replication ID is c0b5a3d0bc8d83e654261d83f868459653cb680c
1:M 23 Jan 2022 13:57:07.552 # Cluster state changed: ok
1:M 23 Jan 2022 13:57:07.569 * Replica 172.17.0.9:6379 asks for synchronization
1:M 23 Jan 2022 13:57:07.570 * Partial resynchronization request from 172.17.0.9:6379 accepted. Sending 0 bytes of backlog starting from offset 1075.
```

6411 节点（172.17.0.9:6379）
``` shell 
1:S 23 Jan 2022 13:57:07.557 # Configuration change detected. Reconfiguring myself as a replica of c727402020a9a8dd242787fc7d61b692a6616766
1:S 23 Jan 2022 13:57:07.558 * Connecting to MASTER 172.17.0.8:6379
1:S 23 Jan 2022 13:57:07.558 * MASTER <-> REPLICA sync started
1:S 23 Jan 2022 13:57:07.558 # Cluster state changed: ok
1:S 23 Jan 2022 13:57:07.568 * Non blocking connect for SYNC fired the event.
1:S 23 Jan 2022 13:57:07.568 * Master replied to PING, replication can continue...
1:S 23 Jan 2022 13:57:07.569 * Trying a partial resynchronization (request 684f9b48a6a29138c3cac9531b46e0b00dd48628:1075).
1:S 23 Jan 2022 13:57:07.570 * Successful partial resynchronization with master.
1:S 23 Jan 2022 13:57:07.570 # Master replication ID changed to c0b5a3d0bc8d83e654261d83f868459653cb680c
1:S 23 Jan 2022 13:57:07.570 * MASTER <-> REPLICA sync: Master accepted a Partial Resynchronization.
```

#### java使用
todo 