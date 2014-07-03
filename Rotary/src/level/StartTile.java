package level;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A start tile is a special static tile that the player starts at.
 * 
 * @author Sherman Ying and Austin Tripp.
 * @version June 15, 2013
 */
public class StartTile extends StaticTile
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a StartTile with the regular tile properties.
	 * 
	 * @param tileColour the tile colour.
	 * @param connectors the colours of the connectors.
	 */
	public StartTile(int tileColour, int[] connectors)
	{
		super(tileColour, connectors);
	}

	/**
	 * Constructs a new StartTile based off the given one.
	 * 
	 * @param startTile the StartTile to duplicate.
	 */
	public StartTile(StartTile startTile)
	{
		super(startTile);
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a StartTile identical to this one.
	 */
	public StartTile clone()
	{
		return new StartTile(this);
	}

	/**
	 * Draws the tile to the given graphics context at the given position.
	 * 
	 * @param g the graphics context to draw to.
	 * @param x the x-position to draw at.
	 * @param y the y-position to draw at.
	 */
	public void drawTile(Graphics g, int x, int y)
	{
		super.drawTile(g, x, y);
		((Graphics2D) g).drawImage(startIcon, x, y, null);
	}
	
	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 23;
	}
}
