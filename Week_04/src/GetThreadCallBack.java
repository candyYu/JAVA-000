import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class GetThreadCallBack {


    public static void main(String[] args) throws InterruptedException,ExecutionException {


        MyCallback callback = new MyCallback();

        FutureTask<String> futureTask = new FutureTask<String>(callback);

        Thread thread = new Thread(futureTask);

        thread.start();

        String result =  futureTask.get();

        System.out.println(result);

    }



}

class MyCallback implements Callable<String> {


    @Override
    public String call() throws Exception {
        return "获取线程执行结果";
    }
}