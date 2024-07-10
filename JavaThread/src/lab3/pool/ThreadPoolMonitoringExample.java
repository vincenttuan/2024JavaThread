package lab3.pool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMonitoringExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;

        // 提交10個任務到執行緒池
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 處理任務 " + index);
                try {
                    Thread.sleep(new Random().nextInt(2000)); // 模擬任務執行時間
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 使用ScheduledExecutorService每500毫秒打印一次執行緒池狀態
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("活躍執行緒數量: " + executor.getActiveCount());
            System.out.println("任務隊列長度: " + executor.getQueue().size());
            System.out.println("已完成的任務數量: " + executor.getCompletedTaskCount());
            System.out.println("總任務數量: " + executor.getTaskCount());
        }, 0, 500, TimeUnit.MILLISECONDS);
        
        // executor 平滑關閉
        executor.shutdown();
        
        try {
            // 等待執行緒池中的所有任務在指定的時間內完成，並在等待時間超過後強制關閉執行緒池
        	// 當程式執行到此會阻塞當前執行緒
        	if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            	// executor 強制關閉
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
        	// executor 強制關閉
            executor.shutdownNow();
        } finally {
            // 關閉 ScheduledExecutorService
            scheduler.shutdown();
            System.out.println("executor 關閉: " + executor.isShutdown());
            System.out.println("scheduler 關閉: " + scheduler.isShutdown());
        }
        
    }
}
