package lab.group;

public class ThreadGroupInterruptExample {
    public static void main(String[] args) {
        // 創建一個執行緒組
        ThreadGroup group = new ThreadGroup("MyThreadGroup");

        // 創建並添加執行緒到這個執行緒組中
        Thread thread1 = new Thread(group, new MyRunnable(), "Thread-1");
        Thread thread2 = new Thread(group, new MyRunnable(), "Thread-2");
        Thread thread3 = new Thread(group, new MyRunnable(), "Thread-3");

        // 啟動執行緒
        thread1.start();
        thread2.start();
        thread3.start();

        // 讓主執行緒等待一段時間，確保子執行緒正在運行
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中斷執行緒組中的所有執行緒
        System.out.println("主執行緒：中斷執行緒組中的所有執行緒");
        group.interrupt();
    }
}




