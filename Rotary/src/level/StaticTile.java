package level;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A Tile that cannot be rotated in any way.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15, 2013
 */
public class StaticTile extends FixedTile
{
	private static final long serialVersionUID = -2331181580932045408L;

	/**
	 * Constructs an tile that cannot be rotated.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param connectors the colours of the connectors.
	 */
	public StaticTile(int tileColour, int[] connectors)
	{
		super(tileColour, connectors);
	}

	/**
	 * Constructs a new StaticTile based off the given one.
	 * 
	 * @param staticTile the StaticTile to duplicate.
	 */
	public StaticTile(StaticTile staticTile)
	{
		super(staticTile);
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a StaticTile identical to this one.
	 */
	public StaticTile clone()
	{
		return new StaticTile(this);
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
		((Graphics2D) g).drawImage(staticIcon, x, y, null);
	}

	/**
	 * Gives the name of this tile type.
	 * 
	 * @return the name of this tile.
	 */
	public String getTileName()
	{
		return "staticTile";
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 29;
	}

	/**
	 * Rotating is disabled for a static tile.
	 */
	public void rotateLeft()
	{

	}

	/**
	 * Rotating is disabled for a static tile.
	 */
	public void rotateRight()
	{

	}
}
