## 说明
* 见 【src/main/java/cc/gegee/study/week7/practice2】

## 环境
* mysql: docker + mysql + 8.0.24
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

## LOAD DATA 方式插入
* 参考：https://dev.mysql.com/doc/refman/8.0/en/load-data.html
* 7 秒
* 因为我这边用的是 docker，会比本机应用程序会再慢些
``` mysql 
load data local infile '~/Downloads/order.csv' into table test.`order`
    character set utf8 -- 可选，避免中文乱码问题
    fields terminated by ',' -- 字段分隔符，每个字段(列)以什么字符分隔，默认是 \t
    optionally enclosed by '' -- 文本限定符，每个字段被什么字符包围，默认是空字符
    escaped by '/' -- 转义符，默认是 \
    lines terminated by '\r\n' -- 记录分隔符，如字段本身也含\n，那么应先去除，否则load data
    ( `user_id`, `status`, `money`) -- 每一行文本按顺序对应的表字段，建议不要省略
```


## importTable 方式插入
* 参考：https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-utilities-parallel-table.html
* todo

## MySQL Shell 方式插入 
* 参考：https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-utilities.html
* todo

## 优化过程
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
