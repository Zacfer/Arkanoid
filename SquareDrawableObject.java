
public class SquareDrawableObject extends DrawableObject
{
	private int colour, coordX, coordY;
	
	public SquareDrawableObject(int coordX, int coordY, int colour)
	{
		this.colour = colour;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public void draw(GraphicsController arg0) 
	{
		for(int x = coordX-1; x < coordX+2; x++) // Draw the square by beginning from (center-1, center-1)
			for(int y = coordY-1; y < coordY+2; y++)
				arg0.setGridColour(x, y, colour);
		
	}
	
	public void changePosition(int newX, int newY)
	{
		coordX = newX;
		coordY = newY;
	}
}
