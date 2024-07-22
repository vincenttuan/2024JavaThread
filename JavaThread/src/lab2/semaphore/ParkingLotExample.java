package lab2.semaphore;

import java.util.concurrent.Semaphore;

// 停車場類
// 在停車場管理系統中，有固定數量的停車位，每次只能允許一定數量的車輛進入停車場。
// 使用 Semaphore 可以控制同時進入停車場的車輛數量，確保停車場不會超載。
class ParkingLot {
	private final Semaphore semaphore;

	// 建構子，初始化停車場容量
	public ParkingLot(int capacity, boolean fair) {
		semaphore = new Semaphore(capacity, fair); // 支持公平性或非公平性
	}

	// 車輛進入停車場
	public void park(String carName) throws InterruptedException {
		System.out.println(carName + " 嘗試進入停車場...");
		semaphore.acquire(); // 獲取許可
		// 模擬車輛停放時間
		//Thread.sleep((long) (Math.random() * 10000));
		System.out.println(carName + " 成功進入停車場");
		semaphore.release(); // 釋放許可
		System.out.println(carName + " 離開停車場");
	}
}

//車輛類
class Car implements Runnable {
	private final ParkingLot parkingLot;
	private final String carName;

	public Car(ParkingLot parkingLot, String carName) {
		this.parkingLot = parkingLot;
		this.carName = carName;
	}

	@Override
	public void run() {
		try {
			parkingLot.park(carName);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}

//停車場管理系統主類
public class ParkingLotExample {
	public static void main(String[] args) {
		// 假設停車場有3個停車位，並設置為公平模式
		ParkingLot parkingLotFair = new ParkingLot(3, true);

		// 創建並啟動多個車輛執行緒
		for (int i = 1; i <= 10; i++) {
			Thread car = new Thread(new Car(parkingLotFair, "車輛" + i));
			car.start();
		}

	}
}
