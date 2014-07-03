package level;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Generic tile object used in a level.
 * 
 * @author Sherman Ying and Austin Tripp.
 * @version June 9, 2013
 */
public class Tile implements Serializable
{

	private static final long serialVersionUID = -7850243351880280695L;

	// Images.
	private static BufferedImage[] tileBase;
	private static BufferedImage[] connectorBase;
	protected static BufferedImage[] lockIcon;
	protected static BufferedImage[] unlockIcon;
	protected static BufferedImage[] keyIcon;
	protected static BufferedImage[] paintBrushIcon;
	protected static BufferedImage[] paintIcon;
	protected static BufferedImage[] transportIcon;
	protected static BufferedImage finishIcon;
	protected static BufferedImage[] launcherIcon;
	protected static BufferedImage staticIcon;
	protected static BufferedImage startIcon;

	// Size of tiles.
	public static final int TILE_WIDTH = 64;

	// Codes for the tile colours.
	public static final int EMPTY = 0;
	public static final int BLUE = 1;
	public static final int RED = 2;
	public static final int YELLOW = 3;
	public static final int GREEN = 4;
	public static final int PURPLE = 5;
	public static final int ORANGE = 6;
	public static final int GRAY = 7;

	// Codes for movements and relationships between tiles.
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	/**
	 * Reloads all tile textures from the given texture pack. If the selected
	 * pack is missing a texture, this will load the default image.
	 * 
	 * @param texturePack the name of the texture pack to load.
	 * @throws IOException thrown when the default texture pack is missing.
	 */
	public static void loadImages(String texturePack) throws IOException
	{
		String root = "textures\\" + texturePack;
		tileBase = new BufferedImage[8];
		connectorBase = new BufferedImage[8];
		lockIcon = new BufferedImage[8];
		unlockIcon = new BufferedImage[8];
		keyIcon = new BufferedImage[8];
		paintBrushIcon = new BufferedImage[8];
		paintIcon = new BufferedImage[8];
		transportIcon = new BufferedImage[8];
		finishIcon = ImageIO.read(new File(root + "\\tile\\finish.png"));
		launcherIcon = new BufferedImage[4];
		staticIcon = ImageIO.read(new File(root + "\\tile\\static.png"));
		startIcon = ImageIO.read(new File(root + "\\tile\\start.png"));
		for (int direction = 0; direction < 4; direction++) {
			launcherIcon[direction] = ImageIO.read(new File(root
					+ "\\tile\\launcher" + direction + ".png"));
		}

		for (int colour = 0; colour < 8; colour++) {
			tileBase[colour] = ImageIO.read(new File(root + "\\tile\\tile"
					+ colour + ".png"));
			connectorBase[colour] = ImageIO.read(new File(root
					+ "\\tile\\connector" + colour + ".png"));
			lockIcon[colour] = ImageIO.read(new File(root + "\\tile\\lock"
					+ colour + ".png"));
			unlockIcon[colour] = ImageIO.read(new File(root + "\\tile\\unlock"
					+ colour + ".png"));
			keyIcon[colour] = ImageIO.read(new File(root + "\\tile\\key"
					+ colour + ".png"));
			paintBrushIcon[colour] = ImageIO.read(new File(root
					+ "\\tile\\paintBrush" + colour + ".png"));
			paintIcon[colour] = ImageIO.read(new File(root + "\\tile\\paint"
					+ colour + ".png"));
			transportIcon[colour] = ImageIO.read(new File(root
					+ "\\tile\\transport" + colour + ".png"));
		}
	}

	// Tile data.
	private int[] connectors;
	private int tileColour;

	/**
	 * Constructs a tile and loads the images.
	 * 
	 * @param tileColour the colour of the tile panel.
	 * @param connectors the colours of the tile connectors.
	 */
	public Tile(int tileColour, int[] connectors)
	{
		this.tileColour = tileColour;
		this.connectors = new int[4];
		for (int dir = 0; dir < 4; dir++)
			this.connectors[dir] = connectors[dir];
	}

