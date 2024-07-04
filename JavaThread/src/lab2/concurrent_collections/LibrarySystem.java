package lab2.concurrent_collections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 使用 Concurrent Collections 來實現圖書館借書系統的多執行緒操作，
// 確保多個讀者同時操作圖書館數據時不會發生數據競爭，從而保證數據的一致性和正確性
class Library {
    // 使用 ConcurrentHashMap 來存儲書籍借閱狀態
    private final ConcurrentHashMap<String, Boolean> books = new ConcurrentHashMap<>();
    // 使用 ConcurrentLinkedQueue 來存儲借書請求
    private final ConcurrentLinkedQueue<String> borrowRequests = new ConcurrentLinkedQueue<>();

    public Library() {
        // 初始化圖書館書籍
        books.put("書籍1", true);
        books.put("書籍2", true);
        books.put("書籍3", true);
        books.put("書籍4", true);
        books.put("書籍5", true);
    }

    // 借書方法
    public void borrowBook(String bookName) {
        borrowRequests.add(bookName);
    }

    // 處理借書請求
    public void processBorrowRequests() {
        while (!borrowRequests.isEmpty()) {
            String bookName = borrowRequests.poll();
            if (bookName != null && books.getOrDefault(bookName, false)) {
                books.put(bookName, false);
                System.out.println(Thread.currentThread().getName() + " 借閱了 " + bookName);
            } else {
                System.out.println(Thread.currentThread().getName() + " 嘗試借閱 " + bookName + " 但已被借閱");
            }
        }
    }

    // 還書方法
    public void returnBook(String bookName) {
        books.put(bookName, true);
        System.out.println(Thread.currentThread().getName() + " 歸還了 " + bookName);
    }

    // 顯示書籍狀態
    public void displayBooks() {
        books.forEach((bookName, isAvailable) -> {
            System.out.println(bookName + " 狀態: " + (isAvailable ? "可借閱" : "已借出"));
        });
    }
}

// 圖書館借書系統主類
public class LibrarySystem {
    public static void main(String[] args) {
        Library library = new Library();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 多個讀者同時借書和還書
        executorService.submit(() -> {
            library.borrowBook("書籍1");
            library.borrowBook("書籍2");
            library.processBorrowRequests();
            library.returnBook("書籍1");
        });

        executorService.submit(() -> {
            library.borrowBook("書籍2");
            library.borrowBook("書籍3");
            library.processBorrowRequests();
            library.returnBook("書籍2");
        });

        executorService.submit(() -> {
            library.borrowBook("書籍4");
            library.borrowBook("書籍5");
            library.processBorrowRequests();
            library.returnBook("書籍4");
        });

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // 等待所有任務完成
        }

        System.out.println("最終書籍狀態：");
        library.displayBooks();
    }
}

