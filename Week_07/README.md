#学习笔记

##mysql 高可用总结
可以查看  https://juejin.cn/post/6844904039054868493
主从复制  GTID复制  半同步复制 组复制  高可用MMM MHA

## 作业一 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

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
>
>