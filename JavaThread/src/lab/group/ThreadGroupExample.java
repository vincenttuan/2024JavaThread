package lab.group;

public class ThreadGroupExample {
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

        // 列出執行緒組中的所有活動執行緒
        Thread[] threads = new Thread[group.activeCount()];
        // 將執行緒組中所有活動的（即尚未終止的）執行緒複製到指定的陣列中
        group.enumerate(threads);

        for (Thread t : threads) {
            System.out.println("活動執行緒：" + t.getName());
        }
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " 正在運行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 被中斷");
                break;
            }
        }
    }
}

