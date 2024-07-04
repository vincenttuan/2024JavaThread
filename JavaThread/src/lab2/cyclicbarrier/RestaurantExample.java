package lab2.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class Restaurant {
    private final CyclicBarrier barrier;

    public Restaurant(int numberOfChefs) {
        barrier = new CyclicBarrier(numberOfChefs, () -> System.out.println("所有廚師材料已備齊，開始烹飪"));
    }

    public void waitForMaterial() {
        try {
            System.out.println(Thread.currentThread().getName() + " 等待其他廚師材料到達...");
            barrier.await();
            // 開始烹飪
            System.out.println(Thread.currentThread().getName() + " 開始烹飪");
            System.out.println(Thread.currentThread().getName() + " 烹飪完成");
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Chef implements Runnable {
    private final Restaurant restaurant;
    private final long materialArrivalTime;

    public Chef(Restaurant restaurant, long materialArrivalTime) {
        this.restaurant = restaurant;
        this.materialArrivalTime = materialArrivalTime;
    }

    @Override
    public void run() {
        try {
        	System.out.println(Thread.currentThread().getName() + " 等待材料中...");
            Thread.sleep(materialArrivalTime); // 模擬材料到達的等待時間
            System.out.println(Thread.currentThread().getName() + " 材料已備齊");
            restaurant.waitForMaterial(); // 等待所有材料到達後開始烹飪
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class RestaurantExample {
    public static void main(String[] args) {
        int numberOfChefs = 2;
        Restaurant restaurant = new Restaurant(numberOfChefs);
        Thread chef1 = new Thread(new Chef(restaurant, 2000), "廚師1");
        Thread chef2 = new Thread(new Chef(restaurant, 4000), "廚師2");

        chef1.start();
        chef2.start();
    }
}
