#学习笔记

##mysql 高可用总结
可以查看  https://juejin.cn/post/6844904039054868493
主从复制  GTID复制  半同步复制 组复制  高可用MMM MHA
https://juejin.cn/post/6844904049540595726

## 作业一 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

见homework01

三种方法 
1. mysql statment insert循环执行
2. 使用statement addBatch执行
3. 使用数据库的loadData 方式

>>> 首先添加用户
> insert into user (`user_name`,`password`,`salt`,`rank`) values("candy2","123456","hils",1);
> insert into product(name,price,state,imgs) values("产品1", 50000, 1, ""),("产品2", 50000, 1, "");
>
>
>一条条插入，非常耗时，我改为了1万执行了154.222秒 好几分钟
>statement batch 快了 1秒搞定
>load file 好快，同样数据1万 2秒搞定
>

搭建mysql 主从复制，我使用了docker-compose来构建

执行start-slave 主从同步
//主数据库执行
CREATE USER 'repl'@'%' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
flush privileges;

mysql> show master status;
mysql> show master status;
+---------------------------+----------+--------------+------------------+----------------------------------------+
| File                      | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set                      |
+---------------------------+----------+--------------+------------------+----------------------------------------+
| replicas-mysql-bin.000003 |      194 |              | mysql            | ad098453-36f7-11eb-9a5b-0242ac120003:1 |
+---------------------------+----------+--------------+------------------+----------------------------------------+




//从库执行
   
    
```
CHANGE MASTER TO
    MASTER_HOST='mysql-master',  
    MASTER_PORT = 3306,
    MASTER_USER='repl',      
    MASTER_PASSWORD='123456',   
    MASTER_LOG_FILE='replicas-mysql-bin.000002',
    MASTER_LOG_POS=154;
   // MASTER_AUTO_POSITION = 1;
```

stop slave;
start slave;

MYSQL在线开启和关闭 GTID https://cloud.tencent.com/developer/article/1418501

## 作业二  读写分离 - 动态切换数据源版本 1.0
见homework02


## 作业三  读写分离 - 数据库框架版本 2.0
见homework03