package lab3.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorExample {
    public static void main(String[] args) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 處理任務 " + index);
            });
        }
        singleThreadExecutor.shutdown();
    }
}
