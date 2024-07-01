package lab.sleep;

public class HeartbeatMonitor implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("發送心跳檢查");
            try {
                Thread.sleep(5000); // 每隔5秒發送一次心跳檢查
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new HeartbeatMonitor(), "HeartbeatMonitorThread");
        thread.start();
    }
}

