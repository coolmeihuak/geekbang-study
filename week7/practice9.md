## 说明
* 见 【src/main/java/cc/gegee/study/week7/practice9】

## 创建多个数据库实例
#### docker创建多个数据库实例
``` cmd
docker pull mysql:8.0

docker create --name mysql-8.0-mulpitle-master -p 3201:3306 -v ~/Documents/docker-conf/mysql8.0-mulpitle/master/conf:/etc/mysql/conf.d -v ~/Documents/docker-conf/mysql8.0-mulpitle/master/logs:/logs -v ~/Documents/docker-conf/mysql8.0-mulpitle/master/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0

docker create --name mysql-8.0-mulpitle-slave0 -p 3202:3306 -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave0/conf:/etc/mysql/conf.d -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave0/logs:/logs -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave0/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0

docker create --name mysql-8.0-mulpitle-slave1 -p 3203:3306 -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave1/conf:/etc/mysql/conf.d -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave1/logs:/logs -v ~/Documents/docker-conf/mysql8.0-mulpitle/slave1/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0
```
#### 初始化和启动数据库
进入docker
``` cmd
docker exec -it mysql-8.0-mulpitle-master /bin/bash
```
添加配置文件
``` cmd
## 查找配置文件位置
mysql --help --verbose | grep my.cnf
cat /etc/mysql/my.cnf
## 找到 !includedir
cd /etc/mysql/conf.d/
vim master.cnf
```
主数据库添加配置
``` cnf
[mysqld]
server_id = 1
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
## 记录影响行的数据，非常精确（Statement、Mixed）
binlog-format=Row
```
如果是应用程序，然后用```mysqld```命令指定配置文件启动和初始化```mysqld --defaults-file=xxx.cnf --initialize-insecure```和```mysqld --defaults-file=xxx.cnf start```
``` cnf
[mysqld]
bind-address = 127.0.0.1
port = 3316
server-id = 1
datadir = /xxx/xxx/data
socket = /xxx/xxx.sock
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
## 记录影响行的数据，非常精确（Statement、Mixed）
binlog-format=Row
```
从数据库添加配置
``` cnf
[mysqld]
server_id = 2
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
binlog-format=Row
```
``` cnf
[mysqld]
server_id = 3
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
binlog-format=Row
```
如果是mysql程序
``` mysql
mysqld --defaults-file=xxx.cnf --initialize-insecure
mysqld start
```
如果是docker，直接启动即可，启动完直接init好了
``` mysql
docker start mysql-8.0-mulpitle-master
docker start mysql-8.0-mulpitle-slave0
docker start mysql-8.0-mulpitle-slave1
```

## 数据库主从关系配置
#### 主节点
``` mysql
docker exec -it mysql-8.0-mulpitle-master /bin/bash
mysql -uroot -p
create user 'repl'@'%' identified by '123456';
grant replication slave on *.* to 'repl'@'%';
flush privileges;
show master status;
```
#### 从节点
* 注意
    * host需要填局域网IP
    * 需要重启主节点docker
    * 重新查看主log位置```show master status;```
``` mysql
docker exec -it mysql-8.0-mulpitle-slave0 /bin/bash
mysql -uroot -p
change master to 
    master_host='192.168.3.72',
    master_port=3201,
    master_user='repl',
    master_password='123456',
    master_log_file='mysql-bin.000001',
    master_log_pos=856;
    ## master_auto_position=1
```
``` mysql
docker exec -it mysql-8.0-mulpitle-slave1 /bin/bash
mysql -uroot -p
change master to 
    master_host='192.168.3.72',
    master_port=3201,
    master_user='repl',
    master_password='123456',
    master_log_file='mysql-bin.000001',
    master_log_pos=856;
    ## master_auto_position=1
```
#### 查看配置状态
``` cmd
show master status\G;
show slave status\G;
```
## 使用
#### 三个数据库创建数据库
``` mysql
create database test
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;
use test;
```
``` mysql
create table t1(id int);
```
然后我们发现没有同步，查看节点关联主节点时，有warning
``` mysql
show warnings;
## mysql8.0新加的验证方式
## error connecting to master 'repl@192.168.3.72:3201' - retry-time: 60 retries: 1 message: Authentication plugin 'caching_sha2_password' reported error: Authentication requires secure connection
cd /etc/mysql/conf.d/
vim master.cnf
```
修改xxx.cnf文件，从数据库类似增加，修改完之后全部重启，注意从数据库需要重新定位主数据库log```change master```
``` cnf
[mysqld]
server_id = 1
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
## 记录影响行的数据，非常精确（Statement、Mixed）
binlog-format=Row
default_authentication_plugin=mysql_native_password
```
或者
``` mysql
select host,user,plugin from mysql.user;
alter user 'repl'@'%' identified with mysql_native_password by '123456';
flush privileges;
```
主数据库过程如下
``` cmd
exit
docker restart mysql-8.0-mulpitle-master
docker exec -it mysql-8.0-mulpitle-master /bin/bash
mysql -uroot -p
show master status;
```
从数据库（如果发现由于两边操作不一致无法同步，查询下master的master_log_pos，然后重新修改下slave的change master重新同步一次，每次重启主数据库也需要重新关联一次）
``` cmd
exit
docker restart mysql-8.0-mulpitle-slave0
docker exec -it mysql-8.0-mulpitle-slave0 /bin/bash
mysql -uroot -p
stop slave;
change master to 
    master_host='192.168.3.72',
    master_port=3201,
    master_user='repl',
    master_password='123456',
    master_log_file='mysql-bin.000004',
    master_log_pos=155;
start slave;
show slave status\G;
```
然后在主库执行sql，查看从库，发现自动同步过来了。

## 工程说明
#### 数据源切换思路
![动态切换数据源时序](https://upload-images.jianshu.io/upload_images/7398624-e66e45c506a49b9a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* 利用```ThreadLocal```作为单个请求(每个请求单独一个线程)的全局容器连接```Service```方法控制使用哪个数据源和```EntityManager```使用的数据源，这样```EntityManager```使用的数据源就是在```Service```方法上要求的数据源，即可做到写```Service```方法时决定使用哪个数据源
* 利用```org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource::determineCurrentLookupKey()```暴露获取DataSource的逻辑
* 在service方法上通过配置Annotation，告诉EntityManager使用哪个数据源
    * 自定义Annotation
    * 利用Spring AOP识别Annotation，织入设置数据源逻辑