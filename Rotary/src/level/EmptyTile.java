package level;

/**
 * A blank tile that is good for nothing except taking up space.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15, 2013
 */
public class EmptyTile extends FixedTile
{
	private static final long serialVersionUID = 2325300788061464959L;

	/**
	 * Creates an EmptyTile.
	 */
	public EmptyTile()
	{
		super(EMPTY, new int[4]);
	}

	/**
	 * Returns the fact that the player cannot enter this tile.
	 * 
	 * @return false since the player cannot enter an EmptyTile.
	 */
	public boolean canEnter()
	{
		return false;
	}

	/**
	 * Returns a new EmptyTile (identical to this one of course).
	 * 
	 * @return a new EmptyTile.
	 */
	public EmptyTile clone()
	{
		return new EmptyTile();
	}

	/**
	 * Gives the name of this tile.
	 * 
	 * @return the name of this tile.
	 */
	public String getTileName()
	{
		return "empty";
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{
		return super.hashCode() * 7;
	}

	/**
	 * This cannot be recoloured.
	 */
	public void recolour(int newColour)
	{

	}

	/**
	 * This cannot rotate.
	 */
	public void rotateLeft()
	{

	}
	
	/**
	 * This cannot rotate.
	 */
	public void rotateRight()
	{

	}
}
