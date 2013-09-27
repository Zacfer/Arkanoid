import java.util.Random;

public class DemoWindow extends WindowController {

	private int lastId = -1, rows, cols, previousX = 0, previousY = 0, blockX = 0, blockY = 0, brickIndex[][], bricksNumber, bricksDestroyed = 0;
	private String[] colours = {"Black", "White", "Red", "Blue", "Green", "Orange", "Pink", "Yellow", "Purple"} ;
	private boolean direction, sense, isPlaying, isOccupied[][];
	private SquareDrawableObject square = null;
	private BlockDrawableObject block = null;
	private BrickDrawableObject[] bricks;	

	protected void buttonPressed(int id) 
	{
		char asciimap = (char) (id + 65); // Converts argument to its button text using the ASCII table
		
		System.out.println("Button " + asciimap + " was pressed.");
		if(id == 0) // Button A
		{
			Random randomColourGenerator = new Random();
			int randomColour = randomColourGenerator.nextInt(9);
			
			setBackgroundColour(randomColour); 
			System.out.println("Background changed to: " + colours[randomColour]); // Use a String array mapping for colour names
		}
		else if(id == 1) // Button B
		{				
			if(lastId != 1) // Button B was not the last button pressed
			{
				previousX = rows/2; //Initialise snail
				previousY = cols/2;
				
				setBackgroundColour(GraphicsController.BLACK);
				setGridColour(previousX, previousY, GraphicsController.WHITE);		
			}
			else
			{
				Random randomMovementGenerator = new Random();
				int randomMovement;
				
				while(true) // While loop so that the snail will try a different random movement if it cannot move in an occasion
				{
					randomMovement = randomMovementGenerator.nextInt(4);
					switch(randomMovement)
					{
						// Down
						case 0: if(previousY + 1 < cols) // In all cases: Check that movement is within boundaries
								{
									setGridColour(previousX, previousY, GraphicsController.YELLOW);
									setGridColour(previousX, ++previousY, GraphicsController.WHITE);
								}
								else
									continue; // Otherwise, try a different movement
								break;
						//Up
						case 1: if(previousY - 1 >= 0)
								{
								setGridColour(previousX, previousY, GraphicsController.YELLOW);
								setGridColour(previousX, --previousY, GraphicsController.WHITE);
								}
								else
									continue;
								break;
						//Left
						case 2: if(previousX - 1 >= 0)
								{
								setGridColour(previousX, previousY, GraphicsController.YELLOW);
								setGridColour(--previousX, previousY, GraphicsController.WHITE);
								}
								else
									continue;
								break;
						//Right
						case 3: if(previousX + 1 < rows)
								{
								setGridColour(previousX, previousY, GraphicsController.YELLOW);
								setGridColour(++previousX, previousY, GraphicsController.WHITE);
								}
								else
									continue;
								break;
						default: break;
					}
					break;
				}
			}
			
		}
		else if(id == 2) // Button C
		{	
			if(lastId != 2) // Button C was not the last button pressed
			{
				Random randomColourGenerator = new Random();
			
				setBackgroundColour(GraphicsController.BLACK);
				square = new SquareDrawableObject(rows/2, cols/2, randomColourGenerator.nextInt(8)+1); // Random colour that's not black
				draw(square);
			}
			else
			{
				Random randomPositionGenerator = new Random();
				
				//Ensure that the square is in the bounds of the grid (0 to rows-1) by making sure that its center is always in the range of (1,1)-(rows-2,cols-2)
				//In other words, disallow the center to be in any position of the first and last rows and columns
				square.changePosition(randomPositionGenerator.nextInt(rows-2)+1, randomPositionGenerator.nextInt(cols-2)+1);
				setBackgroundColour(GraphicsController.BLACK);
				draw(square);
			}
		}
		else if(id == 3) //Button D
		{
			Random randomColourGenerator = new Random();
			
			previousX = rows/2;
			previousY = cols/2;
			setBackgroundColour(GraphicsController.BLACK);
			square = new SquareDrawableObject(rows/2, cols/2, randomColourGenerator.nextInt(8)+1); // Random colour that's not black
			draw(square);
		}
		else if(id == 4) //Button E. Arkanoid Game
		{
			Random randomPositionGenerator = new Random();
			int rowColour=GraphicsController.RED; //Initialise the colour of the bricks of the first row to red.
			
			setBackgroundColour(GraphicsController.BLACK);
			isPlaying = true; 
			blockX = rows/2; // Initialise position of player's block
			blockY = cols-2;
			bricksNumber = 0;
			bricksDestroyed = 0;
			// Initialise game details. isOccupied is a boolean map which shows whether a coordinate is occupied by the block or a brick.
			// brickIndex returns the index of a brick in the bricks array given coordinates, otherwise -1 if the coordinates are unoccupied.
			// bricks is an array which holds all the bricks objects.
			for(int i = blockX-1; i < blockX+5; i++)
					isOccupied[i][blockY] = true;
			for(int i = 0; i < rows; i++)
				for(int j = 0; j < cols; j++)
					brickIndex[i][j] = -1;
			for(int y = 2; y < cols/2; y+=3) // Position the bricks
			{
				for(int x = 1; x < rows-1; x+=4)
				{
					brickIndex[x][y] = bricksNumber;
					brickIndex[x+1][y] = bricksNumber;
					brickIndex[x+2][y] = bricksNumber;
					brickIndex[x][y+1] = bricksNumber;
					brickIndex[x+1][y+1] = bricksNumber;
					brickIndex[x+2][y+1] = bricksNumber;
					isOccupied[x][y] = true;
					isOccupied[x+1][y] = true;
					isOccupied[x+2][y] = true;
					isOccupied[x][y+1] = true;
					isOccupied[x+1][y+1] = true;
					isOccupied[x+2][y+1] = true;
					bricks[bricksNumber++] = new BrickDrawableObject(x+1, y, rowColour);
				}
				rowColour++; // Change the colour of the bricks after a row is done positioning
			}
			sense = true; // Ball is going up if sense is true, down otherwise
			block = new BlockDrawableObject(blockX, blockY, GraphicsController.BLUE); 
			previousX = randomPositionGenerator.nextInt(rows); // Position the ball in a random location between the bricks and the block.
			previousY = randomPositionGenerator.nextInt(cols-20)+20;
			setGridColour(previousX, previousY, GraphicsController.WHITE);
		}
		lastId = id;	// Save the last button that was pressed
	}
	
