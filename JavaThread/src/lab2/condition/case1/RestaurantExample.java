package lab2.condition.case1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 餐廳類
// 範例模擬了一個餐廳系統，廚師在等待烹飪材料的到達。一旦材料到達，廚師會立開始烹飪。
// 這個例子展示了如何使用 Condition 來協調多個執行緒的工作流程。
class Restaurant {
    private final Lock lock = new ReentrantLock();
    private final Condition materialArrived = lock.newCondition();
    private boolean materialAvailable = false;

    public void waitForMaterial() throws InterruptedException {
        lock.lock();
        try {
            while (!materialAvailable) {
                System.out.println(Thread.currentThread().getName() + " 等待材料中...");
                materialArrived.await();
            }
            // 開始烹飪
            System.out.println(Thread.currentThread().getName() + " 開始烹飪");
        } finally {
            lock.unlock();
        }
    }

    public void deliverMaterial(String chefName) {
        lock.lock();
        try {
            materialAvailable = true;
            System.out.println(chefName + " 材料已備齊");
            materialArrived.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class Chef implements Runnable {
    private final Restaurant restaurant;
    private final long materialArrivalTime; // 模擬等待材料到達所花費的時間

    public Chef(Restaurant restaurant, long materialArrivalTime) {
        this.restaurant = restaurant;
        this.materialArrivalTime = materialArrivalTime;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " 等待材料中... ==> 廚師 沒事做");
            Thread.sleep(materialArrivalTime); // 模擬材料到達的等待時間
            
            restaurant.deliverMaterial(Thread.currentThread().getName()); // 通知材料已到達
            restaurant.waitForMaterial(); // 等待材料到達後開始烹飪
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class RestaurantExample {
    public static void main(String[] args) throws InterruptedException {
        Restaurant restaurant = new Restaurant();
        Thread chef1 = new Thread(new Chef(restaurant, 2000), "廚師1");
        Thread chef2 = new Thread(new Chef(restaurant, 4000), "廚師2");

        chef1.start();
        chef2.start();
    }
}