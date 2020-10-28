# 第一周学习笔记

## HomeWork01
   分析字节码 ，分析HomeWork01.java代码，字节码在HomeWork01.bytecode中
   

## HomeWork02
   自定义了classLoader实现类加载，获取类，执行了其中的Hello方法，详见HomeWork02HelloClassLoader.java
   

## HomeWork03
   绘制参数图关系，详解JVM内存空间使用情况图


## JVM常用参数整理

   1. -XX:+MaxTenuringThreshold=15 超过15次还没有young gc放入老年代

   2. 持久代/元数据区
      1.8之前 -XX:MaxPermSize=256m 1.8之后 -XX:MaxMetaspaceSize=256m

   3. 常用JVM工具命令整理
      -Xms4096m  //初始堆大小

      -Xmx4096m  //最大堆大小

      -Xmn1536m //新生代大小 eden + from + to

      -Xss512K  //线程大小

      -XX:NewRatio=2  //新生代和老年代的比例

      -XX:MaxPermSize=64m   //持久代最大值

      -XX:PermSize=16m  //持久代初始值

      -XX:SurvivorRatio=8  // eden 区和survivor区的比例

      -verbose:gc

      -Xloggc:gc.log  //输出gc日志文件

      -XX:+UseGCLogFileRotation  //使用log文件循环输出

      -XX:NumberOfGCLogFiles=1  //循环输出文件数量

      -XX:GCLogFileSize=8k //日志文件大小限制

      -XX:+PrintGCDateStamps //gc日志打印时间

      -XX:+PrintTenuringDistribution            //查看每次minor GC后新的存活周期的阈值

      -XX:+PrintGCDetails //输出gc明细

      -XX:+PrintGCApplicationStoppedTime //输出gc造成应用停顿的时间

      -XX:+PrintReferenceGC //输出堆内对象引用收集时间

      -XX:+PrintHeapAtGC //输出gc前后堆占用情况

      -XX:+UseParallelGC  //年轻代并行GC，标记-清除

      -XX:+UseParallelOldGC //老年代并行GC，标记-清除

      -XX:ParallelGCThreads=23 //并行GC线程数， cpu<=8?cpu:5*cpu/8+3

      -XX:+UseAdaptiveSizePolicy //默认，自动调整年轻代各区大小及晋升年龄

      -XX:MaxGCPauseMillis=15 //每次GC最大停顿时间,单位为毫秒

      -XX:+UseParNewGC  //Serial多线程版

      -XX:+UseConcMarkSweepGC  //CMS old gc

      -XX:+UseCMSCompactAtFullCollection  //FullGC后进行内存碎片整理压缩

      -XX:CMSFullGCsBeforeCompaction=n  //n次FullGC后执行内存整理

      -XX:+CMSParallelRemarkEnabled  //启用并行重新标记,只适用ParNewGC

      -XX:CMSInitiatingOccupancyFraction=80             //cms作为垃圾回收是，回收比例80%

      -XX:ParallelGCThreads=23 //并行GC线程数，cpu<=8?cpu:5*cpu/8+3

      -XX:-UseSerialGC //默认不启用，client使用时启用

      -XX:+UseG1GC //启用G1收集器

      -XX:-UseAdaptiveSizePolicy //默认，不自动调整各区大小及晋升年龄

      -XX:PretenureSizeThreshold=2097152 //直接晋升到老年代的对象大小

      -XX:MaxTenuringThreshold=15(default) //晋升到老年代的对象年龄，PSGen无效

      -XX:-DisableExplicitGC //禁止在运行期显式地调用?System.gc()

      -XX:+HeapDumpOnOutOfMemoryError  //在OOM时输出堆内存快照

      -XX:HeapDumpPath=./java_pid<pid>.hprof  //堆内存快照的存储路径

      -XX:+CMSScavengeBeforeRemark //执行CMS重新标记之前，尝试执行一此MinorGC

      -XX:+CMSPermGenSweepingEnabled //开启永久代的并发垃圾收集」
      - - - - - - - - - - - - - - -

 5  常用GC 和选用GC经验

    1. 如果系统考虑吞吐优先，CPU资源都用来最大程度处理业务，用Parallel GC;
    2. 如果系统考虑低延迟有限，每次GC时间尽量短，用CMS GC;
    3. 如果系统内存堆较大，同时希望整体来看平均GC时间可控，使用G1 GC。 对于内存大小的考量:
       1. 一般4G以上，算是比较大，用G1的性价比较高。
       2. 一般超过8G，比如16G-64G内存，非常推荐使用G1 GC。