	protected void collision() // Function that deals with collisions in the Arkanoid game
	{
		if(sense) // Ball is going up
		{
			if(previousY-1 < 0)  // Ball has reached upper bounds of grid
				sense = !sense;
			else if(isOccupied[previousX][previousY-1]) // Ball is about to hit a brick directly above it
			{
				sense = !sense;
				if(brickIndex[previousX][previousY-1] != -1) 
				{
					isOccupied = bricks[brickIndex[previousX][previousY-1]].destroy(isOccupied); // Destroy brick and update the isOccupied bitmap.
					bricksDestroyed++;
				}
			}
			else
			{
				if(direction)  // Moving in the diagonal line whose y decreases as x increases (resembling y = -x)
				{
					if(previousX-1 >= 0) // Ensure ball stays within bounds
					{
						if(isOccupied[previousX-1][previousY-1] && (!isOccupied[previousX-1][previousY] && !isOccupied[previousX][previousY-1])) // Ball hits the edge of a brick
						{
							if(brickIndex[previousX-1][previousY-1] != -1)
							{
								isOccupied = bricks[brickIndex[previousX-1][previousY-1]].destroy(isOccupied);
								bricksDestroyed++;
							}
						}
						else if(isOccupied[previousX-1][previousY]) // Ball hits side of brick
							if(brickIndex[previousX-1][previousY] != -1)
							{
								isOccupied = bricks[brickIndex[previousX-1][previousY]].destroy(isOccupied);
								bricksDestroyed++;
							}
					}
				}
				else // Moving in the diagonal line whose y increases as x increases (resembling y = x)
				{
					if(previousX+1 < rows) // Ensure ball stays within bounds
					{
						if(isOccupied[previousX+1][previousY-1] && (!isOccupied[previousX+1][previousY] && !isOccupied[previousX][previousY-1])) // Ball hits the edge of a brick
						{
							if(brickIndex[previousX+1][previousY-1] != -1)
							{
								isOccupied = bricks[brickIndex[previousX+1][previousY-1]].destroy(isOccupied);
								bricksDestroyed++;
							}
						}
						else if(isOccupied[previousX+1][previousY]) // Ball hits side of brick
							if(brickIndex[previousX+1][previousY] != -1)
							{
								isOccupied = bricks[brickIndex[previousX+1][previousY]].destroy(isOccupied);
								bricksDestroyed++;
							}
					}
				}
			}
		}
		else // Same as above, only ball is going down
		{
			if(previousY + 1 == cols) // Ball hits bottom
			{
				isPlaying = false;
				System.out.println("GAME OVER!");
			}
			else if(isOccupied[previousX][previousY+1])
			{
				sense = !sense;
				if(brickIndex[previousX][previousY+1] != -1)
				{
					isOccupied = bricks[brickIndex[previousX][previousY+1]].destroy(isOccupied);
					bricksDestroyed++;
				}
			}
			else
			{
				if(direction)
				{
					if(previousX+1 < rows)
					{
						if(isOccupied[previousX+1][previousY+1] && (!isOccupied[previousX+1][previousY] && !isOccupied[previousX][previousY+1]))
						{
							if(brickIndex[previousX+1][previousY+1] != -1)
							{
								isOccupied = bricks[brickIndex[previousX+1][previousY+1]].destroy(isOccupied);
								bricksDestroyed++;
							}
						}
						else if(isOccupied[previousX+1][previousY])
							if(brickIndex[previousX+1][previousY] != -1)
							{
								isOccupied = bricks[brickIndex[previousX+1][previousY]].destroy(isOccupied);
								bricksDestroyed++;
							}
					}
				}
				else
				{
					if(previousX-1 >= 0)
					{
						if(isOccupied[previousX-1][previousY+1] && (!isOccupied[previousX-1][previousY] && !isOccupied[previousX][previousY+1]))
						{
							if(brickIndex[previousX-1][previousY+1] != -1)
							{
								isOccupied = bricks[brickIndex[previousX-1][previousY+1]].destroy(isOccupied);
								bricksDestroyed++;
							}
						}
						else if(isOccupied[previousX-1][previousY])
							if(brickIndex[previousX-1][previousY] != -1)
							{
								isOccupied = bricks[brickIndex[previousX-1][previousY]].destroy(isOccupied);
								bricksDestroyed++;
							}
					}
				}
			}
		}
		direction = !direction; // Change the direction of the ball with every collision.
	}
	
	
	
