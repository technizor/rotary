package level;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Stores an entire level of the game.
 * 
 * @author Austin Tripp and Sherman Ying
 * @version June 15, 2013
 */
public class Level implements Serializable
{
	private static final long serialVersionUID = 9001L;

	// View sizing constants.
	public final static int VIEW_ROWS = 8;
	public final static int VIEW_COLS = 12;

	// Player data.
	private Player player;

	// Tile data.
	private Tile[][] map;

	// Name of the level.
	private String name;

	/**
	 * Reloads all tile textures from the given texture pack. If the selected
	 * pack is missing a texture, this will load the default image.
	 * 
	 * @param texturePack the name of the texture pack to load.
	 * @throws IOException when the default texture pack is missing.
	 */
	public static void loadImages(String texturePack) throws IOException
	{
		Player.loadImages(texturePack);
		Tile.loadImages(texturePack);
	}

	/**
	 * Constructs a new Level based on the given Level.
	 * 
	 * @param clone the level to duplicate.
	 */
	public Level(Level clone)
	{
		map = new Tile[clone.getHeight()][clone.getWidth()];
		for (int row = 0; row < map.length; row++)
			for (int col = 0; col < map[row].length; col++)
				map[row][col] = clone.map[row][col].clone();
		player = new Player(clone.player);
		name = clone.name;
		// Not copied since there is no need (Strings are immutable).
	}

