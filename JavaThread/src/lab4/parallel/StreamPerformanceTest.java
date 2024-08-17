package lab4.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StreamPerformanceTest {
    private static final int[] DATA_SIZES = {10_000, 100_000, 1_000_000, 10_000_000};

    public static void main(String[] args) {
        for (int size : DATA_SIZES) {
            List<Integer> data = generateData(size);
            
            long sequentialTime = testSequential(data);
            long parallelTime = testParallel(data);
            
            System.out.printf("資料量: %d\n", size);
            System.out.printf("循序流時間: %d ms\n", sequentialTime);
            System.out.printf("並行流時間: %d ms\n", parallelTime);
            System.out.printf("加速比: %.2f\n\n", (double)sequentialTime / parallelTime);
        }
    }

    private static List<Integer> generateData(int size) {
        List<Integer> data = new ArrayList<>(size);
        IntStream.range(0, size).forEach(data::add);
        return data;
    }

    private static long testSequential(List<Integer> data) {
        long start = System.currentTimeMillis();
        long count = data.stream()
                         .filter(n -> n % 2 == 0)
                         .map(n -> n * 2)
                         .count();
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static long testParallel(List<Integer> data) {
        long start = System.currentTimeMillis();
        long count = data.parallelStream()
                         .filter(n -> n % 2 == 0)
                         .map(n -> n * 2)
                         .count();
        long end = System.currentTimeMillis();
        return end - start;
    }
}
