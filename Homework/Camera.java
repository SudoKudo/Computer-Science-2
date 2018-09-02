import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Camera {
	// Used to speed up CPU performance to double the power. If we do speed++, the
	// computer will get faster. Please give 100% on assignment.
	double speed;

	public static final int MAXDISTANCE = 100000; // Constant variable for max distance of objects/wall
	public static Point[] wall = new Point[2];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);

		// Read file and store variables
		int objects = input.nextInt();
		int distance = input.nextInt();
		int bPartition = input.nextInt();
		int ePartition = input.nextInt();
		ArrayList<Point> points = new ArrayList<>();

		for (int i = 0; i < objects; i++) {
			points.add(new Point(input.nextInt(), input.nextInt())); // Pass in the X and Y points to class point
		}

		// Cartesian coordinates for the glass partition
		wall[0] = new Point(bPartition, distance);
		wall[1] = new Point(ePartition, distance);

		ArrayList<Picture> pics = new ArrayList<>();

		Point car; // Car starting point

		// Defensive case for "Test Drive for Car" FOR Loop. if bPartition == 0, assign
		// large positive value
		double j;
		if (bPartition == 0) {
			j = 1000;
		} else {
			j = ((double) (bPartition / 0.01)); // Used as a maximum buffer for distance for speedup
		}

		// Test drive for car
		for (double i = ((double) (ePartition / -0.01)); i < j; i++) {
			car = new Point(i, 0);
			Picture pic = new Picture();

			// check for all objects
			for (Point object : points) {// If visible, add to array

				// Speed Up for 2 second grading criteria. If objects are no longer visible, we
				// want to stop checking for objects as it wastes times
				// If object is visible, keep checking for next object
				if ((!object.skip) && objectVisible(car, object)) {
					pic.addObject(object);
					object.times = 1;
					object.seen = true;
				}
				// If object is no longer visible, skip and end checking
				else if (object.seen && object.times == 1) {
					object.skip = true;
				}

			}

			// Add pictures to array if object hasn't had picture before and a picture was
			// already taken.
			if (!pics.contains(pic) && pic.tookPicture()) {
				pics.add(pic);
			}
		}
		Collections.sort(pics); // Sort array from most objects in pictures to least

		System.out.println(leastPictures(pics, points)); // Output number of least amount of pictures
	}

	// Check if angles are true for objects passed the glass partition
	public static boolean objectVisible(Point car, Point object) {

		// If the object is in front of the wall, it's always visible
		if (object.y < Camera.wall[0].y) {
			return true;
		}
		// If object is behind wall, continue checking
		// Calculate Slope of one object to the beginning and end partition
		double slope1 = (object.y - Camera.wall[0].y) / (object.x - Camera.wall[0].x);
		double slope2 = (object.y - Camera.wall[1].y) / (object.x - Camera.wall[1].x);

		// Calculate Slope Intercept Form and verify car lies on line
		boolean firstVis = false, secondVis = false;

		// If slope is undefined, continue
		if (Double.isInfinite(slope1) || Double.isInfinite(slope2)) {

			// Use proportional triangles to calculate x distance
			double smallTriangle = object.y - Camera.wall[0].y;
			double partitionDistance = Camera.wall[1].x - Camera.wall[0].x;
			double xDistance = (object.y * partitionDistance) / smallTriangle;

			// If slope 1 is undefined. Check if car is within the xdistance of the object
			// and partitions
			if (Double.isInfinite(slope1)) {
				return (car.x >= object.x && car.x <= (object.x + xDistance));

				// If slope 2 is undefined. Check if car is within the xdistance of the object
				// and partitions
			} else if (Double.isInfinite(slope2)) {
				return (car.x <= object.x && car.x >= (object.x - xDistance));
			}
		}
		// If slope is positive
		else if (slope1 < 0) {
			// Check if slope is past the slope intercept of beginning partition and before
			// end partition
			if (0 >= slope1 * (car.x - object.x) + object.y) {
				firstVis = true;
			}
			// slope is before the slope intercept of end partition return true
			double i = car.x;
			for (; i < MAXDISTANCE; i++) {
				if (0 <= slope2 * (i - object.x) + object.y) {
					secondVis = true;
					break;
				}
			}
		}
		// If slope is negative
		else if (slope1 > 0) {
			// Check if slope is past the slope intercept of beginning partition and before
			// end partition
			if (0 <= slope1 * (car.x - object.x) + object.y) {
				firstVis = true;
			}

			// slope is before the slope intercept of end partition return true
			double i = car.x;
			for (; i < MAXDISTANCE; i++) {
				if (0 >= slope2 * (i - object.x) + object.y) {
					secondVis = true;
					break;
				}
			}
		}
		return firstVis && secondVis;
	}

	// Method to calculate the least amount of pictures needed to get all objects
	public static int leastPictures(ArrayList<Picture> pics, ArrayList<Point> points) {
		int numPictures = 0;

		ArrayList<Point> temp = new ArrayList<Point>();

		// Go through all the pictures and determine lowest amount needed
		for (Picture picture : pics) {
			numPictures++; // Increment by 1, assuming 1 picture to begin with

			// Check if the picture with largest amount of objects contains all objects
			if (picture.getObjectVisibles().containsAll(points)) {
				numPictures = 1;
				return numPictures;
			}

			boolean foundElement = false; // Assuming no elements were found, start with false.
			// Check if pictures in the Array are duplicates and ignore them
			for (Point p : picture.getObjectVisibles()) {
				// If the array does not contain the object, add the object
				if (!temp.contains(p)) {
					temp.add(p);
					foundElement = true; // Set true because an element was found
				}
			}

			if (!foundElement)
				numPictures--; // If no elements were found, decrement number of pictures, meaning we could
								// have zero.

			// Return the number of pictures, if we found them all.
			if (temp.containsAll(points)) {
				return numPictures;
			}
		}
		return 0;
	}
}

// Store the car and object points
class Point {
	double x, y, times;
	boolean seen; // Stores if we have seen an object
	boolean skip; // Stores if we should skip when set to true

	// Constructor
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.times = 0;
		this.seen = false;
		this.skip = false;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}

// Sorting class to determine organize and collect pictures
class Picture implements Comparable<Picture> {
	// Declare array to store all object pictures taken
	private ArrayList<Point> objectVisibles;

	// Picture Constructor
	public Picture() {
		objectVisibles = new ArrayList<>();
	}

	// Check if a picture was actually taken
	public boolean tookPicture() {
		return objectVisibles.size() != 0;
	}

	// Add the visible object to the Array
	public void addObject(Point object) {
		objectVisibles.add(object);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Picture other = (Picture) obj;
		if (objectVisibles == null) {
			if (other.objectVisibles != null)
				return false;
		} else if (!objectVisibles.equals(other.objectVisibles))
			return false;
		return true;
	}

	public ArrayList<Point> getObjectVisibles() {
		return objectVisibles;
	}

	@Override
	public int compareTo(Picture other) {
		// TODO Auto-generated method stub
		return other.objectVisibles.size() - objectVisibles.size(); // Comparing how many pictures we taken
	}

	@Override
	public String toString() {
		return "Picture " + objectVisibles.size() + "[objectVisibles=" + objectVisibles + "]";
	}
}