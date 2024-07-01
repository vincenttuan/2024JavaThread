package lab.sleep;

public class ResourcePolling {
    private static boolean resourceAvailable = false;

    public static void main(String[] args) {
        Thread pollingThread = new Thread(new PollingTask(), "PollingThread");
        pollingThread.start();

        // 模擬資源在10秒鐘後變為可用
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resourceAvailable = true;
    }

    static class PollingTask implements Runnable {
        @Override
        public void run() {
            while (!resourceAvailable) {
                System.out.println("資源不可用，等待1秒後重新檢查...");
                try {
                    Thread.sleep(1000); // 每隔1秒檢查一次資源
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("資源已可用，開始處理...");
        }
    }
}

