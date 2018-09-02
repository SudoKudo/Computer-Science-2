import java.util.*;

public class Ant {
	// A class to represent a graph edge
	class Edge implements Comparable<Edge> {
		int x, y, d;

		// Comparator function used for sorting edges based on their weight
		public int compareTo(Edge compareEdge) {

			if (this.d == compareEdge.d) {
				return 0;
			} else if (this.d > compareEdge.d) {
				return 1;
			} else {
				return -1;
			}
		}
	};

	// A class to represent a subset for union-find
	class Subset {
		int parent, rank;
	};

	int V, E; // V = no. of vertices & E = no.of edges
	int solvedWeight = 0;

	Edge edge[]; // Collection of all edges

	// Creates a graph with vertices and edges
	Ant(int v, int e) {
		V = v;
		E = e;
		edge = new Edge[E];

		for (int i = 0; i < e; ++i)
			edge[i] = new Edge();
	}

	// A utility function to find set of an element i
	int findSet(Subset subsets[], int i) {
		// find root and make root as parent of i
		if (subsets[i].parent != i)
			subsets[i].parent = findSet(subsets, subsets[i].parent);

		return subsets[i].parent;
	}

	// A function that does union of two sets of x and y
	void unionSet(Subset subsets[], int x, int y) {
		int xroot = findSet(subsets, x);
		int yroot = findSet(subsets, y);

		// Attach smaller rank tree under root of high rank tree
		if (subsets[xroot].rank < subsets[yroot].rank)
			subsets[xroot].parent = yroot;
		else if (subsets[xroot].rank > subsets[yroot].rank)
			subsets[yroot].parent = xroot;

		// If ranks are same, then make one as root and increment
		else {
			subsets[yroot].parent = xroot;
			subsets[xroot].rank++;
		}
	}

	// The main function to construct MST using Kruskal's algorithm
	void disjoint() {
		ArrayList<Edge> result = new ArrayList<Edge>(); // This will store the resultant MST
		int e = 0; // An index variable, used for result
		int i = 0; // An index variable, used for sorted edges
		
		// Makes new edges
		for (i = 0; i < V; ++i) {
			result.add(new Edge());
		}

		// Sort all the edges in non-decreasing order of their weight. If we are not
		// allowed to change the given graph, we can create a copy of array of edges
		Arrays.sort(edge);

		// Allocate memory for creating V subsets
		Subset subsets[] = new Subset[V];
		
		// Makes new subsets
		for (i = 0; i < V; ++i) {
			subsets[i] = new Subset();
		}
		// Create V subsets with single elements
		for (int v = 0; v < V; ++v) {
			subsets[v].parent = v;
			subsets[v].rank = 0;
		}

		i = 0; // Index used to pick next edge

		// Number of edges to be taken is equal to V-1
		while (e < V - 1) {
			// Pick the smallest edge and increment the index for next iteration
			Edge next_edge = new Edge();
			next_edge = edge[i++];

			int x = findSet(subsets, next_edge.x);
			int y = findSet(subsets, next_edge.y);

			// If including this edge does't cause issues, include it in result and increment
			// the index of result for next edge
			if (x != y) {
				result.set(e++, next_edge);
				unionSet(subsets, x, y);
			}
		}

		// Add the weight for each path chosen
		for (i = 0; i < e; ++i) {
			solvedWeight += result.get(i).d;
		}
	}

	// Start main method
	public static void main(String[] args) {
		// Read in file and assign values to variables
		Scanner input = new Scanner(System.in);
		int numCampuses = input.nextInt();

		for (int campusCount = 0; campusCount < numCampuses; campusCount++) {
			int h = input.nextInt();
			int t = input.nextInt();

			// Initialize the tree
			Ant graph = new Ant(h, t);

			// Populate the tree with the values read in from file
			for (int i = 0; i < t; i++) {
				graph.edge[i].x = input.nextInt() - 1;
				graph.edge[i].y = input.nextInt() - 1;
				graph.edge[i].d = input.nextInt();
			}

			// Output the campus number to STDOUT
			System.out.print("Campus #" + (campusCount + 1) + ": ");

			// Try exception, try this, if something bad happens such as an exception, keep
			// running the code.
			try {
				graph.disjoint(); // Solve the tree using Kruskal's Algorithm
			} catch (Exception e) {
				graph.solvedWeight = -1; // Input zero for placeholder, shows unsolvable.
			}

			// Output solved weight or unsolvable message
			if (graph.solvedWeight != -1) {
				System.out.println(graph.solvedWeight); // Output best weight "d"
			} else {
				System.out.println("I'm a programmer, not a miracle worker!"); // If "0", unsolvable.
			}
		}
	}
}