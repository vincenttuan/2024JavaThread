package lab3.pool_best;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {
    public static void main(String[] args) {
        // 一種支持阻塞操作的隊列，用於在多執行緒環境中安全地進行任務的存儲和檢索。
        // BlockingQueue 的典型應用場景包括生產者-消費者模式，執行緒池中的任務隊列等。
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // 生產者執行緒
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.put(i); // 如果隊列滿則阻塞
                    System.out.println("生產者放入：" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消費者執行緒
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer value = queue.take(); // 如果隊列空則阻塞
                    Thread.sleep(10);
                    System.out.println("消費者取出：" + value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
