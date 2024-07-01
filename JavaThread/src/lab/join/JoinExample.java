package lab.join;

public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        // 創建執行緒
        Thread thread1 = new Thread(new Task(), "Thread-1");
        Thread thread2 = new Thread(new Task(), "Thread-2");
        Thread thread3 = new Thread(new Task(), "Thread-3");

        // 啟動執行緒
        thread1.start();
        thread2.start();
        thread3.start();

        // 等待 thread1 結束
        thread1.join();
        System.out.println(thread1.getName() + " 已經結束");

        // 等待 thread2 結束
        thread2.join();
        System.out.println(thread2.getName() + " 已經結束");

        // 等待 thread3 結束
        thread3.join();
        System.out.println(thread3.getName() + " 已經結束");

        System.out.println("所有執行緒已經結束");
    }
}

class Task implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " 正在運行: " + i);
            try {
                Thread.sleep(1000); // 模擬一些工作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
