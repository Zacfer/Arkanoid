
public class BrickDrawableObject extends DrawableObject 
{

	private int colour, coordX, coordY;
	private boolean isDestroyed = false;
	
	public BrickDrawableObject(int coordX, int coordY, int colour)
	{
		this.colour = colour;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public void draw(GraphicsController arg0) 
	{
		if(!isDestroyed) // Draw Brick only if it hasn't been destroyed. Brick consists of 6 pixels, 3 in 2 rows.
		{
		for(int x = coordX-1; x < coordX+2; x++)
			for(int y = coordY; y < coordY+2; y++)
				arg0.setGridColour(x, y, colour);
		}
	}
	
	public boolean[][] destroy(boolean[][] isOccupied)
	{
		isDestroyed = true; // Set the brick's state to destroyed, and return the updated isOccupied bitmap.
		for(int x = coordX-1; x < coordX+2; x++)
			for(int y = coordY; y < coordY+2; y++)
				isOccupied[x][y] = false;
		return isOccupied;
	}
}
