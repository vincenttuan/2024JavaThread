package lab.interrupt;

public class GameResourceLoader {
    public static void main(String[] args) {
        // 創建並啟動資源加載執行緒
        ResourceLoaderTask loaderTask = new ResourceLoaderTask();
        Thread loaderThread = new Thread(loaderTask, "ResourceLoaderThread");
        loaderThread.start();

        try {
            // 主執行緒等待3秒鐘，模擬資源加載時間
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 模擬玩家取消資源加載
        System.out.println("主執行緒：中斷資源加載執行緒");
        loaderThread.interrupt();
    }
}

class ResourceLoaderTask implements Runnable {
    @Override
    public void run() {
        String[] resources = {"Resource1", "Resource2", "Resource3", "Resource4", "Resource5"};
        for (String resource : resources) {
            // 檢查中斷狀態
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("資源加載執行緒：中斷狀態被設置，準備退出");
                break;
            }

            try {
                // 模擬資源加載
                System.out.println("資源加載執行緒：正在加載 " + resource);
                Thread.sleep(1000); // 模擬每個資源加載所需的時間
            } catch (InterruptedException e) {
                System.out.println("資源加載執行緒：在加載 " + resource + " 時被中斷");
                // 重新設置中斷狀態，因為拋出 InterruptedException 會清除中斷狀態
                // 如果我們希望執行緒在捕獲 InterruptedException 後能夠繼續保持中斷狀態，方便後續的中斷檢查，我們需要在捕獲異常後重新設置中斷狀態。
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("資源加載執行緒：資源加載過程結束");
    }
}
