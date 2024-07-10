package lab2.waitnotify;

class Cookie {
	private boolean empty = true;

	public synchronized void eat(int i, String name) {
		while (empty) {
			try {
				System.out.println(name + " 進入 wait");
				wait();
			} catch (Exception e) {
			}
		}
		System.out.println(name + " 吃第 " + i + " 塊餅乾");
		empty = true;
		// 若使用 notify(); 就有可能發生死結
		// 如果喚醒的執行緒不是正確的類型，則可能會導致所有執行緒都陷入等待狀態，從而造成死結。
		notifyAll(); 
	}

	public synchronized void put(int i, String name) {
		while (!empty) {
			try {
				System.out.println(name + " 進入 wait");
				wait();
			} catch (Exception e) {
			}
		}
		System.out.println(name + " 放第 " + i + " 塊餅乾");
		empty = false;
		// 若使用 notify(); 就有可能發生死結
		// 如果喚醒的執行緒不是正確的類型，則可能會導致所有執行緒都陷入等待狀態，從而造成死結。
		notifyAll(); 
	}
}

class Eat implements Runnable {
	private Cookie cookie;
	private String name;

	public Eat(Cookie cookie, String name) {
		this.cookie = cookie;
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 1; i <= 10; i++) {
			cookie.eat(i, name);
		}
	}
}

class Put implements Runnable {
	private Cookie cookie;
	private String name;

	public Put(Cookie cookie, String name) {
		this.cookie = cookie;
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 1; i <= 10; i++) {
			cookie.put(i, name);
		}
	}
}

// 模擬主人、女主人一邊放餅乾，小狗、貓一邊吃餅乾
public class DogAndCookies {
	public static void main(String[] args) {
		Cookie cookie = new Cookie();
		Thread dog = new Thread(new Eat(cookie, "小狗"));
		Thread cat = new Thread(new Eat(cookie, "貓"));
		Thread master = new Thread(new Put(cookie, "主人"));
		Thread mistress = new Thread(new Put(cookie, "女主人"));

		dog.start();
		cat.start();
		master.start();
		mistress.start();
	}
}
