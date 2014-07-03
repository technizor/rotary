package level;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * An action tile that, when activated, repaints all tiles of a target colour to
 * the same colour as this tile.
 * 
 * @author Sherman Ying and Austin Tripp.
 * @version June 15, 2013
 */
public class PaintTile extends ActionTile
{
	private static final long serialVersionUID = 2433452684069234263L;

	/**
	 * Constructs a PaintTile with normal tile properties and a target colour.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param targetColour the target colour of the painter.
	 * @param connectors the connectors of this tile.
	 * @param row the row this tile is in.
	 * @param col the column this tile is in.
	 */
	public PaintTile(int tileColour, int targetColour, int[] connectors, int row, int col)
	{
		super(tileColour, targetColour, connectors, row, col);
	}

	/**
	 * Constructs a new PaintTile based off the given one.
	 * 
	 * @param paintTile the PaintTile to duplicate.
	 */
	public PaintTile(PaintTile paintTile)
	{
		super(paintTile);
	}

	/**
	 * Repaints all of the tiles of the target colour in the given level.
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
				if (tile.isSameColour(targetColour))
					tile.recolour(getColour());
			}
		int newColour = targetColour;
		int newTarget = getColour();
		recolour(newColour);
		retarget(newTarget);
	}

	/**
	 * Checks whether activating this tile would change anything on the given
	 * level.
	 * 
	 * @param level the level to check.
	 * @return true if a change would be made, and false if no changes would be
	 *         made.
	 */
	public boolean canActivate(Level level)
	{
		int width = level.getWidth();
		int height = level.getHeight();
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++)
				if (level.tileAt(r, c).isSameColour(targetColour))
					return true;

		return false;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a PaintTile identical to this one.
	 */
	public PaintTile clone()
	{
		return new PaintTile(this);
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
		((Graphics2D) g).drawImage(paintIcon[getColour()], x, y, null);
		((Graphics2D) g).drawImage(paintBrushIcon[targetColour], x, y, null);
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 19;
	}
}
