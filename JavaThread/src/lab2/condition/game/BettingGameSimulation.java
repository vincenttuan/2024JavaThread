package lab2.condition.game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 ReentrantLock 和 Condition 來設計一個多執行緒的生產者-消費者系統，
 * 適用於線上博弈遊戲中玩家提交下注和遊戲服務器處理下注的場景。
 * 並於 2 秒後停止接受新的下注
 * */

// 資源類，代表遊戲中的下注隊列
class BettingQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<String> queue = new LinkedList<>();
    private boolean acceptingBets = true;

    // 提交下注請求的方法
    public void placeBet(String bet) {
        lock.lock();
        try {
            if (acceptingBets) {
                queue.add(bet);
                System.out.println(Thread.currentThread().getName() + " 提交了下注：" + bet);
                notEmpty.signal(); // 通知消費者有新的下注請求
            } else {
                System.out.println(Thread.currentThread().getName() + " 無法提交下注：" + bet + "，下注已截止");
            }
        } finally {
            lock.unlock();
        }
    }

    // 處理下注請求的方法
    public void processBet() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " 等待新的下注請求...");
                notEmpty.await();
            }
            String bet = queue.poll();
            System.out.println(Thread.currentThread().getName() + " 處理了下注：" + bet);
        } finally {
            lock.unlock();
        }
    }

    // 停止接受下注的方法
    public void stopAcceptingBets() {
        lock.lock();
        try {
            acceptingBets = false;
            System.out.println("停止接受新的下注");
        } finally {
            lock.unlock();
        }
    }
}

// 生產者類，模擬玩家提交下注
class Player implements Runnable {
    private final BettingQueue bettingQueue;

    public Player(BettingQueue bettingQueue) {
        this.bettingQueue = bettingQueue;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            bettingQueue.placeBet("下注 " + i);
            try {
                Thread.sleep(1000); // 模擬玩家提交下注的時間間隔
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// 消費者類，模擬遊戲服務器處理下注
class GameServer implements Runnable {
    private final BettingQueue bettingQueue;

    public GameServer(BettingQueue bettingQueue) {
        this.bettingQueue = bettingQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                bettingQueue.processBet();
                Thread.sleep(500); // 模擬處理下注的時間
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 遊戲模擬類
public class BettingGameSimulation {
    public static void main(String[] args) throws InterruptedException {
        BettingQueue bettingQueue = new BettingQueue();

        Thread player1 = new Thread(new Player(bettingQueue), "玩家1");
        Thread player2 = new Thread(new Player(bettingQueue), "玩家2");
        Thread gameServer = new Thread(new GameServer(bettingQueue), "遊戲服務器");

        player1.start();
        player2.start();
        gameServer.start();

        // 2 秒後停止接受新的下注
        Thread.sleep(2000);
        bettingQueue.stopAcceptingBets();
    }
}
