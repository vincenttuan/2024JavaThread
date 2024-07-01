package lab.priority;

public class ThreadPriorityExample {

    public static void main(String[] args) {
        // 創建高優先級執行緒
        Thread highPriorityThread = new Thread(new PriorityRunnable(), "High-Priority Thread");
        highPriorityThread.setPriority(Thread.MAX_PRIORITY); // 優先級設為10

        // 創建低優先級執行緒
        Thread lowPriorityThread = new Thread(new PriorityRunnable(), "Low-Priority Thread");
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY + 1); // 優先級設為2

        // 啟動執行緒
        highPriorityThread.start();
        lowPriorityThread.start();
    }
}

class PriorityRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " 正在運行, 優先級: " + Thread.currentThread().getPriority());
            
        }
    }
}
