package lab.runnable;

public class DaemonThreadDemo {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {
            System.out.println("背景執行緒正在運行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("背景執行緒完成");
        });

        // 將執行緒設置為背景執行緒
        daemonThread.setDaemon(true);
        daemonThread.start();

        System.out.println("主執行緒結束");
        // 主執行緒結束後，背景執行緒也將終止，不會執行完sleep(5000)的全部時間
    }
}

