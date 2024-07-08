package lab2.reentrantlock.game;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 如何使用 ReentrantLock 和 lockInterruptibly() 來設計一個允許中斷等待鎖的高並發系統，
 * 適用於線上遊戲中資源搶奪機制的場景。
 * */
class GameResource {
    private final Lock lock = new ReentrantLock();

    // 獲取資源的方法
    public void acquireResource(String playerName) {
        try {
            System.out.println(playerName + " 嘗試獲取資源...");
            lock.lockInterruptibly(); // 嘗試獲取鎖，允許中斷
            try {
                System.out.println(playerName + " 獲取到了資源！");
                // 模擬使用資源的時間
                Thread.sleep(2000);
            } finally {
                lock.unlock();
                System.out.println(playerName + " 釋放了資源。");
            }
        } catch (InterruptedException e) {
            System.out.println(playerName + " 被中斷，無法獲取資源。");
            handleEmergencyTask(playerName); // 處理緊急任務
            // 在捕獲 InterruptedException 後，中斷狀態會被清除，執行緒將不再處於中斷狀態。
            // 如果我們希望該執行緒仍保持中斷狀態，以便後續的程式碼能夠檢查到這個中斷狀態，我們需要"明確的寫出"重新設置中斷狀態。
            Thread.currentThread().interrupt(); // 恢復中斷狀態
        }
    }

    // 處理緊急任務的方法
    private void handleEmergencyTask(String playerName) {
        System.out.println(playerName + " 正在處理緊急任務...");
        try {
            // 模擬緊急任務處理時間
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(playerName + " 處理緊急任務時被中斷。");
        }
        System.out.println(playerName + " 完成了緊急任務。");
    }
}

// 玩家類，實現 Runnable 接口
class Player implements Runnable {
    private final GameResource resource;
    private final String playerName;

    public Player(GameResource resource, String playerName) {
        this.resource = resource;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        resource.acquireResource(playerName);
    }
}

// 遊戲模擬類
public class GameSimulation {
    public static void main(String[] args) throws InterruptedException {
        GameResource resource = new GameResource();

        Thread player1 = new Thread(new Player(resource, "玩家1"));
        Thread player2 = new Thread(new Player(resource, "玩家2"));
        Thread player3 = new Thread(new Player(resource, "玩家3"));

        player1.start();
        player2.start();
        player3.start();

        // 主執行緒等待一段時間後，中斷 player2 的操作
        Thread.sleep(1000);
        System.out.println("主執行緒：中斷 玩家2 的操作");
        player2.interrupt();
    }
}
