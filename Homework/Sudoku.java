import java.util.Scanner;

public class Sudoku {

	/**
	 * @param args
	 *            the command line arguments
	 */
	// Being main method
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		int numPuzzles = input.nextInt(); // Retrieving next int in the input text
		
		// Read through entire input
		for (int i = 0; i < numPuzzles; i++) {
			Puzzle p = new Puzzle();
			p.readGame(input);

			System.out.println("Test Case " + (i + 1) + ": \n");
			
			// If puzzle is solved, output puzzle
			if (p.solveSudoku()) {
				p.print();
				System.out.println();
			} else { // If unsolvable, output no solution
				System.out.println("No solution possible. \n");
			}
		}
	}

} // End main method

class Puzzle {
	// Initialize sudoku puzzle
	private int sudoku[][] = new int[9][9];
	private int nextEmptyPosition[] = { 0, 0 };

	// Check if the number is already used in a row
	private boolean inRow(int number, int row) {
		for (int i = 0; i < 9; i++) {
			if (sudoku[row][i] == number) {
				return false;
			}
		}
		return true;
	}

	// Check if the number is already used in a column
	private boolean inCol(int number, int col) {
		for (int i = 0; i < 9; i++) {
			if (sudoku[i][col] == number) {
				return false;
			}
		}
		return true;
	}

	// Check if the number is in the 3x3 grid
	private boolean inGrid(int number, int row, int col) {
		int colGrid = col - col % 3;
		int rowGrid = row - row % 3;

		// Loop through the 3x3 grid and check numbers
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (sudoku[rowGrid + i][colGrid + j] == number) {
					return false;
				}
			}
		}
		return true;
	}

	// Check if the number is in all three previous conditions combined
	private boolean allValid(int number, int row, int col) {
		return (inRow(number, row) && inCol(number, col) && inGrid(number, row, col));
	}

	// Check for empty cells and assign to the next empty cell if true
	private boolean emptyCell() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (sudoku[row][col] == 0) {
					nextEmptyPosition = new int[] { row, col };
					return true;
				}
			}
		}
		return false;
	}

	// Solve the sudoku
	public boolean solveSudoku() {
		// If no empty cells, return true
		if (!emptyCell()) {
			return true;
		}
		
		// Get the first two empty positions
		int row = nextEmptyPosition[0];
		int col = nextEmptyPosition[1];

		// Check the ranges from 1 - 9 for the numbers
		for (int i = 1; i < 10; i++) {
			if (allValid(i, row, col)) {
				sudoku[row][col] = i;

				// Use recursion to evaluate if all cells are true.
				if (solveSudoku()) {
					return true;
				}

				// If recursion doesn't evaluate to true, reset the number back to 0.
				sudoku[row][col] = 0;
			}
		}
		return false; // if the numbers are not good, return false.
	}
	// Read the data file to our class
	public void readGame(Scanner input) {
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku[row].length; col++) {
				sudoku[row][col] = input.nextInt();

			}
		}
	}
	// Output the sudoku puzzle to the standard out
	public void print() {
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku[row].length; col++) {
				System.out.print(sudoku[row][col] + " ");
			}
			System.out.println();
		}
	}
}