java8默认GC策略：
java9默认GC策略
java10默认GC策略
java11默认GC策略


6.  新型GC ZGC/Shenandoah GC
ZGC

-XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx16g

ZGC最主要的特点包括:
1. GC最大停顿时间不超过10ms
2. 堆内存支持范围广，小至几百MB的堆空间，大至4TB的超大堆内 存(JDK13升至16TB)
3. 与G1相比，应用吞吐量下降不超过15%
4. 当前只支持Linux/x64位平台，JDK15后支持MacOS和Windows 系统


ShennandoahGC

XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -Xmx16g

Shenandoah GC立项比ZGC更早，设计为
GC线程与应用线程并发执行的方式，通过实
现垃圾回收过程的并发处理，改善停顿时间， 使得GC执行线程能够在业务处理线程运行过
程中进行堆压缩、标记和整理，从而消除了
绝大部分的暂停时间。
Shenandoah 团队对外宣称Shenandoah GC的暂停时间与堆大小无关，无论是200 MB 还是 200 GB的堆内存，都可以保障具有 很低的暂停时间(注意:并不像ZGC那样保证 暂停时间在10ms以内)



到目前为止，我们一共了解了Java目前支持的所有GC算法，一共有7类:
1. 串行GC(Serial GC): 单线程执行，应用需要暂停;
2. 并行GC(ParNew、Parallel Scavenge、Parallel Old): 多线程并行地执行垃圾回收， 关注与高吞吐;
3. CMS(Concurrent Mark-Sweep): 多线程并发标记和清除，关注与降低延迟;
4. G1(G First): 通过划分多个内存区域做增量整理和回收，进一步降低延迟;
5. ZGC(Z Garbage Collector): 通过着色指针和读屏障，实现几乎全部的并发执行，几毫 秒级别的延迟，线性可扩展;
6. Epsilon: 实验性的GC，供性能分析使用;
7. Shenandoah: G1的改进版本，跟ZGC类似。



可以看出GC算法和实现的演进路线:
1. 串行->并行:重复利用多核CPU的优势，大幅降低GC暂停时间，提升吞吐量。
2. 并行->并发:不只开多个GC线程并行回收，还将GC操作拆分为多个步骤，让很多繁重的任务和应用线程 一起并发执行，减少了单次GC暂停持续的时间，这能有效降低业务系统的延迟。
3. CMS -> G1: G1可以说是在CMS基础上进行迭代和优化开发出来的，划分为多个小堆块进行增量回收，这 样就更进一步地降低了单次GC暂停的时间
4. G1 -> ZGC: ZGC号称无停顿垃圾收集器，这又是一次极大的改进。ZGC和G1有一些相似的地方，但是底层 的算法和思想又有了全新的突破。

目前绝大部分Java应用系统，堆内存并不大比如2G-4G以内，而且对10ms这种低延迟的GC暂停不敏感，也就 是说处理一个业务步骤，大概几百毫秒都是可以接受的，GC暂停100ms还是10ms没多大区别。另一方面， 系统的吞吐量反而往往是我们追求的重点，这时候就需要考虑采用并行GC。
如果堆内存再大一些，可以考虑G1 GC。如果内存非常大(比如超过16G，甚至是64G、128G)，或者是对 延迟非常敏感(比如高频量化交易系统)，就需要考虑使用本节提到的新GC(ZGC/Shenandoah)。


7.开启GC策略 java启动使用不同的GC策略





