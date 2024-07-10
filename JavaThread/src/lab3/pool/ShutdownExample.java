package lab3.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 處理任務 " + index);
            });
        }

        executor.shutdown();
        try {
        	// 等待執行緒池中的所有任務在指定的時間內完成，並在等待時間超過後強制關閉執行緒池。
        	// 當程式執行到此會阻塞當前執行緒
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        } finally {
			System.out.println("executor 關閉: " + executor.isShutdown());
		}
    }
}

