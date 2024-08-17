package lab4.parallel;

import java.util.List;

public class Basic {
	public static void main(String[] args) {

		List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		numbers.parallelStream().forEach(num -> 
						System.out.println(Thread.currentThread().getName() + ": " + num));

	}
}
