package lab2.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 使用 CyclicBarrier 來設計一個並行計算系統，
 * 要求每個階段所有參與的執行緒都必須達到屏障點才能進入下一階段。
 * 共 3 個階段, 每一個階段參與的執行緒有 4 條
 * */

// 計算任務類，模擬並行計算
class ComputationTask implements Runnable {
    private final CyclicBarrier barrier;
    private final int taskId;

    public ComputationTask(CyclicBarrier barrier, int taskId) {
        this.barrier = barrier;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        try {
        	// 模擬 3 個階段計算
            for (int i = 1; i <= 3; i++) {
                // 模擬計算過程
                System.out.println("執行緒 " + taskId + " 正在進行第 " + i + " 階段計算...");
                Thread.sleep((int)(Math.random() * 1000) + 500); // 模擬計算時間

                // 等待其他執行緒到達屏障點
                System.out.println("執行緒 " + taskId + " 完成第 " + i + " 階段計算，等待其他執行緒...");
                barrier.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            System.out.println("執行緒 " + taskId + " 被中斷。");
        }
    }
}

// 並行計算系統主類
public class ParallelComputationSystem {
    public static void main(String[] args) {
        final int numberOfThreads = 4;
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads, () -> {
            System.out.println("所有執行緒完成一個階段，進入下一階段");
        });

        for (int i = 1; i <= numberOfThreads; i++) {
            new Thread(new ComputationTask(barrier, i)).start();
        }
    }
}

