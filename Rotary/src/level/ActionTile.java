package level;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * An ActionTile is a tile that can be activated for a certain effect on the
 * Level.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15, 2013
 */
public abstract class ActionTile extends Tile
{
	private static final long serialVersionUID = -8523786652993762957L;

	// Stores its position and target colour.
	protected int targetColour;
	protected int row;
	protected int col;

	/**
	 * Creates an ActionTile based on the given ActionTile.
	 * 
	 * @param clone the ActionTile to duplicate.
	 */
	public ActionTile(ActionTile clone)
	{
		super(clone);
		targetColour = clone.targetColour;
		row = clone.row;
		col = clone.col;
	}

	/**
	 * Creates an ActionTile with the regular tile properties and a target
	 * colour.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param targetColour the target colour of the action tile.
	 * @param connectors the connectors of the tile.
	 * @param row the row this tile is in.
	 * @param col the column this tile is in.
	 */
	public ActionTile(int tileColour, int targetColour, int[] connectors,
			int row, int col)
	{
		super(tileColour, connectors);
		this.targetColour = targetColour;
		this.row = row;
		this.col = col;
	}

	/**
	 * Activates the tile effect in the given level.
	 */
	public abstract void activate(Level level);

	/**
	 * Checks whether this tile can be activated in the given level.
	 */
	public abstract boolean canActivate(Level level);

	/**
	 * Checks for equality with the given object.
	 * 
	 * @param other the object to check for equality with.
	 * @return true if the other object is not null, is an ActionTile, has the
	 *         same row and column, has the same target colour, and has the same
	 *         tile colour and connector colours. Returns false in all other
	 *         cases.
	 */
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (other.getClass() == getClass()) {
			ActionTile tile = (ActionTile) other;
			if (row != tile.row || col != tile.col
					|| targetColour != tile.targetColour)
				return false;
			return super.equals(tile);
		}
		return false;
	}

	/**
	 * Gives the target colour of the action tile.
	 * 
	 * @return the target colour of this tile.
	 */
	public int getTargetColour()
	{
		return targetColour;
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() + targetColour * 7;
	}

	/**
	 * Reads the tile colour, connector colours, and the target colour.
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
		targetColour = in.readInt();
		row = in.readInt();
		col = in.readInt();
	}

	/**
	 * Sets the target colour of the action tile.
	 * 
	 * @param targetColour the colour to set the target colour to.
	 */
	public void retarget(int targetColour)
	{
		this.targetColour = targetColour;
	}

	/**
	 * Writes the tile colour, connector colours, and target colour.
	 * 
	 * @param out the output stream to write to.
	 * @throws IOException when unable to save to the output stream.
	 */
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		super.writeObject(out);
		out.writeInt(targetColour);
		out.writeInt(row);
		out.writeInt(col);

	}
}