	/**
	 * Constructs a new level object.
	 * 
	 * @param tiles the array of tiles in the level.
	 * @param levelName the name of this level.
	 */
	public Level(Tile[][] tiles, String levelName)
	{
		// Set the name.
		name = levelName;

		// Crop the level bounds and set to a minimum.
		int firstRow = tiles.length;
		int firstCol = tiles[0].length;
		int lastRow = -1;
		int lastCol = -1;
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				Tile t = tiles[row][col];
				// Ignore EmptyTiles and null tiles when determining the crop
				// position.
				if (t != null && !(t instanceof EmptyTile)) {
					if (row < firstRow)
						firstRow = row;
					if (col < firstCol)
						firstCol = col;
					if (row > lastRow)
						lastRow = row;
					if (col > lastCol)
						lastCol = col;
				}
			}
		}
		int noOfRows = lastRow - firstRow + 1 > VIEW_ROWS ? lastRow - firstRow
				+ 1 : VIEW_ROWS;
		int noOfCols = lastCol - firstCol + 1 > VIEW_COLS ? lastCol - firstCol
				+ 1 : VIEW_COLS;
		map = new Tile[noOfRows][noOfCols];

		// Create the map and set the player's starting position.
		int startRow = 0;
		int startCol = 0;
		for (int row = 0; row < map.length; row++)
			for (int col = 0; col < map[row].length; col++) {
				// Fill any null tiles as EmptyTiles.
				if (row + firstRow > lastRow || col + firstCol > lastCol
						|| tiles[row + firstRow][col + firstCol] == null)
					map[row][col] = new EmptyTile();
				// Copy valid tiles into place.
				else if (tiles[row + firstRow][col + firstCol] != null) {
					map[row][col] = tiles[row + firstRow][col + firstCol];
					if (map[row][col] instanceof StartTile) {
						startRow = row;
						startCol = col;
					}
				}
			}
		player = new Player(startRow, startCol);
	}

	/**
	 * Activates the current tile.
	 */
	public void activate()
	{
		map[player.getRow()][player.getCol()].activate(this);
	}

	/**
	 * Checks to see whether the current tile can be activated.
	 * 
	 * @return true if the player can activate the current tile, otherwise
	 *         false.
	 */
	public boolean canActivate()
	{
		return map[player.getRow()][player.getCol()].canActivate(this);
	}

	/**
	 * Checks to see whether the player can move down.
	 * 
	 * @return true if the player can move down, otherwise false.
	 */
	public boolean canMoveDown()
	{
		int row = player.getRow();
		int col = player.getCol();
		if (row + 1 >= map.length)
			return false;
		Tile current = map[row][col];
		Tile target = map[row + 1][col];
		return current.canMoveDownTo(target);
	}

	/**
	 * Checks to see whether the player can move left.
	 * 
	 * @return true if the player can move left, otherwise false.
	 */
	public boolean canMoveLeft()
	{
		int row = player.getRow();
		int col = player.getCol();
		if (col - 1 < 0)
			return false;
		Tile current = map[row][col];
		Tile target = map[row][col - 1];
		return current.canMoveLeftTo(target);
	}

	/**
	 * Checks to see whether the player can move right.
	 * 
	 * @return true if the player can move right, otherwise false.
	 */
	public boolean canMoveRight()
	{
		int row = player.getRow();
		int col = player.getCol();
		if (col + 1 >= map[0].length)
			return false;
		Tile current = map[row][col];
		Tile target = map[row][col + 1];
		return current.canMoveRightTo(target);
	}

	/**
	 * Checks to see whether the player can move up.
	 * 
	 * @return true if the player can move up, otherwise false.
	 */
	public boolean canMoveUp()
	{
		int row = player.getRow();
		int col = player.getCol();
		if (row - 1 < 0)
			return false;
		Tile current = map[row][col];
		Tile target = map[row - 1][col];
		return current.canMoveUpTo(target);
	}

	/**
	 * Checks to see whether the current tile can be rotated.
	 * 
	 * @return true if the current tile can be rotated, otherwise false.
	 */
	public boolean canRotate()
	{
		return map[player.getRow()][player.getCol()].canRotate();
	}

	/**
	 * Draws the current level.
	 * 
	 * @param g2D the 2D graphics context.
	 * @param viewX the x position of the top left tile's top left corner.
	 * @param viewY the y position of the top left tile's top left corner.
	 */
	public void draw(Graphics2D g2D, int viewX, int viewY)
	{
		// Calculate the view offset.
		int offsetX = -(viewX % Tile.TILE_WIDTH);
		int offsetY = -(viewY % Tile.TILE_WIDTH);
		int firstR = viewY / Tile.TILE_WIDTH;
		int firstC = viewX / Tile.TILE_WIDTH;

		// Draw any tiles that are fully or partially in the view of the screen.
		for (int row = 0; row <= VIEW_ROWS && row + firstR < map.length; row++)
			for (int col = 0; col <= VIEW_COLS
					&& col + firstC < map[row].length; col++) {
				map[row + firstR][col + firstC].drawTile(g2D, offsetX
						+ Tile.TILE_WIDTH * col, offsetY + Tile.TILE_WIDTH
						* row);
				if (player.getRow() == row + firstR
						&& player.getCol() == col + firstC) {
					player.draw(g2D, offsetX + Tile.TILE_WIDTH * col, offsetY
							+ Tile.TILE_WIDTH * row);
				}
			}

	}

	/**
	 * Checks to see whether a given object is equal to this one.
	 * 
	 * @param obj the object to compare it to.
	 * @return true if obj is a level of the same dimensions with the player in
	 *         the same spot and all tiles equal, otherwise false.
	 */
	public boolean equals(Object obj)
	{

		// Make sure it is a level.
		if (obj == null || !(obj instanceof Level))
			return false;

		// Compare it as a level.
		Level other = (Level) obj;

		// Check if the player is in the same spot.
		if (!player.equals(other.player))
			return false;

		// Check for equal dimensions.
		if (map.length != other.map.length
				|| map[0].length != other.map[0].length)
			return false;

		// Check for all tiles being equal.
		for (int row = 0; row < map.length; row++)
			for (int col = 0; col < map[row].length; col++)
				if (!map[row][col].equals(other.map[row][col]))
					return false;

		// If it has passed all the tests return true.
		return true;
	}

	/**
	 * Finds the height of this level.
	 * 
	 * @return the height (number of rows) in this level.
	 */
	public int getHeight()
	{
		return map.length;
	}

	/**
	 * Returns the position of the player in the level.
	 * 
	 * @return a Point containing the player's position in (x,y) format,
	 *         equivalent to (col, row).
	 */
	public Point getPlayerPosition()
	{
		return new Point(player.getCol(), player.getRow());
	}

	/**
	 * Finds the width of this level.
	 * 
	 * @return the width (number of columns) in this level.
	 */
	public int getWidth()
	{
		return map[0].length;
	}

	/**
	 * Finds a hashCode value for this level.
	 * 
	 * @return and integer hashCode value for this level.
	 */
	public int hashCode()
	{

		/*
		 * Since it is impossible to generate a guaranteed unique integer for
		 * each level, a level's hashCode is generated by finding the hashCodes
		 * of tiles, adding to them, then multiplying them all to the player's
		 * hashCode.
		 */
		int hashCode = player.hashCode();

		// Multiply it by all the tiles' hashCodes.
		for (int row = 0; row < map.length; row++)
			for (int col = 0; col < map[row].length; col++) {
				int tileHash = map[row][col].hashCode();
				tileHash = tileHash * 71 + row;
				tileHash = tileHash * 5 + col;
				hashCode *= tileHash;
			}

		// Return this hashCode.
		return hashCode;
	}

	/**
	 * Checks to see if a given row and column represent the location of a tile
	 * in this level.
	 * 
	 * @param row the row of this index.
	 * @param col the column of this index.
	 * @return true if this index is valid, otherwise false.
	 */
	private boolean isValidIndex(int row, int col)
	{
		if (row < 0 || row >= map.length || col < 0 || col >= map[0].length)
			return false;
		return true;
	}

	/**
	 * Checks to see whether the player has completed the current level.
	 * 
	 * @return true if the player is standing on the end tile, otherwise false.
	 */
	public boolean levelComplete()
	{
		return map[player.getRow()][player.getCol()] instanceof FinishTile;
	}

	/**
	 * Moves the player 1 square down.
	 */
	public void moveDown()
	{
		player.moveDown();
	}

	/**
	 * Moves the player 1 square left.
	 */
	public void moveLeft()
	{
		player.moveLeft();
	}

	/**
	 * Moves the player to the specified position Precondition: the row and
	 * column given are less than getHeight() and getWidth() respectively.
	 * 
	 * @param row the new row for the player.
	 * @param col the new column for the player.
	 */
	public void movePlayerTo(int row, int col)
	{
		player.reposition(row, col);
	}

	/**
	 * Moves the player 1 square right.
	 */
	public void moveRight()
	{
		player.moveRight();
	}

	/**
	 * Moves the player 1 square up.
	 */
	public void moveUp()
	{
		player.moveUp();
	}

	/**
	 * Reads in this level from an input stream.
	 * 
	 * @param in the input stream to read from.
	 * @throws IOException if the stream is broken.
	 * @throws ClassNotFoundException if the objects have been saved in the
	 *             incorrect order.
	 */
	public void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		map = (Tile[][]) in.readObject();
		player = (Player) in.readObject();
		name = (String) in.readObject();
	}

	/**
	 * Rotates the tile 90 degrees to the left.
	 */
	public void rotateLeft()
	{
		int row = player.getRow();
		int col = player.getCol();

		// Rotate this tile left.
		map[row][col].rotateLeft();

		// Rotate the others right.
		if (isValidIndex(row + 1, col))
			map[row + 1][col].rotateRight();
		if (isValidIndex(row - 1, col))
			map[row - 1][col].rotateRight();
		if (isValidIndex(row, col + 1))
			map[row][col + 1].rotateRight();
		if (isValidIndex(row, col - 1))
			map[row][col - 1].rotateRight();
		if (isValidIndex(row + 1, col + 1))
			map[row + 1][col + 1].rotateRight();
		if (isValidIndex(row - 1, col - 1))
			map[row - 1][col - 1].rotateRight();
		if (isValidIndex(row + 1, col - 1))
			map[row + 1][col - 1].rotateRight();
		if (isValidIndex(row - 1, col + 1))
			map[row - 1][col + 1].rotateRight();
	}

	/**
	 * Rotates the tile 90 degrees to the right.
	 */
	public void rotateRight()
	{
		int row = player.getRow();
		int col = player.getCol();

		// Rotate this tile right.
		map[row][col].rotateRight();

		// Rotate the others left.
		if (isValidIndex(row + 1, col))
			map[row + 1][col].rotateLeft();
		if (isValidIndex(row - 1, col))
			map[row - 1][col].rotateLeft();
		if (isValidIndex(row, col + 1))
			map[row][col + 1].rotateLeft();
		if (isValidIndex(row, col - 1))
			map[row][col - 1].rotateLeft();
		if (isValidIndex(row + 1, col + 1))
			map[row + 1][col + 1].rotateLeft();
		if (isValidIndex(row - 1, col - 1))
			map[row - 1][col - 1].rotateLeft();
		if (isValidIndex(row + 1, col - 1))
			map[row + 1][col - 1].rotateLeft();
		if (isValidIndex(row - 1, col + 1))
			map[row - 1][col + 1].rotateLeft();
	}

	/**
	 * Finds and return the tile at the specified index Precondition: the given
	 * row and column are >= 0 and less than getWidth() and getHeight()
	 * respectively.
	 * 
	 * @param row the row of the desired tile.
	 * @param col the column of the desired tile.
	 * @return the tile on the grid at (row, col).
	 */
	public Tile tileAt(int row, int col)
	{
		return map[row][col];
	}

	/**
	 * Outputs the contents of this level to a String.
	 * 
	 * @return a String containing the name of this level.
	 */
	public String toString()
	{
		return name;
	}

	/**
	 * Writes this level to the target ObjectOutputStream.
	 * 
	 * @param out the output stream to write to.
	 * @throws IOException when the output stream is broken.
	 */
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeObject(map);
		out.writeObject(player);
		out.writeObject(name);
	}
}
