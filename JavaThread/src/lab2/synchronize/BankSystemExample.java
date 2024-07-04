package lab2.synchronize;

// 銀行帳戶類
// 模擬銀行系統中使用 synchronized 關鍵字來確保帳戶操作的執行緒安全性。
// 在這個例子中，每個帳戶都有一個同步機制，確保同一時間只有一個操作能夠進行，例如轉賬或提款。
class BankAccount {
	private double balance; // 帳戶餘額

	// 建構子，初始化帳戶餘額
	public BankAccount(double initialBalance) {
		this.balance = initialBalance;
	}

	// 存款方法
	// 不加上 synchronized，在多執行緒環境中會導致數據競爭（Race Condition）問題
	public synchronized void deposit(double amount, int idx) {
		System.out.print(idx + ":");
		System.out.print(Thread.currentThread().getName() + " 正在存款：" + amount + " ==> ");
		balance += amount; // 增加餘額
		System.out.println(Thread.currentThread().getName() + " 存款後餘額：" + balance);
	}

	// 提款方法
	// 不加上 synchronized，在多執行緒環境中會導致數據競爭（Race Condition）問題
	public synchronized void withdraw(double amount, int idx) {
		System.out.print(idx + ":");
		System.out.print(Thread.currentThread().getName() + " 正在提款：" + amount + " ==> ");
		if (balance >= amount) {
			balance -= amount; // 減少餘額
			System.out.println(Thread.currentThread().getName() + " 提款後餘額：" + balance);
		} else {
			System.out.println(Thread.currentThread().getName() + " 餘額不足($" + balance + ")，無法提款");
		}
	}

	// 獲取帳戶餘額方法
	// 如果在 getBalance() 方法中不加上 synchronized 關鍵字，確實可能會產生髒讀（Dirty Read）問題。
	public synchronized double getBalance() {
		return balance; // 返回餘額
	}
}

//銀行系統範例主類
public class BankSystemExample {
	public static void main(String[] args) {
		BankAccount account = new BankAccount(1000.00); // 創建帳戶並初始化餘額

		// 存款任務
		Runnable depositTask = () -> {
			for (int i = 0; i < 10; i++) {
				account.deposit(150.00, i);
				try {
					Thread.sleep(100); // 模擬其他操作
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};

		// 提款任務
		Runnable withdrawTask = () -> {
			for (int i = 0; i < 10; i++) {
				account.withdraw(400.00, i);
				try {
					Thread.sleep(100); // 模擬其他操作
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};

		// 創建兩個執行緒來執行存款和提款任務
		Thread t1 = new Thread(depositTask, "存款執行緒");
		Thread t2 = new Thread(withdrawTask, "提款執行緒");

		t1.start();
		t2.start();

		// 加入 t1.join() 和 t2.join() 的目的是讓主執行緒等待這兩個執行緒（t1 和 t2）完成後再繼續執行。
		// 這樣可以確保在所有存款和提款操作完成後，再打印最終餘額。
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		System.out.println("最終餘額：" + account.getBalance()); // 打印最終餘額
	}
}