	/**
	 * Constructs a new Tile based on the given one.
	 * 
	 * @param clone the Tile to duplicate.
	 */
	public Tile(Tile clone)
	{
		tileColour = clone.tileColour;
		connectors = new int[4];
		System.arraycopy(clone.connectors, 0, connectors, 0, 4);
	}

	/**
	 * Triggers an action on the level.
	 */
	public void activate(Level level)
	{
	}

	/**
	 * Checks whether the conditions for activating this tile are met.
	 * 
	 * @return true if the tile can be activated, and false if it cannot.
	 */
	public boolean canActivate(Level level)
	{
		return false;
	}

	/**
	 * Checks whether this tile can be entered.
	 * 
	 * @return true if this tile can be entered by the player, and false if it
	 *         cannot.
	 */
	public boolean canEnter()
	{
		return true;
	}

	/**
	 * Checks whether the player can move to the down to the given target tile.
	 * 
	 * @param target the tile to check.
	 * @return true if the connectors match in colour, false if they do not, the
	 *         target cannot be entered, or either of the connectors are 0.
	 */
	public boolean canMoveDownTo(Tile target)
	{
		return connectors[2] == target.connectors[0] && connectors[2] != 0
				&& target.canEnter();
	}

	/**
	 * Checks whether the player can move to the left to the given target tile.
	 * 
	 * @param target the tile to check.
	 * @return true if the connectors match in colour, false if they do not, the
	 *         target cannot be entered, or either of the connectors are 0.
	 */
	public boolean canMoveLeftTo(Tile target)
	{
		return connectors[3] == target.connectors[1] && connectors[3] != 0
				&& target.canEnter();
	}

	/**
	 * Checks whether the player can move to the right to the given target tile.
	 * 
	 * @param target the tile to check.
	 * @return true if the connectors match in colour, false if they do not, the
	 *         target cannot be entered, or either of the connectors are 0.
	 */
	public boolean canMoveRightTo(Tile target)
	{
		return connectors[1] == target.connectors[3] && connectors[1] != 0
				&& target.canEnter();
	}

	/**
	 * Checks whether the player can move to the up to the given target tile.
	 * 
	 * @param target the tile to check.
	 * @return true if the connectors match in colour, false if they do not, the
	 *         target cannot be entered, or either of the connectors are 0.
	 */
	public boolean canMoveUpTo(Tile target)
	{
		return connectors[0] == target.connectors[2] && connectors[0] != 0
				&& target.canEnter();
	}

