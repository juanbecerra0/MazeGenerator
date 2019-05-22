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
 * 4 = Treasure Chest
 * 5 = Enemy Spawn
 * 
 * @author becer
 *
 */
public class Maze {

	/* Variables */
	
	//	Maze array
	private int[][] maze;
	
	//	Random object
	private Random rand;
	
	//	Chance of a point becoming a "treasure" or "enemy" point
	private double treasureChance;
	private double enemyChance;
	
	//	Maze linked list of points
	private LinkedList<Point> path;
	private Iterator<Point> it;
	
	//	Size of array
	private int x;
	private int y;
	
	//	Starting and ending points
	private Point startPoint;
	private Point endPoint;
	
	/**
	 * Constructor
	 * 
	 * Creates an empty maze given a 2D int array
	 * 
	 * @param maze
	 */
	public Maze(int x, int y, double treasureChance, double enemyChance, int seed) {
		//	For int array
		this.x = x;
		this.y = y;
		this.maze = new int[x][y];
		
		//	For chance of certain objects
		this.treasureChance = treasureChance;
		this.enemyChance = enemyChance;
		
		//	Instantiates random object
		if(seed == -1) {
			rand = new Random();
		} else {
			rand = new Random(seed);
		}
	}
	
	/**
	 * Randomly generates a maze with a beginning and end
	 * 
	 * Accepts a seed value, but is completely random if seed == -1
	 */
	public void generateMaze() {
		//	Determine and set the starting point (Top left of grid)
		startPoint = new Point(rand.nextInt(x/4), rand.nextInt(y/4));
		
		//	Determine and set the ending point (Bottom right of grid)
		endPoint = new Point((x - 1) - rand.nextInt(x/4), (y - 1) - rand.nextInt(y/4));
		
		//	Create a linked list of coordinate points for true path (used for dead-end generation)
		path = new LinkedList<Point>();
		path.add(startPoint);
		
		//	Generate a random path from beginning to end
		generateCorrectPath(startPoint.x, startPoint.y, endPoint.x, endPoint.y);	
		
		//	Add final point to the linked list and instantiate the iterator
		path.add(endPoint);
		it = path.iterator();
		
		//	Generate dead-ends
		generateDeadEnds(startPoint.x, startPoint.y);
		
		//	Generate treasure
		generateTreasure();
		
		//	Generate enemies
		generateEnemies();
		
		//	Finally, print the beginning and end
		maze[startPoint.x][startPoint.y] = 2;
		maze[endPoint.x][endPoint.y] = 3;
	}
	
	/**
	 * Prints the contents of a maze to console
	 */
	public void printMaze() {
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				System.out.print(maze[i][j] + "   ");
				if(j == y - 1) {
					System.out.println("\n");
				}
			}
		}
	}
	
	/**
	 * Gets the 2D integer array associated with the maze object
	 * 
	 * @return 2D int array
	 */
	public int[][] getArray() {
		return maze;
	}
	
	//	Utility methods for maze generation
	
	/**
	 * Loop method: Generates a random path from starting point to ending point
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void generateCorrectPath(int startX, int startY, int endX, int endY) {
		
		//	Set coordinates for current position (Same as starting point)
		int currentX = startX;
		int currentY = startY;
		
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
	}
	
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
		if(x == endPoint.x && y == endPoint.y) {
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
			
			//	Recursive call. Check if path was printed. If not, create a "treasure" point and go to next point iterator.
			if(pathPrinted) {
				
				//	Recursive call
				generateDeadEnds(printedPoint.x, printedPoint.y);
			} else {
				
				//	Go to linked list iterator point
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
				try {
					if(maze[wtgX][wtgY] == 1) {
						//	Coordinate is not viable. Return false.
						isViable = false;
						break;
					}
				} catch (IndexOutOfBoundsException ee) {
					//	Coordinate is bot viable. Return false.
					isViable = false;
					break;
				}
			}
		}
		
		//	Return value
		return isViable;
	}
	
	/**
	 * Generates treasure chests based off of the treasure chance
	 * 
	 * Treasure = 4
	 */
	private void generateTreasure() {
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(treasureChance > rand.nextDouble()
						&& maze[i][j] == 1) {
					maze[i][j] = 4;
				}
			}
		}
	}
	
	/**
	 * Generates enemies based off of the enemy chance
	 * 
	 * Enemies = 5
	 */
	private void generateEnemies() {
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(enemyChance > rand.nextDouble()
						&& maze[i][j] == 1
						&& i != startPoint.x
						&& j != startPoint.y) {
					maze[i][j] = 5;
				}
			}
		}
	}
}
