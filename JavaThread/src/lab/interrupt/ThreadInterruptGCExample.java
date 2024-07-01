package lab.interrupt;

public class ThreadInterruptGCExample {
    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable(), "My-Thread");
        thread.start();

        // 等待一段時間後中斷執行緒
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();

        // 等待執行緒結束
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 嘗試清除引用
        thread = null;

        // 建議垃圾回收
        System.gc();

        System.out.println("執行緒已結束，並且引用已清除。");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + " 被中斷");
                break;
            }
            // 模擬執行非阻塞方法
            System.out.println(Thread.currentThread().getName() + " 正在運行");
            try {
                Thread.sleep(500); // 模擬工作
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 在 sleep 時被中斷");
                Thread.currentThread().interrupt(); // 重新設置中斷狀態
                break;
            }
        }
    }
}

