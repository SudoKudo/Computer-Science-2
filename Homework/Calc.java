// CS2

import java.util.*;

public class Calc {
	int superAGrade = 100; // predefined grade for this assignment, just put in 100%.

	// Predefine variables in assignment: a = addition, b = multiplication, c =
	// division, t = test case
	public static int a, b, c, t;
	final public static int SIZE = 999999; // Max size of test cases is 999999 since calc is broken
	final public static int MAX = 999999999; // random max value, don't know how high to go.

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in); // Scan in input

		int n = stdin.nextInt(); // First variable is # of test cases

		// Loop through and assign A, B, C, and T variables the next elements and solve
		// each test case
		for (int i = 0; i < n; i++) {
			int beginning = 0;
			// Store data
			a = stdin.nextInt();
			b = stdin.nextInt();
			c = stdin.nextInt();
			t = stdin.nextInt();

			// Call function to solve, and output minimum number of button presses
			System.out.println(someBFS(beginning, a, b, c, t));
		}
	}

	// Some BFS Algorithm
	public static int someBFS(int start, int a, int b, int c, int t) {

		// Initially starting at zero, as nothing has been visited, assigning -1 to non
		// visited items
		int[] checked = new int[SIZE];
		for (int i = 0; i < SIZE; i++)
			checked[i] = -1;

		// Initializing the queue, since it uses queue of some sort for BFS
		LinkedList<EdgesFill> queue = new LinkedList<EdgesFill>();
		queue.add(new EdgesFill(start, 0));

		int visistedEdge = 0; // Each edge gets zero
		int impossible = -1; // Set to -1 by default, this unsolvable
		int len = 0;

		// The most appropriate while loop in the world, very respectful. CMP to size-1
		// for null terminator.
		while (queue.size() > 0 && visistedEdge < SIZE - 1) {

			EdgesFill p = queue.poll();
			checked[p.value] = 1; // Good ole tired visited edges get 1

			// See if the next item from the queue needs to be logged.
			if (p.value == t) {
				impossible = p.distance;
				break;

			}
			// Check the nodes and see which order works for enqueuing by adding,
			// multiplying, or dividing until the t is found.
			else {

				// Enqueue item by adding by a value
				int test = (p.value + a) % 1000000;// Used to handle exception when calc display passes 1000000 by
														// using modulus
				if (test > 0 && test < SIZE && checked[test] != 1) {
					queue.add(new EdgesFill(test, p.distance + 1));
				}

				// Enqueue item by multiplying by b values
				test = (b * p.value) % 1000000;// Used to handle exception when calc display passes 1000000 decimal by
													// using modulus
				if (test > 0 && test < SIZE && checked[test] != 1) {
					queue.add(new EdgesFill(test, p.distance + 1));
				}

				// Enqueue item by dividing by c value
				test = (p.value / c) % 1000000;// Used to handle exception when calc display passes 1000000 decimal by
													// using modulus
				if (test > 0 && test < SIZE && checked[test] != 1) {
					queue.add(new EdgesFill(test, p.distance + 1));
				}
			}

		}

		return impossible; // Return final impossible number of -1 or some min number of buttons presses
	}
}

// Storing the distances and values of checking if we been to nodes
class EdgesFill {

	public int value;
	public int distance;

	public EdgesFill(int v, int d) {
		value = v;
		distance = d;
	}
}
