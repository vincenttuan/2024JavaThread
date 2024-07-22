package lab2.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CarExample2 implements Runnable {
	private CyclicBarrier barrier;
	private CyclicBarrier barrier2;
	
	CarExample2(CyclicBarrier barrier, CyclicBarrier barrier2) {
		this.barrier = barrier;
		this.barrier2 = barrier2;
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
		System.out.println(Thread.currentThread().getName() + "已到嘉義");
		barrier2.await();
		System.out.println(Thread.currentThread().getName() + "往高雄出發");
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		CyclicBarrier barrier = new CyclicBarrier(3, ()->{
			System.out.println("吃太陽餅");
		});
		CyclicBarrier barrier2 = new CyclicBarrier(2, ()->{
			System.out.println("吃雞肉飯");
		});
		
		Thread t1 = new Thread(new CarExample2(barrier, barrier2));
        Thread t2 = new Thread(new CarExample2(barrier, barrier2));
        Thread t3 = new Thread(new CarExample2(barrier, barrier2));
        Thread t4 = new Thread(new CarExample2(barrier, barrier2));
        Thread t5 = new Thread(new CarExample2(barrier, barrier2));
        Thread t6 = new Thread(new CarExample2(barrier, barrier2));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
	}

	
}
