package lab3.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println("定期任務：" + System.currentTimeMillis());
        }, 1, 3, TimeUnit.SECONDS);
    }
}
