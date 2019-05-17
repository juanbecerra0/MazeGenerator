/**
 * Random Maze Driver
 * 
 * creates a randomly-generated maze with a beginning and end
 * 
 * Size is determined via x and y value
 * 
 * @author becer
 *
 */
public class Driver {

	public static void main(String[] args) {
		int x = Integer.valueOf(args[0]);
		int y = Integer.valueOf(args[1]);
		
		int[][] intArray = new int[x][y];
		Maze m = new Maze(intArray);
		
		m.generateMaze();
		m.printMaze();
		
	}
	
}
