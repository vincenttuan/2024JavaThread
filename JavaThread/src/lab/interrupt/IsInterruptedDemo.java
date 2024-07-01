package lab.interrupt;

public class IsInterruptedDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("執行緒被中斷");
                    break;
                }
                System.out.println("執行緒正在運行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("執行緒在睡眠中被中斷");
                    Thread.currentThread().interrupt(); // 重新設置中斷狀態
                }
            }
        });

        thread.start();

        try {
            Thread.sleep(3000); // 主執行緒等待3秒鐘
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt(); // 中斷執行緒
    }
}

