## 下载
* 下载地址：https://archive.apache.org/dist/shardingsphere/5.0.0-alpha/
* 解压后，下载mysql jdbc驱动，copy至ext-lib/目录下

## 准备
* 创建两个数据库
``` sql
drop database if exists sharding_sphere_proxy_0;
create database sharding_sphere_proxy_0
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;


drop database if exists sharding_sphere_proxy_1;
create database sharding_sphere_proxy_1
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;
```
* 修改配置文件
  * server.yaml（见practice2文件夹）
  * config-sharding.yaml（见practice2文件夹）
* 主要修改点
  * sql-show: true，打开日志
  * 修改url为自己本地数据库连接
  * 修改表名为自己即将创建的表名
  * 修改表的模数为16（因为要建16张表）

## 使用服务
* 启动服务(3310端口)
``` sh
cd /Applications/ShardingSphere-Proxy/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin/bin
./start.sh 3310
```
* 查看ShardingSphere-Proxy服务日志
``` sh
tail -f /Applications/ShardingSphere-Proxy/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin/logs/stdout.log
```
* 进入shardingsphere-proxy虚拟的数据库
``` sh
mysql -h 127.0.0.1 -P3310 -uroot -proot
```

## 测试
* 创建订单表
``` mysql
show databases;
use sharding_db;
create table `order` (
    `id` bigint auto_increment NOT NULL comment '主键',
    `user_id` int NOT NULL comment '用户id',
    `status` int not null comment '状态 0-未支付 1-支付',
    `money` decimal(10,2) comment '金额',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment '订单表'
;
```
执行完成后，发现真实库 sharding_sphere_proxy_0、sharding_sphere_proxy_1 已经分别按后缀0～15创建了16张表

* CRUD操作
* 插入数据
``` sql
insert into `order`(`user_id`, `status`, `money`) values
(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(1, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
,(2, 0, 10000.58)
;
```
* 主库上查看
``` sql
select * from `order`;
+--------------------+---------+--------+----------+
| id                 | user_id | status | money    |
+--------------------+---------+--------+----------+
| 682006235465625616 |       2 |      0 | 10000.58 |
| 682006235465625617 |       2 |      0 | 10000.58 |
| 682006235465625618 |       2 |      0 | 10000.58 |
| 682006235465625619 |       2 |      0 | 10000.58 |
| 682006235465625620 |       2 |      0 | 10000.58 |
| 682006235465625621 |       2 |      0 | 10000.58 |
| 682006235465625622 |       2 |      0 | 10000.58 |
| 682006235465625623 |       2 |      0 | 10000.58 |
| 682006235465625624 |       2 |      0 | 10000.58 |
| 682006235465625625 |       2 |      0 | 10000.58 |
| 682006235465625626 |       2 |      0 | 10000.58 |
| 682006235465625627 |       2 |      0 | 10000.58 |
| 682006235465625628 |       2 |      0 | 10000.58 |
| 682006235465625629 |       2 |      0 | 10000.58 |
| 682006235465625630 |       2 |      0 | 10000.58 |
| 682006235465625631 |       2 |      0 | 10000.58 |
| 682006235465625600 |       1 |      0 | 10000.58 |
| 682006235465625601 |       1 |      0 | 10000.58 |
| 682006235465625602 |       1 |      0 | 10000.58 |
| 682006235465625603 |       1 |      0 | 10000.58 |
| 682006235465625604 |       1 |      0 | 10000.58 |
| 682006235465625605 |       1 |      0 | 10000.58 |
| 682006235465625606 |       1 |      0 | 10000.58 |
| 682006235465625607 |       1 |      0 | 10000.58 |
| 682006235465625608 |       1 |      0 | 10000.58 |
| 682006235465625609 |       1 |      0 | 10000.58 |
| 682006235465625610 |       1 |      0 | 10000.58 |
| 682006235465625611 |       1 |      0 | 10000.58 |
| 682006235465625612 |       1 |      0 | 10000.58 |
| 682006235465625613 |       1 |      0 | 10000.58 |
| 682006235465625614 |       1 |      0 | 10000.58 |
| 682006235465625615 |       1 |      0 | 10000.58 |
+--------------------+---------+--------+----------+
```
* 去各个实际数据库查看，发现各个库的各个表都只插入了一条数据
* 尝试把 user=2 的某条数据更新 user_id=3，看看能不能切换库数据
``` sql
update `order` set user_id = 3 where id = 682006235465625616;
```
* 出现错误 ```ERROR 10002 (C1000): 2Unknown exception: [Can not update sharding key, logic table: [order], column: [user_id].]``` ，说明不能通过更新策略上关联的column去移动真实库数据
* 尝试把 id=682006235465625631 的某条数据更新id=682006235465625632 ，看看能不能切换表数据
``` sql
update `order` set id = 682006235465625632 where id = 682006235465625631;
```
* 出现错误 ```ERROR 10002 (C1000): 2Unknown exception: [Can not update sharding key, logic table: [order], column: [id].]``` ，说明也不能通过更新策略上关联的column去移动真实表数据
* 更新普通字段，成功
``` sql
update `order` set money = 50.44 where id = 682006235465625631;
select * from `order`;
```
* 删除，成功，并且在真实表上删除
``` sql
delete from `order` where id = 682006235465625615;
select * from `order`;
```