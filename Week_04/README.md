学习笔记

## 作业一

作业要求
2.（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？写出你的方法，越多越好，提交到 Github。

Homework03.java 实现了7种方式

线程之间的协作通信方式
1.基本thread.join  synchronized锁  wait notify机制
2.并发工具包 Lock ReentrantLock CycleBarrier Semaphore
3.线程池   lamanda  Future FutureTask CompleteFuture等


## 作业二
作业要求
4.（必做）把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。 可选工具：xmind，百度脑图，wps，MindManage 或其他。

【脑图】(https://github.com/candyYu/JAVA-000/tree/main/Week_04/并发和多线程.png)

## 线程同步和异步

线程之间需要并发访问同一资源，需要沟通，所以有了线程之间通信，有了同步概念

多核cpu时代，多任务程序，为了让每个程序看起来都得到执行，启动多线程处理，复杂度上升


## 线程基本方法
sleep()  线程不会释放锁，让出cpu时间
join()  线程之间协作 ，发起线程等待当前线程执行完成，一个汇聚点


## 线程生命周期



## synchronized  wait notify notifyAll



## 线程池创建和线程执行获取返回结果

推荐使用new ThreadPoolExecutor 创建


给个示例

```
 public static ThreadPoolExecutor initThreadPoolExecutor() {
        int coreSize =  Runtime.getRuntime().availableProcessors();
        int maxSize = coreSize * 2;
        BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>(500);

        CustomThreadFactory threadFactory = new CustomThreadFactory("tiroly");

        return  new ThreadPoolExecutor(coreSize, maxSize, 1, TimeUnit.MINUTES, workQueue,threadFactory);

    }

```

## 创建固定线程池经验

CPU密集型应用 N N+1
IO密集型应用  2*N  2*N+1


## java并发工具包

Lock

ReentrantLock
ReadWriteLock
LockSupport






