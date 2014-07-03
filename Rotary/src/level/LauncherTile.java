package level;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * An action tile that, when activated, launches the player two tiles in one
 * direction.
 * 
 * @author Austin Tripp and Sherman Ying
 * @version June 15, 2013
 */
public class LauncherTile extends ActionTile
{

	private static final long serialVersionUID = 01010101001L;

	// Stores the launching direction.
	private int direction;// URDL

	/**
	 * Constructs a new LauncherTile.
	 * 
	 * @param tileColour this tile's colour.
	 * @param targetColour the colour that it targets.
	 * @param connectors the colours of its 4 connectors.
	 * @param row the row this tile is in.
	 * @param col the column this tile is in.
	 */
	public LauncherTile(int tileColour, int targetColour, int[] connectors,
			int row, int col)
	{
		super(tileColour, targetColour, connectors, row, col);
		direction = 0;
	}

	/**
	 * Constructs a new LauncherTile based off the given one.
	 * 
	 * @param launcherTile the launcherTile to duplicate.
	 */
	public LauncherTile(LauncherTile launcherTile)
	{
		super(launcherTile);
		direction = launcherTile.direction;
	}

	/**
	 * Launches the player 2 tiles away from this tile in the direction it is
	 * facing.
	 * 
	 * @param level the current level of this.
	 */
	public void activate(Level level)
	{
		if (direction == UP && row - 2 >= 0
				&& level.tileAt(row - 2, col).canEnter()) {
			level.movePlayerTo(row - 2, col);
			return;
		}
		if (direction == RIGHT && col + 2 < level.getWidth()
				&& level.tileAt(row, col + 2).canEnter()) {
			level.movePlayerTo(row, col + 2);
			return;
		}
		if (direction == DOWN && row + 2 < level.getHeight()
				&& level.tileAt(row + 2, col).canEnter()) {
			level.movePlayerTo(row + 2, col);
			return;
		}
		if (direction == LEFT && col - 2 >= 0
				&& level.tileAt(row, col - 2).canEnter()) {
			level.movePlayerTo(row, col - 2);
			return;
		}
	}

	/**
	 * Checks to see whether this tile can be activated.
	 * 
	 * @param level the current level.
	 * @return true if it can be activated, otherwise false.
	 */
	public boolean canActivate(Level level)
	{
		if (direction == UP && row - 2 >= 0
				&& level.tileAt(row - 2, col).canEnter())
			return true;
		if (direction == RIGHT && col + 2 < level.getWidth()
				&& level.tileAt(row, col + 2).canEnter())
			return true;
		if (direction == DOWN && row + 2 < level.getHeight()
				&& level.tileAt(row + 2, col).canEnter())
			return true;
		if (direction == LEFT && col - 2 >= 0
				&& level.tileAt(row, col - 2).canEnter())
			return true;
		return false;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a LauncherTile identical to this one.
	 */
	public LauncherTile clone()
	{
		return new LauncherTile(this);
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
		((Graphics2D) g).drawImage(launcherIcon[direction], x, y, null);
	}

	/**
	 * Checks for equality with the given object.
	 * 
	 * @param other the object to check for equality with
	 * @return true if the other object is not null, is a LauncherTile, is
	 *         facing the same direction, and has the same tile colour and
	 *         connector colours. Returns false in all other cases.
	 */
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (other.getClass() == getClass()) {
			LauncherTile tile = (LauncherTile) other;
			if (tile.direction != direction)
				return false;
			return super.equals(tile);
		}
		return false;
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 11;
	}

	/**
	 * Reads in tile variables from an ObjectInputStream.
	 */
	public void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		super.readObject(in);
		direction = in.readInt();
	}

	/**
	 * Rotates this tile 90 degrees left.
	 */
	public void rotateLeft()
	{
		direction += 3;
		direction %= 4;
		super.rotateLeft();
	}

	/**
	 * Rotates this tile 90 degrees right.
	 */
	public void rotateRight()
	{
		direction++;
		direction %= 4;
		super.rotateRight();
	}

	/**
	 * Writes the tile variables to an ObjectOutputStream.
	 */
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		super.writeObject(out);
		out.writeInt(direction);
	}
}
