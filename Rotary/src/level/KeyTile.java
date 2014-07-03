package level;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * An action tile that, when activated, toggles the locked states of all
 * LockedTiles.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15, 2013
 */
public class KeyTile extends ActionTile
{
	private static final long serialVersionUID = -7058118636297512098L;

	/**
	 * Creates a KeyTile that has the regular tile properties and a target
	 * colour.
	 * 
	 * @param tileColour the colour of this tile.
	 * @param targetColour the target colour of the action.
	 * @param connectors the colours of the connectors.
	 * @param row the row this tile is in.
	 * @param col the column this tile is in.
	 */
	public KeyTile(int tileColour, int targetColour, int[] connectors, int row, int col)
	{
		super(tileColour, targetColour, connectors, row, col);
	}

	/**
	 * Constructs a new KeyTile based off the given one.
	 * 
	 * @param keyTile the KeyTile to duplicate.
	 */
	public KeyTile(KeyTile keyTile)
	{
		super(keyTile);
	}

	/**
	 * Toggles the locked states of all LockedTiles of the target colour on the
	 * given level.
	 * 
	 * @param level the level to affect.
	 */
	public void activate(Level level)
	{
		int width = level.getWidth();
		int height = level.getHeight();
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++) {
				Tile tile = level.tileAt(r, c);
				if (tile instanceof LockedTile
						&& tile.isSameColour(targetColour))
					((LockedTile) tile).toggleLock();
			}
	}

	/**
	 * Checks whether there are any LockedTiles of the target colour to toggle
	 * in the given level.
	 * 
	 * @param level the level to check.
	 * @return true if there are LockedTiles that will toggle, and false it
	 *         there would be no changes.
	 */
	public boolean canActivate(Level level)
	{
		int width = level.getWidth();
		int height = level.getHeight();
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++) {
				Tile tile = level.tileAt(r, c);
				if (tile instanceof LockedTile
						&& tile.isSameColour(targetColour))
					return true;
			}
		return false;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a KeyTile identical to this one.
	 */
	public KeyTile clone()
	{
		return new KeyTile(this);
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
		((Graphics2D) g).drawImage(keyIcon[targetColour], x, y, null);
	}

	/**
	 * Gives the name of this tile type.
	 * 
	 * @return the name of this tile.
	 */
	public String getTileName()
	{
		return "keyTile";
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 3;
	}
}
