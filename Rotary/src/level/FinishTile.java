package level;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A special tile that, when moved to, will complete the level.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15, 2013
 */
public class FinishTile extends StaticTile
{
	private static final long serialVersionUID = 3662810088971416341L;

	/**
	 * Constructs a new FinishTile based off the given one.
	 * 
	 * @param finishTile the FinishTile to duplicate.
	 */
	public FinishTile(FinishTile finishTile)
	{
		super(finishTile);
	}

	/**
	 * Creates a FinishTile with the regular tile properties.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param connectors the colours of the connectors.
	 */
	public FinishTile(int tileColour, int[] connectors)
	{
		super(tileColour, connectors);
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a FinishTile identical to this one.
	 */
	public FinishTile clone()
	{
		return new FinishTile(this);
	}

	/**
	 * Draws the tile to the given graphics context at the given location.
	 * 
	 * @param g the graphics context to draw to.
	 * @param x the x-position to draw at.
	 * @param y the y-position to draw at.
	 */
	public void drawTile(Graphics g, int x, int y)
	{
		super.drawTile(g, x, y);
		((Graphics2D) g).drawImage(finishIcon, x, y, null);
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 5;
	}

}
