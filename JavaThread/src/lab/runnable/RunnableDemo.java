package lab.runnable;

public class RunnableDemo {
    public static void main(String[] args) {
        // 創建一個實現 Runnable 介面的物件
        MyRunnable myRunnable = new MyRunnable();

        // 創建一個 Thread 物件，並將 Runnable 物件作為參數傳遞給它
        Thread thread = new Thread(myRunnable);

        // 啟動執行緒
        thread.start();
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        // 執行緒的任務程式碼
        System.out.println("執行緒正在運行");
    }
}
