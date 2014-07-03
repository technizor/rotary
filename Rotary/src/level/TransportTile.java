package level;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * An action tile that, when activated, transports a player to another transport
 * tile of the target colour.
 * 
 * @author Sherman Ying and Austin Tripp.
 * @version June 15, 2013
 */
public class TransportTile extends ActionTile
{
	private static final long serialVersionUID = 2136621340176975356L;

	/**
	 * Constructs a TransportTile with normal tile properties and a target
	 * colour.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param targetColour the target colour of the transporter.
	 * @param connectors the connectors of this tile.
	 * @param row the row this tile is in.
	 * @param col the column this tile is in.
	 */
	public TransportTile(int tileColour, int targetColour, int[] connectors,
			int row, int col)
	{
		super(tileColour, targetColour, connectors, row, col);
	}

	/**
	 * Constructs a new TransportTile based off the given one.
	 * 
	 * @param transportTile the TransportTile to duplicate.
	 */
	public TransportTile(TransportTile transportTile)
	{
		super(transportTile);
	}

	/**
	 * Transports the player in the given level to another transporter.
	 * 
	 * @param level the level to affect.
	 */
	public void activate(Level level)
	{
		int width = level.getWidth();
		int height = level.getHeight();
		ArrayList<int[]> transporterPositions = new ArrayList<int[]>();
		int currentIndex = -1;

		// Find every transport tile and search for valid targets.
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				Tile t = level.tileAt(r, c);
				if (t instanceof TransportTile) {
					if (t.isSameColour(getTargetColour())
							&& !(r == row && c == col))
						transporterPositions.add(new int[] { r, c });
					else if (r == row && c == col)
						currentIndex = transporterPositions.size();
				}
			}
		}
		int[] target = transporterPositions.get(currentIndex
				% transporterPositions.size());
		level.movePlayerTo(target[0], target[1]);
	}

	/**
	 * Checks whether there is a valid target transporter in the given level.
	 * 
	 * @param level the level to check.
	 * @return true if there is a valid transport location, and false if there
	 *         is not.
	 */
	public boolean canActivate(Level level)
	{
		int width = level.getWidth();
		int height = level.getHeight();

		// Find any valid target.
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				Tile t = level.tileAt(r, c);
				if (t instanceof TransportTile
						&& t.isSameColour(getTargetColour())
						&& !(r == row && c == col))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a TransportTile identical to this one.
	 */
	public TransportTile clone()
	{
		return new TransportTile(this);
	}

	/**
	 * Draws this tile to the given graphics context at the given position.
	 * 
	 * @param g the graphics context to draw to.
	 * @param x the x-position to draw at.
	 * @param y the y-position to draw at.
	 */
	public void drawTile(Graphics g, int x, int y)
	{
		super.drawTile(g, x, y);
		((Graphics2D) g)
				.drawImage(transportIcon[getTargetColour()], x, y, null);
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 37;
	}
}
