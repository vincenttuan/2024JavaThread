package lab3.pool_best;

import java.util.concurrent.*;

public class ThreadLeakExample {
    public static void main(String[] args) {
        // 創建一個固定大小為 5 的執行緒池
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            // 提交 10 個任務到執行緒池
            for (int i = 0; i < 10; i++) {
                executor.submit(new Task());
            }
        } finally {
            // 1. 啟動有序關閉：不再接受新任務，但會繼續執行已提交的任務，防止執行緒洩漏
            executor.shutdown();
            try {
                // 2. 等待所有任務完成，最多等待 60 秒，防止執行緒洩漏
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    // 3. 若超過等待時間，強制關閉所有任務，防止執行緒洩漏
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // 4. 捕獲中斷異常並強制關閉，防止執行緒洩漏
                executor.shutdownNow();
                // 5. 重新設置中斷狀態
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("執行任務的執行緒：" + Thread.currentThread().getName());
            // 模擬任務處理，這裡可以替換成實際任務代碼
            try {
                Thread.sleep(1000); // 模擬任務執行時間
            } catch (InterruptedException e) {
                // 設置中斷狀態
                Thread.currentThread().interrupt();
            }
        }
    }
}
