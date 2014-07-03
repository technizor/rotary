package level;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A special tile that can be locked or unlocked through the use of a key tile.
 * 
 * @author Sherman Ying
 * @version June 15, 2013
 */
public class LockedTile extends FixedTile
{
	private boolean unlocked;
	private static final long serialVersionUID = 6398636759466744040L;

	/**
	 * Creates a LockedTile that has the regular tile properties and is locked
	 * by default.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param connectors the colours of the connectors.
	 */
	public LockedTile(int tileColour, int[] connectors)
	{
		super(tileColour, connectors);
	}

	/**
	 * Constructs a new LockedTile based off the given one.
	 * 
	 * @param lockedTile the LockedTile to duplicate.
	 */
	public LockedTile(LockedTile lockedTile)
	{
		super(lockedTile);
		unlocked = lockedTile.unlocked;
	}

	/**
	 * Checks whether this tile is free to rotate.
	 * 
	 * @return true if the tile is unlocked, and false if it is locked.
	 */
	public boolean canRotate()
	{
		return unlocked;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a LockedTile identical to this one.
	 */
	public LockedTile clone()
	{
		return new LockedTile(this);
	}

	/**
	 * Draws the tile in the given graphics context.
	 * 
	 * @param g the graphics context to draw to.
	 * @param x the x-position to draw at.
	 * @param y the y-position to draw at.
	 */
	public void drawTile(Graphics g, int x, int y)
	{
		super.drawTile(g, x, y);
		if (unlocked)
			((Graphics2D) g).drawImage(unlockIcon[getColour()], x, y, null);
		else
		{
			((Graphics2D) g).drawImage(staticIcon, x, y, null);
			((Graphics2D) g).drawImage(lockIcon[getColour()], x, y, null);
		}
	}

	/**
	 * Checks for equality with the given object.
	 * 
	 * @param other the object to check for equality with.
	 * @return true if the other object is not null, is a LockedTile, has the
	 *         same lock state, and has the same tile colour and connector
	 *         colours. Returns false in all other cases.
	 */
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (other.getClass() == getClass()) {
			LockedTile tile = (LockedTile) other;
			if (tile.unlocked != unlocked)
				return false;
			return super.equals(tile);
		}
		return false;
	}

	/**
	 * Gives the name of this tile.
	 * 
	 * @return the name of this tile.
	 */
	public String getTileName()
	{
		return "locked";
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		int hashCode = unlocked ? 13 : 17;
		return super.hashCode() * hashCode;
	}

	/**
	 * Reads the tile colour, connector colours, and locked/unlocked state of
	 * this tile.
	 * 
	 * @param in the input stream to read the object from.
	 * @throws IOException then the input stream cannot be read.
	 * @throws ClassNotFoundException when the tile data has been saved in the
	 *             wrong order.
	 */
	public void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		super.readObject(in);
		unlocked = in.readBoolean();
	}

	public void rotateLeft()
	{
		if (unlocked)
			super.rotateLeft();
	}

	public void rotateRight()
	{
		if (unlocked)
			super.rotateRight();
	}

	/**
	 * Toggles the lock state of the tile.
	 */
	public void toggleLock()
	{
		unlocked = !unlocked;
	}

	/**
	 * Writes the tile colour, connector colours, and locked/unlocked state of
	 * this tile.
	 * 
	 * @param out the output stream to write to.
	 * @throws IOException when unable to save to the output stream.
	 */
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		super.writeObject(out);
		out.writeBoolean(unlocked);
	}
}
