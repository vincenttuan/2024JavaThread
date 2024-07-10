package lab3.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            cachedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 處理任務 " + index);
            });
        }
        cachedThreadPool.shutdown();
    }
}

