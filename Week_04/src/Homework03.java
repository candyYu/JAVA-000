import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by candy on 2020/11/11.
 */
public class Homework03 {

    public static void main(String[] args) {
//        method1(); //使用线程futureTask
//        method2();
//          method3();
//        method4();
//        method5();
//        method6();
        method7();


    }

    public static  void method1() {
        ExecutorService executorService = initThreadPoolExecutor();
        try {

            long start = System.currentTimeMillis();
            // 在这里创建一个线程或线程池，
            // 异步执行 下面方法
            Future<Integer> future = executorService.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    return sum();
                }
            });

            Future<Integer> future2 = executorService.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    return 12;
                }
            });



            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + future.get(1, TimeUnit.SECONDS));

            System.out.println("异步计算结果为：" + future2.get());

            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        }catch (InterruptedException | ExecutionException | TimeoutException ex){

        }finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

        // 然后退出main线程
    }


    public static  void method2() {

        try {

            long start = System.currentTimeMillis();

            CountDownLatch countDownLatch = new CountDownLatch(1);

            final int[] sum = {0};

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sum[0] = sum();
                    countDownLatch.countDown();
                }
            });

            thread.start();

            countDownLatch.await();

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + sum[0]);


            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        }catch (InterruptedException ex){

        }

        // 然后退出main线程
    }

    public static  void method3() {

        try {

            long start = System.currentTimeMillis();

            final int[] sum = {0};

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sum[0] = sum();
                }
            });

            thread.start();


            thread.join();

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + sum[0]);



            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        }catch (InterruptedException ex){

        }

        // 然后退出main线程
    }


    public static  void method4() {

        try {

            long start = System.currentTimeMillis();

            final int[] sum = {0};

            final Object object = new Object();

            final Thread mainThread = Thread.currentThread();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sum[0] = sum();
                    LockSupport.unpark(mainThread);
                }
            });

            thread.start();


           LockSupport.park(object);

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + sum[0]);



            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        }catch (Exception ex){

        }

        // 然后退出main线程
    }

    public static  void method5() {

        try {

            long start = System.currentTimeMillis();

            final int[] sum = {0};

            CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
                @Override
                public void run() {
                    // 确保  拿到result 并输出
                    System.out.println("异步计算结果为：" + sum[0]);
                    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
                }
            });

            final Thread mainThread = Thread.currentThread();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    sum[0] = sum();
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            thread.start();




        }catch (Exception ex){

        }

        // 然后退出main线程
    }

    public static  void method6() {

        try {

            long start = System.currentTimeMillis();

            final int[] sum = {0};

            Semaphore semaphore = new Semaphore(1);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        sum[0] = sum();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }

                }
            });

            thread.start();


            while (true) {
                try {
                    semaphore.acquire();

                    System.out.println("异步计算结果为：" + sum[0]);
                    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

                    break;

                }finally {
                    semaphore.release();
                }
            }




        }catch (Exception ex){

        }

        // 然后退出main线程
    }

    public static  void method7() {

        try {

            long start = System.currentTimeMillis();


            FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return sum();
                }
            });

            Thread thread = new Thread(futureTask);
            thread.start();


            System.out.println("异步计算结果为：" + futureTask.get());
            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");


            start = System.currentTimeMillis();
            CompletableFuture<Integer> newFuture = CompletableFuture.supplyAsync(()->{ return sum(); });

            System.out.println("异步计算结果为：" + newFuture.get());
            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");






        }catch (Exception ex){

        }

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }


    public static ThreadPoolExecutor initThreadPoolExecutor() {
        int coreSize =  Runtime.getRuntime().availableProcessors();
        int maxSize = coreSize * 2;
        BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>(500);

        CustomThreadFactory threadFactory = new CustomThreadFactory("tiroly");

        return  new ThreadPoolExecutor(coreSize, maxSize, 1, TimeUnit.MINUTES, workQueue,threadFactory);

    }

}

class  CustomThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;
    private final boolean daemon;

    public CustomThreadFactory(String namePrefix, boolean daemon) {
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    public CustomThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + "-thread-" + threadNumber.getAndIncrement(), 0);
        t.setDaemon(daemon);
        return t;
    }
}