package lab.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo {
    public static void main(String[] args) {
        // 創建一個 Callable 物件
        MyCallable myCallable = new MyCallable();

        // 創建一個 ExecutorService 物件，用於管理執行緒
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 提交 Callable 任務，並返回 Future 物件
        Future<String> future = executorService.submit(myCallable);

        try {
            // 獲取 Callable 任務的結果
            String result = future.get();
            System.out.println("Callable 返回的結果: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 關閉 ExecutorService
            executorService.shutdown();
        }
    }
}

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        // 執行緒的任務程式碼
        return "執行緒的結果";
    }
}

