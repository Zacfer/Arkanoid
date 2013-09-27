public class BlockDrawableObject extends DrawableObject
{
	private int colour, coordX, coordY;
	
	public BlockDrawableObject(int coordX, int coordY, int colour)
	{
		this.colour = colour;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public void draw(GraphicsController arg0) 
	{
		// Player's block consists of 7 "pixels". First and last are painted blue, inner ones red.
		arg0.setGridColour(coordX-3, coordY, GraphicsController.RED);
		for(int x = coordX-2; x < coordX+3; x++)
			arg0.setGridColour(x, coordY, colour);
		arg0.setGridColour(coordX+3, coordY, GraphicsController.RED);
	}
	
	public void changePosition(int newX, int newY)
	{
		coordX = newX;
		coordY = newY;
	}
}