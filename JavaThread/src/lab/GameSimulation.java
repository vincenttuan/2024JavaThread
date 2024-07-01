package lab;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
	多執行緒遊戲示例
	這個示例展示了一個多執行緒遊戲，其中包括玩家操作、背景音樂、AI 控制等多個並發任務。
	背景音樂將作為守護執行緒，一開始就會播放，不受其他執行緒的影響。
	
	遊戲情境
	1. 玩家操作：模擬玩家每隔一段時間執行操作。
	2. 背景音樂：播放背景音樂，作為守護執行緒運行。
	3. AI 控制：AI 執行一系列的行動。
	4. 監控系統：監控遊戲的運行狀態，並在需要時中斷某些執行緒。
	
	GameSimulation 類：
	main 方法中創建並管理所有遊戲相關的執行緒，包含玩家操作執行緒、背景音樂執行緒、AI 執行緒和監控執行緒。
	使用 join 方法等待所有遊戲相關的執行緒完成，並使用 Callable 和 Future 計算遊戲的總運行時間。
	
	PlayerTask 類：
	模擬玩家操作的任務類，每隔一段時間執行一次玩家操作，並在操作完成後打印消息。
	
	MusicTask 類：
	模擬背景音樂播放的任務類，作為守護執行緒一開始就播放背景音樂，並在中斷後打印消息。
	
	AITask 類：
	模擬 AI 行動的任務類，每隔一段時間執行一次 AI 行動，並在行動完成後打印消息。
	
	MonitorTask 類：
	監控遊戲執行緒狀態的任務類，定期打印活動執行緒的狀態，並隨機中斷某個活動執行緒。
	
	GameTimeTask 類：
	計算遊戲總運行時間的任務類，使用 Callable 介面來實現。
	
	這個範例可以幫助我們了解
	(1). 如何創建和管理多個執行緒：使用 Runnable 和 Callable 介面來實現不同的任務。
	(2). 執行緒優先級：設置執行緒的優先級來控制執行緒調度的順序。
	(3). 背景執行緒：創建背景(守護)執行緒來執行背景任務，如背景音樂。
	(4). 使用 ExecutorService 來管理執行緒池：如何使用高級的執行緒管理 API 來簡化並發程式設計。
	(5). 使用 join() 方法進行執行緒協作：等待某些任務完成後再繼續執行後續操作。
	(6). 執行緒中斷：如何安全地中斷執行緒，以及在執行緒被中斷時的處理方法。
	(7). 監控系統：定期監控執行緒的狀態並執行相應操作。

 * */

public class GameSimulation {
    public static void main(String[] args) {
        // 創建一個執行緒組
        ThreadGroup gameGroup = new ThreadGroup("GameGroup");

        // (1) 創建並添加遊戲相關的執行緒到這個執行緒組中
        Thread playerThread = new Thread(gameGroup, new PlayerTask(), "Player-Thread");
        Thread aiThread = new Thread(gameGroup, new AITask(), "AI-Thread");

        // (3) 創建並設置背景音樂執行緒為守護執行緒
        Thread musicThread = new Thread(new MusicTask(), "Music-Thread");
        musicThread.setDaemon(true); // 設置為守護執行緒

        // (2) 設置執行緒優先級
        playerThread.setPriority(Thread.NORM_PRIORITY);
        aiThread.setPriority(Thread.MAX_PRIORITY);

        // 啟動遊戲相關的執行緒
        playerThread.start();
        aiThread.start();
        musicThread.start(); // 一開始就播放背景音樂

        // (7) 創建監控執行緒
        Thread monitorThread = new Thread(new MonitorTask(gameGroup), "Monitor-Thread");
        monitorThread.setDaemon(true); // 設置為守護執行緒
        monitorThread.start();

        try {
            // (5) 使用 join() 等待所有遊戲相關的執行緒完成
            playerThread.join();
            System.out.println(playerThread.getName() + " 已經結束");

            aiThread.join();
            System.out.println(aiThread.getName() + " 已經結束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // (4) 使用 Callable 和 Future 來計算遊戲的總運行時間
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(new GameTimeTask());

        try {
            Long totalTime = future.get();
            System.out.println("遊戲運行完成，總耗時：" + totalTime + " 毫秒");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}

/**
 * (1) 模擬玩家操作的任務類
 */
class PlayerTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " 玩家操作: " + i);
            try {
                Thread.sleep(1000); // 模擬玩家操作時間
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 被中斷"); // (6)
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " 玩家操作完成");
    }
}

/**
 * (1) 模擬背景音樂播放的任務類
 */
class MusicTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + " 播放背景音樂");
            try {
                Thread.sleep(3000); // 模擬背景音樂播放時間
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 背景音樂被中斷"); // (6)
                break;
            }
        }
    }
}

/**
 * (1) 模擬 AI 行動的任務類
 */
class AITask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " AI 行動: " + i);
            try {
                Thread.sleep(1500); // 模擬 AI 行動時間
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " AI 行動被中斷"); // (6)
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " AI 行動完成");
    }
}

/**
 * (7) 監控遊戲執行緒狀態的任務類
 */
class MonitorTask implements Runnable {
    private ThreadGroup group;

    /**
     * 構造方法，初始化監控的執行緒組
     * @param group 要監控的執行緒組
     */
    public MonitorTask(ThreadGroup group) {
        this.group = group;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("監控中... 活動執行緒數量：" + group.activeCount());
            Thread[] threads = new Thread[group.activeCount()];
            group.enumerate(threads);
            for (Thread t : threads) {
                System.out.println("活動執行緒：" + t.getName() + " 優先級：" + t.getPriority());
            }

            // 模擬中斷條件，這裡可以根據具體業務需求設置中斷條件
            if (group.activeCount() > 0 && Math.random() > 0.8) {
                threads[0].interrupt();
                System.out.println("中斷 " + threads[0].getName()); // (6)
            }

            try {
                Thread.sleep(2000); // 每隔2秒監控一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * (4) 計算遊戲總運行時間的任務類
 */
class GameTimeTask implements Callable<Long> {
    @Override
    public Long call() throws Exception {
        long startTime = System.currentTimeMillis();
        Thread.sleep(5000); // 模擬遊戲運行時間
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}



