package level;

/**
 * A special tile that normally cannot be rotated.
 * 
 * @author Sherman Ying
 * @version June 5, 2013
 */
public abstract class FixedTile extends Tile
{
	private static final long serialVersionUID = 7803840398337037173L;

	/**
	 * Constructs a new FixedTile identical to the given one.
	 * 
	 * @param fixedTile the tile to duplicate.
	 */
	public FixedTile(FixedTile fixedTile)
	{
		super(fixedTile);
	}

	/**
	 * Creates a FixedTile with the regular tile properties.
	 * 
	 * @param tileColour the colour of the tile.
	 * @param connectors the colours of the connectors.
	 */
	public FixedTile(int tileColour, int[] connectors)
	{
		super(tileColour, connectors);
	}

	/**
	 * By default, FixedTiles cannot rotate.
	 * 
	 * @return false because FixedTiles cannot rotate.
	 */
	public boolean canRotate()
	{
		return false;
	}
}
