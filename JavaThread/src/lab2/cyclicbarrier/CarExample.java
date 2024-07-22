package lab2.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CarExample implements Runnable {
	private CyclicBarrier barrier;
	
	CarExample(CyclicBarrier barrier) {
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		try {
			play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play() throws Exception {
		Thread.sleep(new Random().nextInt(3000));
		System.out.println(Thread.currentThread().getName() + "已到台中");
		barrier.await();
		System.out.println(Thread.currentThread().getName() + "往高雄出發");
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		CyclicBarrier barrier = new CyclicBarrier(5, ()->{
			System.out.println("吃中飯");
		});
		
		Thread t1 = new Thread(new CarExample(barrier));
        Thread t2 = new Thread(new CarExample(barrier));
        Thread t3 = new Thread(new CarExample(barrier));
        Thread t4 = new Thread(new CarExample(barrier));
        Thread t5 = new Thread(new CarExample(barrier));
        Thread t6 = new Thread(new CarExample(barrier));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
	}

	
}
