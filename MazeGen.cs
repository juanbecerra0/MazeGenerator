using Godot;
using System;
using System.Collections.Generic;
using System.Text;

public class MazeGen : Node
{
    // Declare member variables here. Examples:
    // private int a = 2;
    // private string b = "text";

    /* Variables */

	//	2D int array representation
    private int[,] maze;
	
	//	Random object
	private Random rand;
	
	//	Chance of a point becoming a "treasure" or "enemy" point
	private double treasureChance;
	private double enemyChance;
	
	//	Maze linked list of points
	private LinkedList<Tuple<int, int>> path;
	private LinkedListNode<Tuple<int, int>> currIterNode;
	
	//	Size of array
	private int x;
	private int y;
	
	//	Starting and ending points
	private Tuple<int, int> startPoint;
	private Tuple<int, int> endPoint;

    public MazeGen() {
		//	Empty constructor
    }

	public int[,] getArray() {
        return maze;
    }

	public void printArray() {

		StringBuilder str = new StringBuilder((maze.GetLength(0) * maze.GetLength(1)) * 4);

		for(int i = 0; i < maze.GetLength(0); i++) {
			for(int j = 0; j < maze.GetLength(1); j++) {
				str.Append(maze[i,j] + "   ");
			}
			str.Append("\n\n");
		}

		GD.Print(str.ToString());
	}

	public MazeGen(int x, int y, double treasureChance, double enemyChance, int seed) {
        this.x = x;
        this.y = y;
        this.maze = new int[x,y];

        this.treasureChance = treasureChance;
        this.enemyChance = enemyChance;

        if(seed == -1) {
            rand = new Random();
        } else {
            rand = new Random(seed);
        }
    }

    public void generateMaze() {
        //	Determine and set the starting point (Top left of grid)
		startPoint = new Tuple<int, int>(rand.Next(x/4), rand.Next(y/4));
		
		//	Determine and set the ending point (Bottom right of grid)
		endPoint = new Tuple<int, int>((x - 1) - rand.Next(x/4), (y - 1) - rand.Next(y/4));
		
		//	Create a linked list of coordinate points for true path (used for dead-end generation)
		path = new LinkedList<Tuple<int, int>>();
		path.AddLast(startPoint);
		
		//	Generate a random path from beginning to end
		generateCorrectPath(startPoint.Item1, startPoint.Item2, endPoint.Item1, endPoint.Item2);	
		
		//	Add final point to the linked list and instantiate the iterator
		path.AddLast(endPoint);
		currIterNode = path.First;
		currIterNode = currIterNode.Next;

		//	Generate dead-ends
		generateDeadEnds(startPoint.Item1, startPoint.Item2);	//	TODO causing game to crash due to unhandled exception
		
		//	Generate treasure
		generateTreasure();
		
		//	Generate enemies
		generateEnemies();
		
		//	Finally, print the beginning and end
		maze[startPoint.Item1,startPoint.Item2] = 2;
		maze[endPoint.Item1,endPoint.Item2] = 3;
    }

    private void generateCorrectPath(int startX, int startY, int endX, int endY) {
        int currentX = startX;
		int currentY = startY;
		
		while(!(currentX == endX && currentY == endY)) {
			if((rand.Next(2) == 0) && (currentY + 1 <= endY)) {
				currentY ++;
				if(!(currentX == endX && currentY == endY)) {
					maze[currentX,currentY] = 1;
					path.AddLast(new Tuple<int, int>(currentX, currentY));
				}
			} else if(currentX + 1 <= endX) {
				currentX++;
				if(!(currentX == endX && currentY == endY)) {
					maze[currentX,currentY] = 1;
					path.AddLast(new Tuple<int, int>(currentX, currentY));
				}
			}
		}
    }

    private void generateDeadEnds(int x, int y) {
        //	End condition
		if(x == endPoint.Item1 && y == endPoint.Item2) {
			return;
		} else {
			//	Random cardinal direction
			int direction = rand.Next(5);
			
			//	Boolean flag for path printed. If printed, point changes to sane value.
			bool pathPrinted = false;
			Tuple<int, int> printedPoint = new Tuple<int, int>(-1, -1);
			
			//	Check for viable points in the four cardinal directions counter-clockwise
			for(int i = 0; i < 4; i++) {
				switch(direction) {
				case 0:
					if(checkDirection(x-1, y, x, y) && !pathPrinted) {
						maze[x-1,y] = 1;
						printedPoint = new Tuple<int, int>(x-1, y);
						pathPrinted = true;
					}
					break;
				case 1:
					if(checkDirection(x, y-1, x, y) && !pathPrinted) {
						maze[x,y-1] = 1;
						printedPoint = new Tuple<int, int>(x, y-1);
						pathPrinted = true;
					}
					break;
				case 2:
					if(checkDirection(x+1, y, x, y) && !pathPrinted) {
						maze[x+1,y] = 1;
						printedPoint = new Tuple<int, int>(x+1, y);
						pathPrinted = true;
					}
					break;
				case 3:
					if(checkDirection(x, y+1, x, y) && !pathPrinted) {
						maze[x,y+1] = 1;
						printedPoint = new Tuple<int, int>(x, y+1);
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
				generateDeadEnds(printedPoint.Item1, printedPoint.Item2);
			} else {
				//	Go to linked list iterator point
				currIterNode = currIterNode.Next;
				if(currIterNode != null) {
					Tuple<int, int> temp = currIterNode.Value;
					generateDeadEnds(temp.Item1, temp.Item2);
				}
			}
		}
    }

    private bool checkDirection(int x, int y, int prevX, int prevY) {
		/* */
        //	return is true by default
		bool isViable = true;
		
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
					if(maze[wtgX,wtgY] == 1) {
						//	Coordinate is not viable. Return false.
						isViable = false;
						break;
					}
				} catch (IndexOutOfRangeException) {
					//	Coordinate is bot viable. Return false.
					isViable = false;
					break;
				} 
			}
		}
		
		//	Return value
		return isViable;
    }

    private void generateTreasure() {
        for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(treasureChance > rand.NextDouble()
						&& maze[i,j] == 1) {
					maze[i,j] = 4;
				}
			}
		}
    }

    private void generateEnemies() {
        for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(enemyChance > rand.NextDouble()
						&& maze[i,j] == 1
						&& i != startPoint.Item1
						&& j != startPoint.Item2) {
					maze[i,j] = 5;
				}
			}
		}
    }

    // Called when the node enters the scene tree for the first time.
    public override void _Ready()
    {
		
    }

//  // Called every frame. 'delta' is the elapsed time since the previous frame.
//  public override void _Process(float delta)
//  {
//      
//  }
}
