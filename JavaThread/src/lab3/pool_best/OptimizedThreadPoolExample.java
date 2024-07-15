package lab3.pool_best;

import java.util.concurrent.*;

public class OptimizedThreadPoolExample {
    public static void main(String[] args) {
        // 創建一個有界隊列，用於存放待執行的任務，隊列大小為 10
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        
        // 自定義的 ThreadFactory，用於創建執行緒
        ThreadFactory customThreadFactory = new CustomThreadFactory();
        
        // 創建一個自定義的 ThreadPoolExecutor
        ExecutorService executor = new ThreadPoolExecutor(
                5, // 核心執行緒數量，執行緒池始終保持至少這麼多執行緒，即使它們處於空閒狀態
                10, // 最大執行緒數量，當任務數量超過核心數量並且隊列已滿時，會創建新的執行緒直到達到此數量
                60, // 當執行緒數量超過核心數量時，多餘的空閒執行緒的存活時間
                TimeUnit.SECONDS, // 存活時間的單位
                queue, // 任務隊列，用於存放等待執行的任務
                customThreadFactory // 自定義的 ThreadFactory
        );

        // 生產者執行緒
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    Runnable task = new Task(i);
                    queue.put(task); // 如果隊列滿則阻塞
                    System.out.println("生產者放入任務：" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();

        // 消費者由執行緒池處理
        for (int i = 0; i < 10; i++) {
            executor.submit(new Consumer(queue));
        }

        // 等待生產者執行緒結束
        try {
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 啟動有序關閉：不再接受新任務，但會繼續執行已提交的任務，防止執行緒洩漏
        executor.shutdown();
        try {
            // 等待所有任務完成，最多等待 60 秒，防止執行緒洩漏
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // 若超過等待時間，強制關閉所有任務，防止執行緒洩漏
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // 捕獲中斷異常並強制關閉，防止執行緒洩漏
            executor.shutdownNow();
            // 重新設置中斷狀態
            Thread.currentThread().interrupt();
        }
    }

    static class Task implements Runnable {
        private final int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("執行任務的執行緒：" + Thread.currentThread().getName() + "，任務ID：" + id);
            // 模擬任務處理，這裡可以替換成實際任務代碼
            try {
                Thread.sleep(1000); // 模擬任務執行時間
            } catch (InterruptedException e) {
                // 設置中斷狀態
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Runnable> queue;

        public Consumer(BlockingQueue<Runnable> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Runnable task = queue.take(); // 如果隊列空則阻塞
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class CustomThreadFactory implements ThreadFactory {
        private int count = 0;

        @Override
        public Thread newThread(Runnable r) {
            // 創建一個新執行緒，並設置其名稱和為守護執行緒
            Thread thread = new Thread(r);
            thread.setName("CustomThread-" + count++);
            thread.setDaemon(true); // 設置為守護執行緒
            return thread;
        }
    }
}
