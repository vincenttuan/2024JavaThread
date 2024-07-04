package lab2.countdownlatch;

import java.util.concurrent.CountDownLatch;

// 模擬學生旅遊場景，使用 CountDownLatch 來確保所有學生都上了遊覽車後才能開車。

// 學生類
class Student implements Runnable {
 private final CountDownLatch latch;
 private final String studentName;

 public Student(CountDownLatch latch, String studentName) {
     this.latch = latch;
     this.studentName = studentName;
 }

 @Override
 public void run() {
     try {
         // 模擬學生上車過程
         System.out.println(studentName + " 正在上車...");
         Thread.sleep((long) (Math.random() * 5000)); // 模擬上車所需的時間
         System.out.println(studentName + " 已上車");
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
     } finally {
         latch.countDown(); // 學生上車完成，計數器減1
     }
 }
}

//遊覽車類
class TourBus {
 private final CountDownLatch latch;

 public TourBus(int numberOfStudents) {
     this.latch = new CountDownLatch(numberOfStudents);
 }

 public void waitForAllToBoard() {
     try {
         System.out.println("等待所有學生上車...");
         latch.await(); // 等待所有學生上車
         System.out.println("所有學生已上車，遊覽車開始出發！");
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
     }
 }

 public CountDownLatch getLatch() {
     return latch;
 }
}

//旅遊活動主類
public class SchoolTrip {
 public static void main(String[] args) {
     int numberOfStudents = 5;
     System.out.println("總共有: " + numberOfStudents + " 位學生");
     TourBus tourBus = new TourBus(numberOfStudents);

     // 創建並啟動多個學生執行緒
     for (int i = 1; i <= numberOfStudents; i++) {
         new Thread(new Student(tourBus.getLatch(), "學生" + i)).start();
     }

     // 遊覽車等待所有學生上車
     tourBus.waitForAllToBoard();
 }
}

