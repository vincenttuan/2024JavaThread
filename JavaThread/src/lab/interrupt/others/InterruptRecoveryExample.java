package lab.interrupt.others;

/**
 * 中斷與恢復執行緒的示例
 * 這個程式展示了如何在 Java 中使用中斷機制來停止執行緒，並在需要時重新啟動一個新的執行緒。
 * 程式包括兩個主要類別：InterruptRecoveryExample 和 InterruptibleTask。
 * */
public class InterruptRecoveryExample {
    public static void main(String[] args) {
        Thread thread = new Thread(new InterruptibleTask(), "WorkerThread");

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
        /*
         * 在 Java 中，一旦執行緒被中斷（通過 thread.interrupt() 方法），我們可以通過檢查並重置中斷狀態來恢復執行緒的運行。
         * 不過，需要注意的是，interrupt() 方法並不會真正銷毀或終止執行緒，它只是設置一個中斷標誌，告訴執行緒應該停止執行。
         * 執行緒需要自己檢查這個中斷標誌並做出響應。
         * */
        
        try {
            // 主執行緒等待2秒鐘，觀察子執行緒的行為
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 重啟中斷後的子執行緒
        System.out.println("主執行緒：重啟子執行緒");
        thread = new Thread(new InterruptibleTask(), "WorkerThread");
        thread.start();
    }
}

