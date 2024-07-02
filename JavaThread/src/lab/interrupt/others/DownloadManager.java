package lab.interrupt.others;

/**
 * 下載管理器程式
 * 這個程式模擬了一個簡單的下載管理器，它展示了如何在多執行緒環境下進行下載任務，
 * 以及如何中斷下載執行緒。
 * 程式包括兩個主要類別：DownloadManager 和 DownloadTask。
 * */
public class DownloadManager {
    public static void main(String[] args) {
        // 創建並啟動下載執行緒
        DownloadTask downloadTask = new DownloadTask("file.zip");
        Thread downloadThread = new Thread(downloadTask, "DownloadThread");
        downloadThread.start();

        try {
            // 主執行緒等待1秒鐘，模擬用戶操作時間
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 用戶決定取消下載
        System.out.println("主執行緒：中斷下載執行緒");
        downloadThread.interrupt();
    }
}

class DownloadTask implements Runnable {
    private String fileName;
    private int totalSize = 100; // 模擬文件總大小（單位：KB）

    public DownloadTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        int downloadedSize = 0; // 已下載大小

        while (downloadedSize < totalSize) {
            // 檢查中斷狀態
            //if (Thread.currentThread().isInterrupted()) {
            if (Thread.currentThread().interrupted()) {
                        System.out.println("下載執行緒：中斷狀態被設置，準備退出");
                // 可選：保存當前進度到一個文件，供以後恢復下載
                break;
            }

            try {
                // 模擬下載數據
                Thread.sleep(100); // 模擬每次下載所需的時間
                downloadedSize += 10; // 每次下載10KB
                System.out.println("下載執行緒：已下載 " + downloadedSize + "KB");
            } catch (InterruptedException e) {
                System.out.println("下載執行緒：在睡眠中被中斷");
                // 重新設置中斷狀態，因為拋出 InterruptedException 會清除中斷狀態
                // 如果我們希望執行緒在捕獲 InterruptedException 後能夠繼續保持中斷狀態，方便後續的中斷檢查，我們需要在捕獲異常後重新設置中斷狀態。
                Thread.currentThread().interrupt();
            }
        }

        if (downloadedSize >= totalSize) {
            System.out.println("下載執行緒：下載完成 " + fileName);
        } else {
            System.out.println("下載執行緒：下載已取消 " + fileName);
        }
    }
}

