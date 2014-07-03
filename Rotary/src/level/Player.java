package level;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Stores the information for the player the user moves around in the game's
 * environment.
 * 
 * @author Austin Tripp and Sherman Ying.
 * @version June 15th 2013
 */
public class Player implements Serializable
{

	private static final long serialVersionUID = 8000000000L;

	public static BufferedImage playerImg;

	// Player positioning data.
	private int row;
	private int col;

	/**
	 * Reloads the player textures from the given texture pack. If the selected
	 * pack is missing a texture, this will load the default image.
	 * 
	 * @param texturePack the name of the texture pack to load.
	 * @throws IOException thrown when the default texture pack is missing.
	 */
	public static void loadImages(String texturePack) throws IOException
	{
		String root = "textures\\" + texturePack;
		playerImg = ImageIO.read(new File(root + "\\player\\player.png"));
	}

	/**
	 * Constructs a new Player at the specified location.
	 * 
	 * @param row the starting row of the player.
	 * @param col the starting column of this player.
	 */
	public Player(int row, int col)
	{
		this.row = row;
		this.col = col;
	}

	/**
	 * Constructs a new Player.
	 * 
	 * @param clone the player of which to construct an identical clone.
	 */
	public Player(Player clone)
	{
		row = clone.row;
		col = clone.col;
	}

	/**
	 * Draws this player on the screen.
	 * 
	 * @param g the graphics context.
	 * @param x the top row being displayed.
	 * @param y the leftmost column being displayed.
	 */
	public void draw(Graphics g, int x, int y)
	{
		((Graphics2D) g).drawImage(playerImg, x, y, null);
	}

	/**
	 * Checks to see if this Player is equal to another object.
	 * 
	 * @param other the other player to compare it to.
	 * @return true if other has the same row and column as this player,
	 *         otherwise false.
	 */
	public boolean equals(Player other)
	{
		if (other.row == row && other.col == col)
			return true;
		return false;
	}

	/**
	 * Finds the current column of this player.
	 * 
	 * @return the column this Player is currently in.
	 */
	public int getCol()
	{
		return col;
	}

	/**
	 * Finds the current row of this player.
	 * 
	 * @return the row this Player is currently in.
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Returns a hashCode value based on the position of this player.
	 * 
	 * @return an integer hashCode, unique for just about every row and column.
	 */
	public int hashCode()
	{
		return row * 35_923 + col * 7 + 1;
		// So row and column are distinct and not 0.
	}

	/**
	 * Moves the player down 1 tile.
	 */
	public void moveDown()
	{
		row++;
	}

	/**
	 * Moves the player left 1 tile.
	 */
	public void moveLeft()
	{
		col--;
	}

	/**
	 * Moves the player right 1 tile.
	 */
	public void moveRight()
	{
		col++;
	}

	/**
	 * Moves the player up 1 tile.
	 */
	public void moveUp()
	{
		row--;
	}

	/**
	 * Repositions the player to the given row and column.
	 * 
	 * @param newRow the new row that the player will occupy.
	 * @param newCol the new column that the player will occupy.
	 */
	public void reposition(int newRow, int newCol)
	{
		row = newRow;
		col = newCol;
	}
}
