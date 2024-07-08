package lab3.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// 線點餐系統中，顧客下單後，系統可以異步通知廚房準備餐點，並在餐點準備好後通知顧客。
// 使用 CompletableFuture 可以實現這種非阻塞的異步操作。
class OnlineOrderSystem {
    // 顧客下單
    public CompletableFuture<String> placeOrder(String order) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("顧客下單：" + order);
            return order;
        });
    }

    // 廚房準備餐點
    public CompletableFuture<String> prepareOrder(String order) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("廚房正在準備餐點：" + order);
            try {
                Thread.sleep(3000); // 模擬準備餐點所需時間
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "餐點準備完成：" + order;
        });
    }

    // 通知顧客
    public void notifyCustomer(String orderStatus) {
        System.out.println("通知顧客：" + orderStatus);
    }
}

public class OnlineOrderExample {
    public static void main(String[] args) {
        OnlineOrderSystem orderSystem = new OnlineOrderSystem();

        // 顧客下單並異步處理
        CompletableFuture<String> orderFuture = orderSystem.placeOrder("漢堡")
                .thenCompose(orderSystem::prepareOrder) // 異步準備餐點
                .thenApply(orderStatus -> {
                    orderSystem.notifyCustomer(orderStatus);
                    return orderStatus;
                });

        // 主線程可以繼續處理其他任務
        System.out.println("主線程繼續處理其他任務...");

        // 等待訂單處理完成
        try {
            String result = orderFuture.get();
            System.out.println("最終結果：" + result);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
    }
}

