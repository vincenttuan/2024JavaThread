package lab2.waitnotify;

class Cookie {
	private boolean empty = true;
	
	public synchronized void eat(int i) {
		while(empty) {
			try {
				wait();
			} catch (Exception e) {
			
			}
		}
		System.out.println("小狗吃第" + i + "塊餅乾");
		empty = true;
		notify();
	}
	
	public synchronized void put(int i) {
		if(!empty) {
			try {
				wait();
			} catch (Exception e) {
			}
		}
		System.out.println("主人放第" + i + "塊餅乾");
		empty = false;
		notify();
	}
}

class Eat implements Runnable {
	private Cookie cookie;
	public Eat(Cookie cookie) {
		this.cookie = cookie;
	} 
	@Override
	public void run() {
		for(int i=1;i<=10;i++) {
			cookie.eat(i);
		}
	}
}

class Put implements Runnable {
	private Cookie cookie;
	public Put(Cookie cookie) {
		this.cookie = cookie;
	} 
	@Override
	public void run() {
		for(int i=1;i<=10;i++) {
			cookie.put(i);
		}
	}
}

// 模擬主人一邊放餅乾，小狗一邊吃餅乾
public class DogAndCookies {
	public static void main(String[] args) {
		Cookie cookie = new Cookie();
		Thread dog = new Thread(new Eat(cookie));
		Thread master = new Thread(new Put(cookie));
		dog.start();
		master.start();
	}
}