	protected void setBackgroundColour(int colour)
	{	
		for (int x = 0; x < rows; x++)
			for(int y = 0; y < cols; y++)
				setGridColour(x, y, colour); // Paint the grid
	}
	
	protected void keyPressed(Key arg0) 
	{
		if(lastId == 4) // Used for Arkanoid. Moves the player's block either left or right according to key, and updates the positions
		{
			if(arg0.isLeft())
			{
				if(blockX-4 >= 0)
				{
					isOccupied[blockX+3][blockY] = false;
					isOccupied[blockX-4][blockY] = true;
					block.changePosition(--blockX, blockY);
				}
			}
			if(arg0.isRight())
			{
				if(blockX+4 <= rows-1)
				{
					isOccupied[blockX-3][blockY] = false;
					isOccupied[blockX+4][blockY] = true;
					block.changePosition(++blockX, blockY);
				}
			}
		}
		
	}

	protected void keyReleased(Key arg0) 
	{

		if(lastId == 4) // Moves the block when key is released too so it moves faster
		{
			if(arg0.isLeft())
			{
				if(blockX-4 >= 0)
				{
					isOccupied[blockX+3][blockY] = false;
					isOccupied[blockX-4][blockY] = true;
					block.changePosition(--blockX, blockY);
				}
			}
			if(arg0.isRight())
			{
				if(blockX+4 <= rows-1)
				{
					isOccupied[blockX-3][blockY] = false;
					isOccupied[blockX+4][blockY] = true;
					block.changePosition(++blockX, blockY);
				}
			}
		}
	}

