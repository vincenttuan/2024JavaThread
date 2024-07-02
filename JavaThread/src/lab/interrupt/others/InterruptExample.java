package lab.interrupt.others;

/**
 * 中斷執行緒的示例
 * 這個程式展示了如何在 Java 中使用中斷機制來停止一個執行緒。程式包括兩個主要類別：InterruptExample 和 InterruptibleTask。
 * */
public class InterruptExample {
    public static void main(String[] args) {
        // 創建一個執行緒，該執行緒會檢查中斷狀態並進行處理
        Thread thread = new Thread(new InterruptibleTask(), "WorkerThread");

        // 啟動執行緒
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

class InterruptibleTask implements Runnable {
	private int count = 10;
    @Override
    public void run() {
        for(int i=0;i<count;i++) {
            // 檢查中斷狀態
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("子執行緒：中斷狀態被設置，準備退出");
                break; // 退出循環，結束執行緒
            }

            System.out.println("子執行緒：正在運行");

            try {
                // 模擬任務執行時間
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("子執行緒：在睡眠中被中斷");
                // 重新設置中斷狀態，因為拋出 InterruptedException 會清除中斷狀態
                // 如果我們希望執行緒在捕獲 InterruptedException 後能夠繼續保持中斷狀態，
                // 方便後續的中斷檢查，我們需要在捕獲異常後重新設置中斷狀態。
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("子執行緒：已退出");
    }
}

