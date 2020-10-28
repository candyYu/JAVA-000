学习笔记

# 作业一
使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例

1.串行GC

命令：java -XX:+UseSerialGC -Xmx512m -Xms512m -Xloggc:serial.gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

结果： 正在执行...
   执行结束!共生成对象次数:5768

具体日志
   [串行日志](https://github.com/candyYu/JAVA-000/tree/main/Week_02/serial.gc.demo.log)

2.并行GC

命令：java -XX:+UseParallelGC -Xmx512m -Xms512m -Xloggc:parallel.gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

结果：正在执行...
执行结束!共生成对象次数:6211

具体日志
   [并行日志](https://github.com/candyYu/JAVA-000/tree/main/Week_02/parallel.gc.demo.log)

3.CMS GC

命令：java -XX:+UseConcMarkSweepGC -Xmx512m -Xms512m -Xloggc:cms.gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

结果：正在执行...
    执行结束!共生成对象次数:7549

具体日志
   [CMS日志](https://github.com/candyYu/JAVA-000/tree/main/Week_02/cms.gc.demo.log)


4.G1 GC

命令：java -XX:+UseG1GC -Xmx512m -Xms512m -Xloggc:g1.gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

结果：正正在执行...
    执行结束!共生成对象次数:5412

具体日志
   [G1日志](https://github.com/candyYu/JAVA-000/tree/main/Week_02/g1.gc.demo.log)


# 作业二
使用压测工具(wrk或sb)，演练gateway-server-0.0.1-SNAPSHOT.jar 示例

首先启动 跑起来了 java  -jar -Xms512m -Xmx512m gateway-server-0.0.1-SNAPSHOT.jar

wrk -t 10 -c 100 -d 60  http://localhost:8088/api/hello

Running 1m test @ http://localhost:8088/api/hello
  10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.93ms   49.06ms 547.26ms   90.01%
    Req/Sec     0.89k   469.11     2.12k    58.68%
  531083 requests in 1.00m, 63.41MB read
Requests/sec:   8837.03
Transfer/sec:      1.06MB

加上了 --latency

wrk -t 5 -c 100 -d 60  http://localhost:8088/api/hello


Running 1m test @ http://localhost:8088/api/hello
  5 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.08ms   34.18ms 501.36ms   87.71%
    Req/Sec     2.76k     1.54k   11.45k    72.58%
  809913 requests in 1.00m, 96.70MB read
Requests/sec:  13484.54
Transfer/sec:      1.61MB


wrk  -c 100 -d 60 --latency  http://localhost:8088/api/hello
Running 1m test @ http://localhost:8088/api/hello
  2 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.62ms   18.93ms 339.55ms   95.25%
    Req/Sec     8.65k     3.38k   17.72k    66.93%
  Latency Distribution
     50%    3.45ms
     75%    5.30ms
     90%   11.54ms
     99%  102.66ms
  1006384 requests in 1.00m, 120.15MB read
Requests/sec:  16747.84
Transfer/sec:      2.00MB


把进程使用G1GC启动看并发效果
java  -jar -XX:+UseG1GC  -Xms512m -Xmx512m gateway-server-0.0.1-SNAPSHOT.jar
wrk  -c 100 -d 60 --latency  http://localhost:8088/api/hello


Running 1m test @ http://localhost:8088/api/hello
  2 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    21.88ms   48.00ms 692.67ms   92.27%
    Req/Sec     4.69k     2.48k   12.59k    56.42%
  Latency Distribution
     50%    6.85ms
     75%   13.45ms
     90%   49.70ms
     99%  256.35ms
  556262 requests in 1.00m, 66.41MB read
  Socket errors: connect 0, read 0, write 1, timeout 0
Requests/sec:   9256.52
Transfer/sec:      1.11MB

效果还是比较明显的，符合并行GC 比 CMS 或 G1 能支持更高的吞吐量 相比QPS更高一些
下面介绍下wrk工具的使用

首先给出wrk的参数说明：

参数说明
-c：总的连接数（每个线程处理的连接数=总连接数/线程数）
-d：测试的持续时间，如2s(2second)，2m(2minute)，2h(hour)，默认为s
-t：需要执行的线程总数，默认为2，一般线程数不宜过多. 核数的2到4倍足够了. 多了反而因为线程切换过多造成效率降低
-s：执行Lua脚本，这里写lua脚本的路径和名称，后面会给出案例
-H：需要添加的头信息，注意header的语法，举例，-H “token: abcdef”
—timeout：超时的时间
—latency：显示延迟统计信息

返回结果
Latency：响应时间
Req/Sec：每个线程每秒钟的执行的连接数
Avg：平均
Max：最大
Stdev：标准差
+/- Stdev： 正负一个标准差占比
Requests/sec：每秒请求数（也就是QPS），等于总请求数/测试总耗时
Latency Distribution，如果命名中添加了—latency就会出现相关信息


