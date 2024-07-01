package lab.interrupt;

public class InterruptComparison {
    public static void main(String[] args) {
        // 創建並啟動執行緒
        Thread thread = new Thread(new Task());
        thread.start();

        try {
            // 主執行緒等待2秒鐘，以便讓子執行緒運行
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中斷子執行緒
        System.out.println("主執行緒：中斷子執行緒");
        thread.interrupt();
    }
}

class Task implements Runnable {
    @Override
    public void run() {
        try {
            // 模擬長時間運行的任務
            System.out.println("子執行緒：正在運行");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("子執行緒：在睡眠中被中斷");

            // 使用 isInterrupted() 檢查中斷狀態，不會清除中斷標誌
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("子執行緒：中斷狀態被設置 - isInterrupted 檢查");
            } else {
                System.out.println("子執行緒：中斷狀態未設置 - isInterrupted 檢查");
            }

            // 使用 interrupted() 檢查中斷狀態，會清除中斷標誌
            if (Thread.interrupted()) {
                System.out.println("子執行緒：中斷狀態被設置 - interrupted 檢查");
            } else {
                System.out.println("子執行緒：中斷狀態未設置 - interrupted 檢查");
            }

            // 再次使用 isInterrupted() 檢查中斷狀態，確認 interrupted 是否清除了標誌
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("子執行緒：中斷狀態被設置 - isInterrupted 檢查（第二次）");
            } else {
                System.out.println("子執行緒：中斷狀態未設置 - isInterrupted 檢查（第二次）");
            }
        }
    }
}

