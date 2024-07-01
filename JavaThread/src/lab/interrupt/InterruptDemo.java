package lab.interrupt;

public class InterruptDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("執行緒正在運行: " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("執行緒被中斷");
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

