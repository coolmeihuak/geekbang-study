## 说明
* JDBC代码见 【src/main/java/cc/gegee/study/week7/practice2】

## 环境
* mysql: docker + mysql(8.0.18、8.0.27)
* jdk8 
* mac 系统

## JDBC 方式插入
#### statement
* 见 【cc.gegee.study.week7.practice2.Thread1Statement】
* 19 秒

#### 预处理
* 见 【cc.gegee.study.week7.practice2.Thread1PreparedStatement】
* 22 秒

#### 多线程多连接 + statement
* 见 【cc.gegee.study.week7.practice2.ThreadNStatement】
* 21 秒

#### 多线程多连接 + 预处理
* 见 【cc.gegee.study.week7.practice2.ThreadNPreparedStatement】
* 25 秒

#### 总结
* 感觉用不用多线程多连接差不多
* statement 比 预处理方式要快，可能需要多跑几次才能确定

## LOAD DATA 方式插入(mysql版本8.0.18)
* 参考：https://dev.mysql.com/doc/refman/8.0/en/load-data.html
* 因为我这边用的是 docker，会比本机应用程序会再慢些
* 需要--local-infile=1 或者 登录后 SET GLOBAL local_infile = 1
* bin log 打开时测试（其他参数已经优化过了，见下面的优化过程）
``` mysql 
mysql -h 127.0.0.1 -uroot -P 3406 --local-infile=1 -p
SET GLOBAL local_infile = 1;
load data local infile '~/Downloads/order.csv' into table test.`order`
    character set utf8mb4 -- 可选，避免中文乱码问题
    fields terminated by ',' -- 字段分隔符，每个字段(列)以什么字符分隔，默认是 \t
    optionally enclosed by '' -- 文本限定符，每个字段被什么字符包围，默认是空字符
    escaped by '/' -- 转义符，默认是 \
    lines terminated by '\n' -- 记录分隔符，如字段本身也含\n，那么应先去除，否则load data
    ( `user_id`, `status`, `money`) -- 每一行文本按顺序对应的表字段，建议不要省略
    ;
```
* 输出如下，8.61 sec
``` log
Query OK, 1000000 rows affected, 0 warnings (8.61 sec)
Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
```
* 关闭 bin log 测试，4.95 sec
``` log
Query OK, 1000000 rows affected, 0 warnings (4.95 sec)
Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
```


## importTable 方式插入(MySQL Shell mysql版本8.0.27)
* 参考：https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-utilities-parallel-table.html
* 先将之前插入的数据导出，打开一个命令行
``` sql 
## 进入 mysql shell 
mysqlsh
## 进入后 session 连接
shell.connect('mysql://root@localhost:3308')
## 然后输入密码进入
## 导出
util.exportTable("test.`order`", "~/Downloads/order.csv")
## 导入
## The parallel table import utility uses LOAD DATA LOCAL INFILE statements to upload data, 
## so the local_infile system variable must be set to ON on the target server. 
## You can do this by issuing the following statement in SQL mode before running the parallel table import utility
\sql SET GLOBAL local_infile = 1;
util.importTable("~/Downloads/order.csv", {"characterSet": "utf8mb4","schema": "test","table": "order"})
```
* 输出如下，用时11.5021 sec
``` log
Importing from file '/Users/xuqingbin/Downloads/order.csv' to table `test`.`order` in MySQL Server at localhost:3308 using 1 thread
[Worker000] order.csv: Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
100% (19.89 MB / 19.89 MB), 143.13 KB/s
File '/Users/xuqingbin/Downloads/order.csv' (19.89 MB) was imported in 11.5021 sec at 1.73 MB/s
Total rows affected in test.order: Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
```
* 关闭 bin log 测试，4.8136 sec
``` log
Importing from file '/Users/xuqingbin/Downloads/order.csv' to table `test`.`order` in MySQL Server at localhost:3406 using 1 thread
[Worker000] order.csv: Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
100% (19.89 MB / 19.89 MB), 4.38 MB/s
File '/Users/xuqingbin/Downloads/order.csv' (19.89 MB) was imported in 4.8136 sec at 4.13 MB/s
Total rows affected in test.order: Records: 1000000  Deleted: 0  Skipped: 0  Warnings: 0
```
* 从日志可以看出importTable的方式可以支持多个线程，应该可以调整，以进一步提高导入速度


## 优化过程
#### 优化思路可以归结为
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
  
以下是具体说明
#### Inserting indexes
* 开始主键用的是UUID，执行一次需要200多秒，后来改成主键自增
* 影响性能的分析与总结
    * 表上如果有索引，插入时会建立索引
    * 聚集索引因为存储按索引排序存储，插入中间索引值时，可能会引起页分裂。所以，如果一定要用UUID，批量插入时，可以先排序在插入。
    * 对于其他索引，可以先删除索引，完成导入后再重新建索引
#### foreign key
* SET @@foreign_key_checks=0;
* 关闭外键检查来加速表导入
#### UNIQUE
* SET @@unique_checks=0;
* InnoDB最后通过缓冲区批量写入二级索引记录保证唯一性
* 我这里没使用，不涉及
#### columns set default values
* Insert values explicitly only when the value to be inserted differs from the default
* This reduces the parsing that MySQL must do and improves the insert speed
* 即：当插入的列的值和默认值一样时，可以不指定该列，减少Mysql解析列的时间
* 即：如果某列的值出现的比较多，甚至可以设置为默认值
* 我这里表比较简单没有设置默认值
#### change buffer
* SET autocommit=0;
* change buffer会影响磁盘IO次数，关闭自动提交
#### undo log 
* 没找到可以关闭的设置
#### redo log
* Mysql 8.0.21 版本后支持
  ``` sql 
    ## 查看当前状态
    SHOW GLOBAL STATUS LIKE 'Innodb_redo_log_enabled';
    ## 关闭
    ALTER INSTANCE DISABLE INNODB REDO_LOG;
    ## 打开
    ALTER INSTANCE ENABLE INNODB REDO_LOG;
  ```
* 参考：https://dev.mysql.com/doc/refman/8.0/en/innodb-redo-log.html#innodb-disable-redo-logging
* 在使用过程中出现【Communications link failure】异常，未解决
* 建议不要在生产上使用
#### bin log
* 关闭 bin log
``` cnf 
    [mysqld]
    skip-log-bin
```
* 查看：show variables like '%log_bin%';
#### max_allowed_packet
* 给server端发送的数据包的最大值
* 我这边 statement 第一次执行时出现超过最大值，后修改
``` cnf 
[mysqld]
## 100M
max_allowed_packet=104857600
```
#### bulk_insert_buffer_size
* 参考：https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_bulk_insert_buffer_size
* This variable limits the size of the cache tree in bytes per thread
``` cnf 
[mysqld]
## 100M
bulk_insert_buffer_size=104857600
```

## 最后
* 因为我用的是 docker ,在 JDBC 上使用多线程是否有用以及有多大效果还不确定，不知道容器是否有自己的一套，需要后续使用应用程序起mysql服务方式测试
* 看mysql官网应该还有针对 InnoDB 的具体优化参数，可以进一步优化，后续有时间再研究