	protected void update() 
	{
		if(lastId == 3) // Button D animation
		{
			Random randomMovementGenerator = new Random();
			int randomMovement;
			
			while(true) // Ensure that the brick tries a different movement if it cannot move
			{
				setBackgroundColour(GraphicsController.BLACK);
				randomMovement = randomMovementGenerator.nextInt(4);
				switch(randomMovement)
				{
					// Down
					case 0: if(previousY + 1 < cols-1)
							{
								square.changePosition(previousX, ++previousY);
								draw(square);
							}
							else
								continue; // Try a random movement again if it cannot change position
							break;
					//Up
					case 1: if(previousY - 1 > 0)
							{
								square.changePosition(previousX, --previousY);
								draw(square);
							}
							else
								continue;
							break;
					//Left
					case 2: if(previousX - 1 > 0)
							{
								square.changePosition(--previousX, previousY);
								draw(square);
							}
							else
								continue;
							break;
					//Right
					case 3: if(previousX + 1 < rows-1)
							{
								square.changePosition(++previousX, previousY);
								draw(square);
							}
							else
								continue;
							break;
					default: break;
				}
				break;
			}
		}
		if(lastId == 4) // Arkanoid animation
		{
			if(bricksDestroyed < bricksNumber)
			{
				if(isPlaying)
				{
					setBackgroundColour(GraphicsController.BLACK);
					draw(block);
					for(int i=0; i<bricksNumber; i++)
						draw(bricks[i]);
					if(direction) // As explained in collision function
					{
						// Ball Movement. Call collision function if it is about to collide.
						if(sense)
						{
							if((previousX-1 < 0) || (previousY-1 < 0) || isOccupied[previousX-1][previousY-1])
								collision();
							else
							{
								setGridColour(previousX, previousY, GraphicsController.BLACK);
								setGridColour(--previousX, --previousY, GraphicsController.WHITE);
							}
						}
						else
						{
							if((previousX+1 == rows) || (previousY+1 == cols) || isOccupied[previousX+1][previousY+1])
								collision();
							else
							{
								setGridColour(previousX, previousY, GraphicsController.BLACK);
								setGridColour(++previousX, ++previousY, GraphicsController.WHITE);
							}
						}
					}
					else
					{
						if(sense)
						{
							if((previousX+1 == rows) || (previousY-1 < 0) || isOccupied[previousX+1][previousY-1])
								collision();
							else
							{
								setGridColour(previousX, previousY, GraphicsController.BLACK);
								setGridColour(++previousX, --previousY, GraphicsController.WHITE);
							}
						}
						else
						{
							if((previousX-1 < 0) || (previousY+1 == cols) || isOccupied[previousX-1][previousY+1])
								collision();
							else
							{
								setGridColour(previousX, previousY, GraphicsController.BLACK);
								setGridColour(--previousX, ++previousY, GraphicsController.WHITE);
							}
						}
					}
				}
			}
			else
			{
				if(isPlaying)
				{
					setBackgroundColour(GraphicsController.BLACK); // So that the last brick disappears after game ends
					draw(block);
					for(int i=0; i<bricksNumber; i++) 
						draw(bricks[i]);
					System.out.println("You win!");
					isPlaying = false;
				}
			}
		}	
	}
	
	public DemoWindow()
	{
		// Constructor to add 5 buttons with the default settings
		super("Zacharias", 5);
		setButtonText(0, "A");
		setButtonText(1, "B");
		setButtonText(2, "C");
		setButtonText(3, "D");
		setButtonText(4, "E");
		
		rows = getGridWidth();
		cols = getGridHeight();
		isOccupied = new boolean[rows][cols]; // Declarations needed for Arkanoid game extension
		brickIndex = new int[rows][cols];
		bricks = new BrickDrawableObject[35]; // 5x7 = 35 bricks in the game
		this.start();
	}

	public static void main(String[] args)
	{
		new DemoWindow();
	}
}
