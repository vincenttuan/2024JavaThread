package lab3.pool_best;

import java.util.concurrent.*;

public class ThreadBestPoolExample {
    private static final int N_CPU = Runtime.getRuntime().availableProcessors();
    private static final double U_CPU = 0.8; // 80%
    
    // 假設 I/O 密集型任務的等待時間和計算時間
    private static final double IO_WAITING_TIME = 100; // 100ms
    private static final double IO_COMPUTATION_TIME = 20; // 20ms
    private static final int IO_THREADS = (int) (N_CPU * U_CPU * (1 + IO_WAITING_TIME / IO_COMPUTATION_TIME));

    // 假設 CPU 密集型任務的等待時間和計算時間
    private static final double CPU_WAITING_TIME = 10; // 10ms
    private static final double CPU_COMPUTATION_TIME = 90; // 90ms
    private static final int CPU_THREADS = (int) (N_CPU * U_CPU * (1 + CPU_WAITING_TIME / CPU_COMPUTATION_TIME));

    public static void main(String[] args) {
    	System.out.println("N_CPU CPU核心數量: " + N_CPU);
        System.out.println("I/O 密集型任務建議執行緒數量: " + IO_THREADS);
        System.out.println("CPU 密集型任務建議執行緒數量: " + CPU_THREADS);

        // I/O 密集型任務執行緒池
        ExecutorService ioExecutor = Executors.newFixedThreadPool(IO_THREADS);
        for (int i = 0; i < 10; i++) {
            ioExecutor.submit(new IOTask());
        }
        ioExecutor.shutdown();

        // CPU 密集型任務執行緒池
        ExecutorService cpuExecutor = Executors.newFixedThreadPool(CPU_THREADS);
        for (int i = 0; i < 10; i++) {
            cpuExecutor.submit(new CPUTask());
        }
        cpuExecutor.shutdown();
    }

    static class IOTask implements Runnable {
        @Override
        public void run() {
            System.out.println("執行 I/O 任務的執行緒：" + Thread.currentThread().getName());
            // 模擬 I/O 操作
            try {
                Thread.sleep(100); // 假設 I/O 操作等待 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class CPUTask implements Runnable {
        @Override
        public void run() {
            System.out.println("執行 CPU 任務的執行緒：" + Thread.currentThread().getName());
            // 模擬 CPU 計算操作
            for (int i = 0; i < 1_000_000; i++) {
                double result = Math.sin(i);
            }
        }
    }
}
