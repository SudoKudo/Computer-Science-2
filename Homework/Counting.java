// CS2

import java.util.*;

public class Counting {

	// Being Main
	public static void main(String[] args) {
		// Reads in input from STDIN
		Scanner input = new Scanner(System.in);

		// Scan in number of test cases for each case and solve
		for (long numCases = input.nextInt(); numCases != 0; numCases--) {
			// Create new hashmap for the test case
			HashMap<String, Long> map = new HashMap<String, Long>();

			// Takes in the sequence given
			String sequence = input.next();

			// Takes in the subsequence we are trying to find
			String subsequence = input.next();

			System.out.println(permutate(map, sequence, subsequence, 0L, 0L)); // Call method and start at 0,0 to begin
																				// checking first elements
		}
	} // End Main

	// Perform the permutations to solve the problem by checking how many times the
	// subsequence appears in the sequence
	public static long permutate(HashMap<String, Long> map, String sequence, String subsequence, long element1,
			long element2) {
		long result = 0; // Result starts at zero

		// Base case being if the first string and second string are identical, just
		// return 1 and quit checking.
		if (element2 == subsequence.length())
			return 1;

		String position = element1 + subsequence.substring((int) element2); // Get current position of string

		// If we have already checked the same position, just return that position back
		// to us.
		if (map.containsKey(position)) {
			return map.get(position);
		}

		// Begin checking each element starting at element1
		for (long i = element1; i < sequence.length(); i++) {

			// If sequence and subsequence have the same character at position "i",
			// increment result and recursive back to method
			if (sequence.charAt((int) i) == subsequence.charAt((int) element2)) {

				// We increase the result and move the element up by 1 or more.
				result += permutate(map, sequence, subsequence, i + 1, element2 + 1);
			}
		}
		return result;
	} // End permutate method
} // End Counting Class
