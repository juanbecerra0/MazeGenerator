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

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 2 || args.length == 3) {
				//	Variables
				int x = -1;
				int y = -1;
				Maze m;
				
				//	Parse x value
				if(Integer.valueOf(args[0]) > 1) {
					x = Integer.valueOf(args[0]);
				} else {
					System.err.println("ERROR: X value must be greater than 1. You entered " + Integer.valueOf(args[0]));
					printUsageAndExit();
				}
				
				//	Parse y value
				if(Integer.valueOf(args[1]) > 1) {
					y = Integer.valueOf(args[1]);
				} else {
					System.err.println("ERROR: Y value must be greater than 1. You entered " + Integer.valueOf(args[1]));
					printUsageAndExit();
				}
				
				//	Create intArray and Maze object
				int[][] intArray = new int[x][y];
				m = new Maze(intArray);
				
				//	Parse optional seed value or generate maze without seed
				if(args.length == 3) {
					if(Integer.valueOf(args[2]) > 0) {
						m.generateMaze(Integer.valueOf(args[2]));
					} else {
						System.err.println("ERROR: Seed value must be greater than 0. You entered " + Integer.valueOf(args[2]));
						printUsageAndExit();
					}
				} else {
					m.generateMaze(-1);
				}
				
				m.printMaze();
			} else {
				System.err.println("ERROR: Your argument length must either be 2 or 3. Your argument length was " + args.length);
				printUsageAndExit();
			}
		} catch (Exception e) {
			System.err.println("An exception has occured\n");
			e.printStackTrace();
			System.err.println();
			printUsageAndExit();
		}
	}
	
	/**
	 * Prints a usage message and exits program
	 * 
	 * Used for invalid arguments/propagated exceptions in Maze.java
	 */
	private static void printUsageAndExit() {
		System.err.println("Usage: java Driver <x dimensions> <y dimensions> <OPTIONAL: seed value>");
		System.exit(1);
	}
	
}
