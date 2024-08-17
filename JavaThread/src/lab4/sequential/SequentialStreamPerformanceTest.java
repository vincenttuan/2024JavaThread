package lab4.sequential;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SequentialStreamPerformanceTest {
    public static void main(String[] args) {
        testPerformance(1); // 建議第一次的效能測試時間先忽略, 因會有建置成本
        testPerformance(1000);
        testPerformance(10000);
        testPerformance(100000);
        testPerformance(1000000);
    }

    private static void testPerformance(int size) {
        List<Integer> numbers = new ArrayList<>(size);
        IntStream.range(0, size).forEach(numbers::add);

        long startTime = System.nanoTime();

        long count = numbers.stream()
                            .filter(n -> n % 2 == 0)
                            .map(n -> n * 2)
                            .count();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;  // 轉換為毫秒

        System.out.printf("資料量: %d, 處理時間: %d ms, 結果數量: %d%n", size, duration, count);
    }
}
