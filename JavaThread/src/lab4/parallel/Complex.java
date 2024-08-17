package lab4.parallel;

import java.util.List;

public class Complex {
	public static void main(String[] args) {
		List<String> words = List.of("apple", "banana", "cherry", "date", "berry", "fig", "grape"); 

		words.parallelStream()
		            .filter(word -> word.length() > 5) 
		            .map(String::toUpperCase)
		            .sorted() 
		            .forEachOrdered(System.out::println);


	}
}
