package lab.sleep;

public class SleepExample {
    public static void main(String[] args) {
        System.out.println("主執行緒：開始");

        // 創建並啟動子執行緒
        Thread thread = new Thread(new SleepTask(), "SleepThread");
        thread.start();

        try {
            // 主執行緒休眠2秒鐘
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主執行緒：結束");
    }
}

class SleepTask implements Runnable {
    @Override
    public void run() {
        System.out.println("子執行緒：開始休眠");

        try {
            // 子執行緒休眠3秒鐘
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("子執行緒：在休眠中被中斷");
        }

        System.out.println("子執行緒：結束休眠");
    }
}

