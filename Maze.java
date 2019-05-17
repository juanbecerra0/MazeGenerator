import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Maze generator
 * 
 * Creates a random maze given a 2D boolean array
 * 
 * 0 = Inaccessible
 * 1 = Accessible
 * 2 = Start
 * 3 = End
 * 
 * @author becer
 *
 */
public class Maze {

	/* Variables */
	
	//	Maze array
	private int[][] maze;
	
	//	Maze linked list of points
	private LinkedList<Point> path;
	private Iterator<Point> it;
	
	//	Random object
	private Random rand;
	
	//	Size of array
	private int x;
	private int y;
	
	//	Starting points
	private int startX;
	private int startY;
	
	//	Ending points
	private int endX;
	private int endY;
	
	/**
	 * Constructor
	 * 
	 * Creates an empty maze given a 2D int array
	 * 
	 * @param maze
	 */
	public Maze(int[][] maze) {
		this.maze = maze;
		x = maze.length;
		y = maze[0].length;
	}
	
	/**
	 * Randomly generates a maze with a beginning and end
	 * 
	 * No seed used
	 */
	public void generateMaze() {
		//	Instantiates random object
		rand = new Random();
		
		//	Determine and set the starting point (Top left of grid)
		startX = rand.nextInt(x/4);
		startY = rand.nextInt(y/4);
		maze[startX][startY] = 2;
		
		//	Determine and set the ending point (Bottom right of grid)
		endX = (x - 1) - rand.nextInt(x/4);
		endY = (y - 1) - rand.nextInt(y/4);
		maze[endX][endY] = 3;
		
		//	Set coordinates for current position (Same as starting point)
		int currentX = startX;
		int currentY = startY;
		
		//	Create a linked list of coordinate points for true path (used for dead-end generation)
		path = new LinkedList<Point>();
		path.add(new Point(startX, startY));
		
		//	Generate a random path from beginning to end
		while(!(currentX == endX && currentY == endY)) {
			if((rand.nextInt(2) == 0) && (currentY + 1 <= endY)) {
				currentY ++;
				if(!(currentX == endX && currentY == endY)) {
					maze[currentX][currentY] = 1;
					path.add(new Point(currentX, currentY));
				}
			} else if(currentX + 1 <= endX) {
				currentX++;
				if(!(currentX == endX && currentY == endY)) {
					maze[currentX][currentY] = 1;
					path.add(new Point(currentX, currentY));
				}
			}
		}
		
		//	Add end point to linked list
		path.add(new Point(endX, endY));
		
		//	Instantiate linked list iterator
		it = path.iterator();
		
		//	Generate dead-ends
		generateDeadEnds(startX, startY);
		
	}
	
	/**
	 * Randomly generates a maze with a beginning and end
	 * 
	 * Seed is used
	 * 
	 * @param seed
	 */
	public void generateMaze(int seed) {
		// Instantiates random SEEDED object
		rand = new Random(seed);
		
		//	TODO
	}
	
	/**
	 * Prints the contents of a maze to console
	 */
	public void printMaze() {
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				System.out.print(maze[i][j] + "  ");
				if(j == y - 1) {
					System.out.println("\n");
				}
			}
		}
	}
	
	//	Utility methods for maze generation
	
	/**
	 * Recursively checks and prints paths that do not lead to the end
	 * 
	 * Exit condition: x == endX and y == endY
	 * 
	 * Recursive check: Pick a random cardinal direction. Iterate through directions counter-clockwise.
	 * The first viable direction will be selected.
	 * Keep recursively calling until a dead-end is hit. At this point, recursively check it.next()
	 * 
	 */
	private void generateDeadEnds(int x, int y) {
		//	End condition
		if(x == endX && y == endY) {
			return;
		} else {
			//	Random cardinal direction
			int direction = rand.nextInt(5);
			
			//	Boolean flag for path printed. If printed, point changes to sane value.
			boolean pathPrinted = false;
			Point printedPoint = new Point(-1, -1);
			
			//	Check for viable points in the four cardinal directions counter-clockwise
			for(int i = 0; i < 4; i++) {
				switch(direction) {
				case 0:
					if(checkDirection(x-1, y, x, y) && !pathPrinted) {
						maze[x-1][y] = 1;
						printedPoint = new Point(x-1, y);
						pathPrinted = true;
					}
					break;
				case 1:
					if(checkDirection(x, y-1, x, y) && !pathPrinted) {
						maze[x][y-1] = 1;
						printedPoint = new Point(x, y-1);
						pathPrinted = true;
					}
					break;
				case 2:
					if(checkDirection(x+1, y, x, y) && !pathPrinted) {
						maze[x+1][y] = 1;
						printedPoint = new Point(x+1, y);
						pathPrinted = true;
					}
					break;
				case 3:
					if(checkDirection(x, y+1, x, y) && !pathPrinted) {
						maze[x][y+1] = 1;
						printedPoint = new Point(x, y+1);
						pathPrinted = true;
					}
					break;
				}
				
				//	Go to next cardinal direction
				direction++;
				if(direction == 4) {
					direction = 0;
				}
			}
			
			//	Recursive call. Check if path was printed. If not, go to next point iterator.
			if(pathPrinted) {
				generateDeadEnds(printedPoint.x, printedPoint.y);
			} else {
				Point temp = it.next();
				generateDeadEnds(temp.x, temp.y);
			}
			
		}
	}
	
	/**
	 * Checks to see if a maze value is viable
	 * 
	 * If so, returns true. Otherwise returns false.
	 * 
	 * @param x - X of where you want to go
	 * @param y - Y of where you want to go
	 * @param prevX - X of where you currently are
	 * @param prevY - Y of where you currently are
	 * @return
	 */
	private boolean checkDirection(int x, int y, int prevX, int prevY) {
		//	return is true by default
		boolean isViable = true;
		
		//	Coordinates for checking cardinal directions
		int wtgX = 0;
		int wtgY = 0;
		
		//	Iterate through four cardinal directions
		for(int i = 0; i < 4; i++) {
			switch(i) {
			case 0:
				wtgX = x + 1;
				wtgY = y;
				break;
			case 1:
				wtgX = x - 1;
				wtgY = y;
				break;
			case 2:
				wtgX = x;
				wtgY = y + 1;
				break;
			case 3:
				wtgX = x;
				wtgY = y - 1;
				break;
			}
			
			//	Skip iteration if direction is equal to prevX and prevY
			if(!(wtgX == prevX && wtgY == prevY)) {
				if(maze[wtgX][wtgY] == 1) {
					//	Coordinate is not viable. Return false.
					isViable = false;
					break;
				}
			}
		}
		
		//	Return value
		return isViable;
	}
}
