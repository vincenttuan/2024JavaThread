package lab.sleep;

public class PeriodicTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("執行定時任務");
            try {
                Thread.sleep(2000); // 每隔2秒執行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new PeriodicTask(), "PeriodicTaskThread");
        thread.start();
    }
}

