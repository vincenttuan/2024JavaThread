package lab2.condition.case2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 餐廳類
// 範例模擬了一個餐廳系統，其中有一個廚師在等待烹飪材料的到達。在材料到達之前，廚師會進行洗碗操作，一旦材料到達，廚師會立即停止洗碗並開始烹飪。
// 這個例子展示了如何使用 Condition 來協調多個執行緒的工作流程，使得資源能夠得到更有效的利用。
class Restaurant {
    private final Lock lock = new ReentrantLock();
    private final Condition materialArrived = lock.newCondition();
    boolean materialAvailable = false;

    // 廚師等待材料的方法
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

    // 材料到達通知的方法
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

    // 廚師洗碗的方法
    public void washDishes() throws InterruptedException {
        Thread.sleep(500); // 模擬洗碗所需的時間
        System.out.print(Thread.currentThread().getName() + " 等待材料中... ==> ");
        System.out.println(Thread.currentThread().getName() + " 正在洗碗...");
    }
}

// 廚師類
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
        	// 模擬材料到達的等待時間
        	// --------------------------------------------------------
        	// Thread.sleep(materialArrivalTime); // 沒事做
        	// 等待時不要沒事做，而要去洗碗
            long startTime = System.currentTimeMillis();
            long endTime = startTime + materialArrivalTime;
            while (System.currentTimeMillis() < endTime) {
                restaurant.washDishes(); // 材料未到達期間不斷洗碗
            }
            // --------------------------------------------------------
            
            // 通知材料已到達
            restaurant.deliverMaterial(Thread.currentThread().getName());
            
            // 等待材料到達並開始烹飪
            restaurant.waitForMaterial();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 餐廳系統主類
public class RestaurantExample {
    public static void main(String[] args) throws InterruptedException {
        Restaurant restaurant = new Restaurant();
        Thread chef = new Thread(new Chef(restaurant, 2000), "廚師");

        chef.start();
    }
}
