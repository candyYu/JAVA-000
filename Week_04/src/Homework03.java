import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by candy on 2020/11/11.
 */
public class Homework03 {

    public static void main(String[] args) {

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