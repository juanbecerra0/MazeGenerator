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
			if (args.length == 4 || args.length == 5) {
				//	Variables
				int x = -1;
				int y = -1;
				double treasureChance = -1;
				double enemyChance = -1;
				Maze m = null;
				
				//	Parse x value
				if(Integer.valueOf(args[0]) >= 4) {
					x = Integer.valueOf(args[0]);
				} else {
					System.err.println("ERROR: X value must be at least 4. You entered " + Integer.valueOf(args[0]));
					printUsageAndExit();
				}
				
				//	Parse y value
				if(Integer.valueOf(args[1]) >= 4) {
					y = Integer.valueOf(args[1]);
				} else {
					System.err.println("ERROR: Y value must be at least 4. You entered " + Integer.valueOf(args[1]));
					printUsageAndExit();
				}
				
				//	Parse treasure chance
				if(Double.valueOf(args[2]) >= 0 || Double.valueOf(args[2]) <= 1) {
					treasureChance = Double.valueOf(args[2]);
				} else {
					System.err.println("ERROR: Treasure rate must be a decimal value between 0 and 1 (inclusive). You entered " + Integer.valueOf(args[2]));
					printUsageAndExit();
				}
				
				//	Parse enemy chance
				if(Double.valueOf(args[3]) >= 0 || Double.valueOf(args[3]) <= 1) {
					enemyChance = Double.valueOf(args[3]);
				} else {
					System.err.println("ERROR: Enemy rate must be a decimal value between 0 and 1 (inclusive). You entered " + Integer.valueOf(args[2]));
					printUsageAndExit();
				}
				
				//	Parse optional seed value or create maze without seed
				if(args.length == 5) {
					if(Integer.valueOf(args[4]) > 0) {
						m = new Maze(x, y, treasureChance, enemyChance, Integer.valueOf(args[4]));
					} else {
						System.err.println("ERROR: Seed value must be greater than 0. You entered " + Integer.valueOf(args[3]));
						printUsageAndExit();
					}
				} else {
					m = new Maze(x, y, treasureChance, enemyChance, -1);
				}
				
				//	Generate the maze
				m.generateMaze();
				
				//	Print maze to console 
				m.printMaze();
				
			} else {
				System.err.println("ERROR: Your argument length must either be 4 or 5. Your argument length was " + args.length);
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
		System.err.println("Usage: java Driver <x dimensions> <y dimensions> <treasure rate> <OPTIONAL: seed value>");
		System.exit(1);
	}
	
}
