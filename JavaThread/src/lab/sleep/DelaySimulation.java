package lab.sleep;

public class DelaySimulation {
    public static void main(String[] args) {
        System.out.println("開始模擬延遲操作");
        try {
            Thread.sleep(5000); // 模擬5秒鐘的延遲
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("模擬延遲操作結束");
    }
}