	/**
	 * Checks whether this tile can be rotated by the player.
	 * 
	 * @return true if this tile can be rotated by the player, and false if it
	 *         cannot.
	 */
	public boolean canRotate()
	{
		return true;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a Tile identical to this one.
	 */
	public Tile clone()
	{
		return new Tile(this);
	}

	/**
	 * Draws the tile onto the given graphics object at the given position.
	 * 
	 * @param g the graphics object to draw onto.
	 * @param x the x-position to draw to.
	 * @param y the y-position to draw to.
	 */
	public void drawTile(Graphics g, int x, int y)
	{
		BufferedImage tileImg = new BufferedImage(TILE_WIDTH, TILE_WIDTH,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D t = tileImg.createGraphics();

		// Draw the connectors
		t.drawImage(connectorBase[connectors[0]].getSubimage(0, 0, 64, 16), 0,
				0, null);
		t.drawImage(connectorBase[connectors[1]].getSubimage(48, 0, 16, 64),
				48, 0, null);
		t.drawImage(connectorBase[connectors[2]].getSubimage(0, 48, 64, 16), 0,
				48, null);
		t.drawImage(connectorBase[connectors[3]].getSubimage(0, 0, 16, 64), 0,
				0, null);

		// Draw the tile base
		t.drawImage(tileBase[tileColour], 0, 0, null);
		((Graphics2D) g).drawImage(tileImg, x, y, null);
	}

	/**
	 * Checks for equality with the given object.
	 * 
	 * @param other the object to check for equality with
	 * @return true if the other object is not null, is the same type of Tile as
	 *         this tile, and has the same tile colour and connector colours.
	 *         Returns false in all other cases.
	 */
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (other.getClass() == getClass()) {
			Tile tile = (Tile) other;
			if (tile.tileColour != tileColour)
				return false;
			if (tile.connectors[0] != connectors[0]
					|| tile.connectors[1] != connectors[1]
					|| tile.connectors[2] != connectors[2]
					|| tile.connectors[3] != connectors[3])
				return false;
			return true;
		}
		return false;
	}

	/**
	 * Gives the current colour of the tile.
	 * 
	 * @return the colour of this tile.
	 */
	public int getColour()
	{
		return tileColour;
	}

	/**
	 * Gives the colours of the connectors.
	 * 
	 * @return an array of colours.
	 */
	public int[] getConnections()
	{
		return new int[] { connectors[0], connectors[1], connectors[2],
				connectors[3] };
	}

	/**
	 * Gives the name of this tile.
	 * 
	 * @return the name of the tile.
	 */
	public String getTileName()
	{
		return "tile";
	}

	/**
	 * Returns a hashCode value for this tile.
	 * 
	 * @return an integer representing the hashCode of this tile.
	 */
	public int hashCode()
	{

		// Multiply all values by primes to generate a unique hashCode for every
		// tile
		int hashCode = (tileColour + 1) * 103;
		hashCode += (connectors[0] + 1) * 1_069;
		hashCode += (connectors[1] + 1) * 10_453;
		hashCode += (connectors[2] + 1) * 116_791;
		hashCode += (connectors[3] + 1) * 1_299_827;
		return hashCode;
	}

	/**
	 * Checks whether the given colour is the same colour as this tile.
	 * 
	 * @param colour the colour to compare to.
	 * @return true if the colours are the same, and false if they are not.
	 */
	public boolean isSameColour(int colour)
	{
		return colour == this.tileColour;
	}

	/**
	 * Reads only the tile colour and the connector colours.
	 * 
	 * @param in the input stream to read the object from.
	 * @throws IOException then the inputstream cannot be read.
	 * @throws ClassNotFoundException when the tile data has been saved in the
	 *             wrong order.
	 */
	public void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		tileColour = in.readInt();
		connectors = (int[]) in.readObject();
	}

	/**
	 * Recolours this tile.
	 * 
	 * @param newColour the colour to change to.
	 */
	public void recolour(int newColour)
	{
		tileColour = newColour;
	}

	/**
	 * Recolours the bottom connector.
	 * 
	 * @param colour the colour to change to.
	 */
	public void recolourBottom(int colour)
	{
		connectors[2] = colour;
	}

	/**
	 * Recolours the left connector.
	 * 
	 * @param colour the colour to change to.
	 */
	public void recolourLeft(int colour)
	{
		connectors[3] = colour;
	}

	/**
	 * Recolours the right connector.
	 * 
	 * @param colour the colour to change to.
	 */
	public void recolourRight(int colour)
	{
		connectors[1] = colour;
	}

	/**
	 * Recolours the top connector.
	 * 
	 * @param colour the colour to change to.
	 */
	public void recolourTop(int colour)
	{
		connectors[0] = colour;
	}

	/**
	 * Rotates the connectors by PI/2 radians.
	 */
	public void rotateLeft()
	{
		int first = connectors[0];
		connectors[0] = connectors[1];
		connectors[1] = connectors[2];
		connectors[2] = connectors[3];
		connectors[3] = first;
	}

	/**
	 * Rotates the connectors by -PI/2 radians.
	 */
	public void rotateRight()
	{
		int last = connectors[3];
		connectors[3] = connectors[2];
		connectors[2] = connectors[1];
		connectors[1] = connectors[0];
		connectors[0] = last;
	}

	/**
	 * Creates a String representation of this tile.
	 * 
	 * @return a String containing the tile's name, colour, and connectors.
	 */
	public String toString()
	{
		return getTileName() + "[" + tileColour + "|" + connectors[0] + ","
				+ connectors[1] + "," + connectors[2] + "," + connectors[3]
				+ "]";
	}

	/**
	 * Writes only the tile colour and connector colours.
	 * 
	 * @param out the output stream to write to.
	 * @throws IOException when unable to save to the outputstream.
	 */
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeInt(tileColour);
		out.writeObject(connectors);
	}
}
