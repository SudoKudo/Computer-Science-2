
/*
 * PACMAN with memoization
 */
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

public class Path {
	// Declare Variables
	public static HashMap<String, Integer> map = new HashMap<>(); // Storing all possible permutations
	public static ArrayList<String> memo = new ArrayList<>(); // Storage for Memoization
	public static int maxRow, maxCol; // Store number of rows and cols
	public static String[][] grid; // Array of all data

	// Method to calculate the current value of a given path
	public static int sum(String[][] grid, String input) {
		int count = 0;
		int row = 0, column = 0;

		// If we're not on the first element, continue
		if (input.length() > 1) {
			String oldPosition = input.substring(0, input.length() - 1); // Remove last character from position to check
																			// previous element
			Integer memo = map.get(oldPosition); // Grabs the previous element

			// *************************************************
			// Perform memoization. If a previous element exists
			// *************************************************
			if (memo != null) {
				// Calculate next value
				String pos = "";
				int dCount = oldPosition.length() - oldPosition.replace("D", "").length(); // Count how many D's
				int rCount = oldPosition.length() - oldPosition.replace("R", "").length(); // Count how many R's

				// Check if it's an R or a D
				if (input.charAt(input.length() - 1) == 'R') {
					// Check if out of bounds, if so, return -1
					if (rCount + 1 < maxCol) {
						pos = grid[dCount][rCount + 1];
						// If we're not in the E position, perform calculation, else return -1 if we are
						if (!pos.equals("E")) {
							return memo + Integer.parseInt(grid[dCount][rCount + 1]);
						}
						return -1;
					} else
						return -1;
					// Repeat for "D" as we did for "R"
				} else if (input.charAt(input.length() - 1) == 'D') {
					// Check if out of bounds
					if (dCount + 1 < maxRow) {
						pos = grid[dCount + 1][rCount];
						// Check if in "E" element
						if (!pos.equals("E")) {
							return memo + Integer.parseInt(grid[dCount + 1][rCount]);
						}
						return -1;
					} else
						return -1;
				}
				// If not in the memoization already, we calculate the value
			} else {
				for (char move : input.toCharArray()) {
					String pos = "";
					// Move to the right and calculate the next right value sum
					if (move == 'R') {
						// Check out of bounds
						if (column + 1 < maxCol) {
							pos = grid[row][++column];
							if (!pos.equals("E")) {

								count += Integer.parseInt(pos);
							}
						} else
							return -1;
						// Move down and calculate next down value
					} else if (move == 'D') {
						// Check out of bounds
						if (row + 1 < maxRow) {
							pos = grid[++row][column];
							if (!pos.equals("E")) {
								count += Integer.parseInt(pos);
							}
						} else
							return -1;
					}
				}
			}
		}
		// Calculate the sum of the first element
		else {
			for (char move : input.toCharArray()) {
				String pos = "";
				// Check next right value
				if (move == 'R') {
					// Check out of bounds
					if (column + 1 < maxCol) {
						pos = grid[row][++column];
						if (!pos.equals("E")) {
							count += Integer.parseInt(pos);
						}
					} else
						return -1;
					// Check next down value
				} else if (move == 'D') {
					// Check out of bounds
					if (row + 1 < maxRow) {
						pos = grid[++row][column];
						if (!pos.equals("E")) {
							count += Integer.parseInt(pos);
						}
					} else
						return -1;
				}
			}
		}
		return count; // Return the sum
	} // End sum method

	// Being main method
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in); // Read input
		// Store input
		int row = scan.nextInt();
		int col = scan.nextInt();
		maxRow = row;
		maxCol = col;
		grid = new String[row][col];
		// Populate the grid array with values
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				grid[i][j] = scan.next();
			}
		}

		char set1[] = { 'D', 'R' }; // This is what will be inside the permutations
		int k = row + col - 2; // How big the permutation should be
		printAllKLength(set1, k); // Perform permutation

		// Find the max value
		Comparator<? super Entry<String, Integer>> maxValueComparator = (entry1, entry2) -> entry1.getValue()
				.compareTo(entry2.getValue());

		Optional<Entry<String, Integer>> maxValue = map.entrySet().stream().max(maxValueComparator);
		Entry<String, Integer> entry = maxValue.get();

		System.out.print((int) (entry.getValue() % (Math.pow(10, 9) + 7)) + " "); // Output max value with modulo of
																					// 10^9 + 7
		// Count how many max values we have
		int count = 0;
		for (Integer value : map.values()) {
			if (value.equals(entry.getValue())) {
				count++;
			}
		}

		System.out.println(count); // Output total number of possible max value paths

		// Hard-Coded alphabetical output
		for (int i = 1; i < row; i++) {
			System.out.print("D");
		}

		for (int i = 1; i < col; i++) {
			System.out.print("R");
		}
	}

	// Begin permutation method
	static void printAllKLength(char set[], int k) {
		int n = set.length;
		printAllKLengthRec(set, "", n, k); // Calls permutation method
	}

	// The main recursive method to print all possible strings of length k
	static void printAllKLengthRec(char set[], String prefix, int n, int k) {
		// Base case: k is 0, print prefix
		if (k == 0) {
			int sum = sum(grid, prefix);
			if (sum != -1) {
				map.put(prefix, sum);
			}
			return;
		}
		// One by one add all characters from set and recursively
		// call for k equals to k-1
		for (int i = 0; i < n; ++i) {

			// Next character of input added
			String newPrefix = prefix + set[i];

			int sum = sum(grid, newPrefix);
			if (sum != -1) {
				map.put(newPrefix, sum);
			}
			printAllKLengthRec(set, newPrefix, n, k - 1); // Recursive call
		}
	}
}
