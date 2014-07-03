package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import level.ActionTile;
import level.EmptyTile;
import level.FinishTile;
import level.KeyTile;
import level.LauncherTile;
import level.Level;
import level.LockedTile;
import level.PaintTile;
import level.StartTile;
import level.StaticTile;
import level.Tile;
import level.TransportTile;

/**
 * The panel that holds the playable game.
 * 
 * @author Austin Tripp and Sherman Ying
 * @version June 15th 2013
 */
public class EditorPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private static final long serialVersionUID = 1234567890;

	// GUI positioning constants.
	private static final int CONTROL_X = 803;
	private static final int CONTROL_Y = 383;
	private static final int CONTROL_W = 136;
	private static final int CONTROL_H = 136;
	private static final int CONTROL_SX = 32;
	private static final int CONTROL_SY = 32;
	private static final int CONTROL_OX = 4;
	private static final int CONTROL_OY = 4;
	private static final int COLOUR_X = 803;
	private static final int COLOUR_Y = 359;
	private static final int COLOUR_W = 136;
	private static final int COLOUR_H = 24;
	private static final int COLOUR_SX = 16;
	private static final int COLOUR_OX = 4;
	private static final int COLOUR_OY = 4;
	private static final int PALETTE_X = 803;
	private static final int PALETTE_Y = 179;
	private static final int PALETTE_W = 136;
	private static final int PALETTE_H = 180;
	private static final int PALETTE_SX = 68;
	private static final int PALETTE_SY = 36;
	private static final int PALETTE_OX = 2;
	private static final int PALETTE_OY = 2;
	private static final int SAVE_X = 835;
	private static final int SAVE_Y = 14;
	private static final int SAVE_W = 36;
	private static final int SAVE_H = 36;
	private static final int EXIT_X = 871;
	private static final int EXIT_Y = 14;
	private static final int EXIT_W = 36;
	private static final int EXIT_H = 36;
	private static final int RENAME_X = 835;
	private static final int RENAME_Y = 50;
	private static final int RENAME_W = 36;
	private static final int RENAME_H = 36;
	private static final int REMOVE_X = 871;
	private static final int REMOVE_Y = 50;
	private static final int REMOVE_W = 36;
	private static final int REMOVE_H = 36;
	private static final int VIEW_X = 14;
	private static final int VIEW_Y = 14;
	private static final int VIEW_W = 768;
	private static final int VIEW_H = 512;

	// Loaded images.
	private static BufferedImage borderFrame;
	private static BufferedImage selectorBox;
	private static BufferedImage[] rotateButtons;
	private static BufferedImage[] activateButtons;
	private static BufferedImage colourPalette;
	private static BufferedImage colourSelect;
	private static BufferedImage tilePalette;
	private static BufferedImage[] tileDragIcons;
	private static BufferedImage targetIcon;
	private static BufferedImage componentSelect;
	private static BufferedImage tileViewBase;
	private static BufferedImage exitButton;
	private static BufferedImage saveButton;
	private static BufferedImage renameButton;
	private static BufferedImage removeButton;
	private static BufferedImage background;

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
		borderFrame = ImageIO.read(new File(root + "\\gui\\frame.png"));
		selectorBox = ImageIO.read(new File(root + "\\gui\\selector.png"));
		rotateButtons = new BufferedImage[4];
		activateButtons = new BufferedImage[2];
		rotateButtons[0] = ImageIO.read(new File(root
				+ "\\gui\\counterClock0.png"));
		rotateButtons[1] = ImageIO.read(new File(root
				+ "\\gui\\counterClock1.png"));
		rotateButtons[2] = ImageIO.read(new File(root + "\\gui\\clock0.png"));
		rotateButtons[3] = ImageIO.read(new File(root + "\\gui\\clock1.png"));
		activateButtons[0] = ImageIO.read(new File(root
				+ "\\gui\\activate0.png"));
		activateButtons[1] = ImageIO.read(new File(root
				+ "\\gui\\activate1.png"));
		targetIcon = ImageIO.read(new File(root + "\\gui\\target.png"));
		colourPalette = ImageIO
				.read(new File(root + "\\gui\\colourPalette.png"));
		colourSelect = ImageIO.read(new File(root
				+ "\\gui\\colourPaletteSelect.png"));
		tilePalette = ImageIO.read(new File(root + "\\gui\\tilePalette.png"));
		componentSelect = ImageIO.read(new File(root
				+ "\\gui\\componentSelect.png"));
		tileViewBase = ImageIO.read(new File(root + "\\gui\\tileView.png"));
		tileDragIcons = new BufferedImage[10];
		for (int icon = 0; icon < 10; icon++)
			tileDragIcons[icon] = ImageIO.read(new File(root + "\\gui\\drag"
					+ icon + ".png"));
		exitButton = ImageIO.read(new File(root + "\\gui\\exit.png"));
		saveButton = ImageIO.read(new File(root + "\\gui\\save.png"));
		renameButton = ImageIO.read(new File(root + "\\gui\\rename.png"));
		removeButton = ImageIO.read(new File(root + "\\gui\\remove.png"));
		background = ImageIO.read(new File(root + "\\gui\\background.png"));
		Level.loadImages(texturePack);
	}

	// Other window elements.
	private final RotaryFrame parent;

	// Holds level pack data.
	private int currentLevel;
	private Tile[][] grid;
	private ArrayList<Tile[][]> currentPack;
	private ArrayList<String> levelNames;
	private String packName;

	// Editor variables.
	private int draggedIcon;
	private int selectedComponent;
	private int selectCol;
	private int selectRow;
	private int viewX;
	private int viewY;
	private int dragOffsetX;
	private int dragOffsetY;
	private Point viewPos;
	private Point clickPos;
	private Point dragPos;

	/**
	 * Constructs a new GamePanel.
	 * 
	 * @param frame the frame holding this panel.
	 */
	public EditorPanel(RotaryFrame frame)
	{
		// Set up panel.
		super();
		setSize(RotaryFrame.PANEL_SIZE);
		setMaximumSize(RotaryFrame.PANEL_SIZE);
		setMinimumSize(RotaryFrame.PANEL_SIZE);
		setPreferredSize(RotaryFrame.PANEL_SIZE);
		this.parent = frame;

		grid = new Tile[16][24];
		for (int row = 0; row < grid.length; row++)
			for (int col = 0; col < grid[row].length; col++)
				grid[row][col] = new EmptyTile();
		draggedIcon = -1;

		// Add listeners.
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);

		// Load images.
		try {
			loadImages("default");
		} catch (IOException e) {
		}
	}

	/**
	 * Updates the window title to display the name of the level being edited.
	 */
	public void changeTitle()
	{
		parent.setTitle("Rotary - Editing \"" + levelNames.get(currentLevel)
				+ "\"");
	}

	/**
	 * Deletes the specified pack.
	 * 
	 * @param packName the pack to delete.
	 */
	public void deletePack(String packName)
	{
		File packFile = new File("data\\editor\\" + packName + ".tmpk");
		if (packFile.exists())
			packFile.delete();
	}

	/**
	 * Helper method for drawing the colour palette.
	 * 
	 * @param g the graphics context to draw to.
	 */
	private void drawColourPalette(Graphics g)
	{
		BufferedImage palette = new BufferedImage(COLOUR_W, COLOUR_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D paletteG2D = palette.createGraphics();
		paletteG2D.drawImage(colourPalette, 0, 0, null);

		// draw box for selected component colour.
		Tile tile = getSelectedTile();
		if (!(tile instanceof EmptyTile)) {
			if (tile instanceof ActionTile && selectedComponent == 6)
				paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
						* ((ActionTile) tile).getTargetColour(), COLOUR_OY,
						null);
			else {
				if (selectedComponent == 1)
					paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
							* tile.getColour(), COLOUR_OY, null);
				else if (selectedComponent == 2)
					paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
							* tile.getConnections()[0], COLOUR_OY, null);
				else if (selectedComponent == 3)
					paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
							* tile.getConnections()[1], COLOUR_OY, null);
				else if (selectedComponent == 4)
					paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
							* tile.getConnections()[2], COLOUR_OY, null);
				else if (selectedComponent == 5)
					paletteG2D.drawImage(colourSelect, COLOUR_OX + COLOUR_SX
							* tile.getConnections()[3], COLOUR_OY, null);
			}
		}

		g.drawImage(palette, COLOUR_X, COLOUR_Y, null);
	}

	/**
	 * Helper method for drawing the primary control box.
	 * 
	 * @param g the graphics context to draw to.
	 */
	private void drawPrimaryControls(Graphics g)
	{
		// Draw selected tile underneath.
		BufferedImage selection = new BufferedImage(Tile.TILE_WIDTH,
				Tile.TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D selectG2D = selection.createGraphics();
		Tile tile = getSelectedTile();
		tile.drawTile(selectG2D, 0, 0);

		// Add the relevant buttons.
		BufferedImage primaryControl = new BufferedImage(CONTROL_W, CONTROL_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D controlG2D = primaryControl.createGraphics();
		controlG2D.drawImage(tileViewBase, 0, 0, null);
		controlG2D.drawImage(selection, CONTROL_OX, CONTROL_OY, CONTROL_SX * 4,
				CONTROL_SY * 4, null);

		// Draw buttons.
		int buttonX = -1;
		int buttonY = -1;
		if (clickPos != null) {
			buttonX = (clickPos.x - CONTROL_X - CONTROL_OX + CONTROL_SX)
					/ CONTROL_SX - 1;
			buttonY = (clickPos.x - CONTROL_Y - CONTROL_OY + CONTROL_SY)
					/ CONTROL_SY - 1;
		}
		if (!(tile instanceof EmptyTile)) {
			// Draw the rotation buttons.
			if (!(tile instanceof StaticTile)) {
				if (buttonX == 0 && buttonY == 0)
					controlG2D.drawImage(rotateButtons[0], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(rotateButtons[1], CONTROL_OX,
							CONTROL_OY, null);
				if (buttonX == 3 && buttonY == 0)
					controlG2D.drawImage(rotateButtons[2], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(rotateButtons[3], CONTROL_OX,
							CONTROL_OY, null);
			}

			// Draw the target symbol.
			if (tile instanceof LockedTile
					|| (tile instanceof ActionTile && !(tile instanceof LauncherTile))) {
				controlG2D.drawImage(targetIcon, CONTROL_OX, CONTROL_OY, null);
			}

			// Draw the selection of the component to colour.
			if (tile instanceof ActionTile && !(tile instanceof LauncherTile)
					&& selectedComponent == 6)
				controlG2D.drawImage(componentSelect.getSubimage(0,
						CONTROL_SY * 3, CONTROL_SX, CONTROL_SY), CONTROL_OX,
						CONTROL_SY * 3 + CONTROL_OY, null);
			else if (selectedComponent == 1)
				controlG2D.drawImage(componentSelect.getSubimage(CONTROL_SX,
						CONTROL_SY, CONTROL_SX * 2, CONTROL_SY * 2), CONTROL_SX
						+ CONTROL_OX, CONTROL_SY + CONTROL_OY, null);
			else if (selectedComponent == 2)
				controlG2D.drawImage(componentSelect.getSubimage(CONTROL_SX, 0,
						CONTROL_SX * 2, CONTROL_SY), CONTROL_SX + CONTROL_OX,
						CONTROL_OY, null);
			else if (selectedComponent == 3)
				controlG2D.drawImage(
						componentSelect.getSubimage(CONTROL_SX * 3, CONTROL_SY,
								CONTROL_SX, CONTROL_SY * 2), CONTROL_SX * 3
								+ CONTROL_OX, CONTROL_SY + CONTROL_OY, null);
			else if (selectedComponent == 4)
				controlG2D.drawImage(componentSelect.getSubimage(CONTROL_SX,
						CONTROL_SY * 3, CONTROL_SX * 2, CONTROL_SY), CONTROL_SX
						+ CONTROL_OX, CONTROL_SY * 3 + CONTROL_OY, null);
			else if (selectedComponent == 5)
				controlG2D.drawImage(componentSelect.getSubimage(0, CONTROL_SY,
						CONTROL_SX, CONTROL_SY * 2), CONTROL_OX, CONTROL_SY
						+ CONTROL_OY, null);
		}
		((Graphics2D) g).drawImage(primaryControl, CONTROL_X, CONTROL_Y, null);
	}

	/**
	 * Helper method for drawing the tile palette.
	 * 
	 * @param g the graphics context to draw to.
	 */
	private void drawTilePalette(Graphics g)
	{
		BufferedImage palette = new BufferedImage(PALETTE_W, PALETTE_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D paletteG2D = palette.createGraphics();
		paletteG2D.drawImage(tilePalette, 0, 0, null);
		for (int icon = 0; icon < 10; icon++) {
			paletteG2D.drawImage(tileDragIcons[icon], PALETTE_OX + (icon % 2)
					* PALETTE_SX, PALETTE_OY + (icon / 2) * PALETTE_SY, null);
		}
		g.drawImage(palette, PALETTE_X, PALETTE_Y, null);
	}

	/**
	 * Duplicates the current pack.
	 * 
	 * @return true if the operation was successful, and false if it was not.
	 */
	public boolean duplicatePack()
	{
		String packName = JOptionPane.showInputDialog(this,
				"Enter the new name of this level pack: ");
		if (packName == null)
			return false;
		if (packName.equals("")) {
			JOptionPane.showMessageDialog(this,
					"You must type a name for this level pack!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// Check for file overwriting
		File target = new File("data\\editor\\" + packName + ".tmpk");
		if (target.exists()) {
			int choice = JOptionPane
					.showOptionDialog(
							this,
							"There is already a pack called "
									+ packName
									+ ".\nAre you sure you want to overwrite this file?",
							"Overwrite Existing Pack?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, null,
							JOptionPane.NO_OPTION);
			if (choice == JOptionPane.NO_OPTION)
				return false;
		}
		
		this.packName = packName;
		if (currentPack.size() > 0)
			save();
		return true;
	}

	/**
	 * Checks to see whether the current tile array contains a valid level The
	 * criteria for a valid level are:
	 * 
	 * 1) contains exactly 1 start tile and at least 1 end tile.
	 * 
	 * 2) There are either 0 or more than two teleporters.
	 * 
	 * 3) If there is a locked tile, there is a key tile and vice versa.
	 * 
	 * @return an ArrayList containing all the errors contained in this level.
	 */
	private ArrayList<String> getLevelErrors()
	{

		// Find the number of various tiles in the level.
		int numStartTile = 0;
		int numFinishTile = 0;
		int numTransportTile = 0;
		int numLockedTile = 0;
		int numKeyTile = 0;
		for (Tile[] row : grid)
			for (Tile tile : row) {
				if (tile instanceof StartTile)
					numStartTile++;
				if (tile instanceof FinishTile)
					numFinishTile++;
				if (tile instanceof TransportTile)
					numTransportTile++;
				if (tile instanceof LockedTile)
					numLockedTile++;
				if (tile instanceof KeyTile)
					numKeyTile++;
			}

		// Add each error to the list.
		ArrayList<String> errors = new ArrayList<String>();
		if (numStartTile < 1)
			errors.add("There must be a Start Tile!");
		else if (numStartTile > 1)
			errors.add("There can only be one Start Tile!");
		if (numFinishTile < 1)
			errors.add("There must be at least one End Tile!");
		if (numTransportTile == 1)
			errors.add("There needs to be another Transport Tile for the Transport Tile in this level to function");
		if (numLockedTile == 0 && numKeyTile > 0)
			errors.add("There must be at least one Locked Tile for a Key Tile to be present on the map");
		if (numKeyTile == 0 && numLockedTile > 0)
			errors.add("There must be at least one key tile for a Locked Tile to be present on the map");
		return errors;
	}

	/**
	 * Gives the number of levels in the current pack.
	 * 
	 * @return the number of levels.
	 */
	public int getNoOfLevels()
	{
		if (currentPack != null)
			return currentPack.size();
		return 0;
	}

	/**
	 * Gives the name of the current pack.
	 * 
	 * @return the name of the current pack.
	 */
	public String getPackName()
	{
		return packName;
	}

	/**
	 * Gets a reference to the selected tile.
	 * 
	 * @return the tile that is selected.
	 */
	public Tile getSelectedTile()
	{
		return grid[selectRow][selectCol];
	}

	/**
	 * Sets the current level to edit to the given level.
	 * 
	 * @param levelNo the number of the level to currently edit.
	 */
	public void loadLevel(int levelNo)
	{
		grid = currentPack.get(levelNo);
		currentLevel = levelNo;
		changeTitle();
	}

	/**
	 * Loads the level pack with the given name.
	 * 
	 * @param name the name of this level pack.
	 * @throws IOException if the IO fails.
	 * @throws ClassNotFoundException if the correct classes cannot be found.
	 */
	public void loadLevelPack(String name) throws IOException,
			ClassNotFoundException
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
				"data\\editor\\" + name + ".tmpk"));
		currentPack = new ArrayList<Tile[][]>(Arrays.asList((Tile[][][]) in
				.readObject()));
		levelNames = new ArrayList<String>(Arrays.asList((String[]) in
				.readObject()));
		grid = null;
		packName = name;
		in.close();
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	/**
	 * Handles the mouse dragging events.
	 * @param evt the mouse event to process.
	 */
	public void mouseDragged(MouseEvent evt)
	{
		if (clickPos == null)
			return;

		// Dragging the level view.
		if (clickPos.x >= VIEW_X && clickPos.x < VIEW_X + VIEW_W
				&& clickPos.y >= VIEW_Y && clickPos.y < VIEW_Y + VIEW_H) {
			int changeX = clickPos.x - evt.getPoint().x;
			int changeY = clickPos.y - evt.getPoint().y;
			viewX = viewPos.x + changeX;
			viewY = viewPos.y + changeY;
			if (viewX < 0)
				viewX = 0;
			else if (viewX + VIEW_W > grid[0].length * Tile.TILE_WIDTH)
				viewX = grid[0].length * Tile.TILE_WIDTH - VIEW_W;
			if (viewY < 0)
				viewY = 0;
			else if (viewY + VIEW_H > grid.length * Tile.TILE_WIDTH)
				viewY = grid.length * Tile.TILE_WIDTH - VIEW_H;
			repaint();
		}

		// Dragging the tile palette element.
		else if (clickPos.x >= PALETTE_X && clickPos.x < PALETTE_X + PALETTE_W
				&& clickPos.y >= PALETTE_Y
				&& clickPos.y < PALETTE_Y + PALETTE_H) {
			this.dragPos = evt.getPoint();
			repaint();
		}
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseMoved(MouseEvent event)
	{
	}

	@Override
	/**
	 * Handles the mouse clicking events.
	 * @param evt the mouse event to process.
	 */
	public void mousePressed(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		// Save the original click position and view position in order to
		// calculate screen dragging.
		clickPos = evt.getPoint();
		dragPos = evt.getPoint();
		viewPos = new Point(viewX, viewY);

		// If we are clicking on the level view, change the selected tile.
		if (clickPos.x >= VIEW_X && clickPos.x < VIEW_X + VIEW_W
				&& clickPos.y >= VIEW_Y && clickPos.y < VIEW_Y + VIEW_H) {
			selectCol = (clickPos.x + viewX - VIEW_X) / Tile.TILE_WIDTH;
			selectRow = (clickPos.y + viewY - VIEW_Y) / Tile.TILE_WIDTH;
		}
		// Control box event.
		else if (clickPos.x >= CONTROL_X + CONTROL_OX
				&& clickPos.x + CONTROL_OX < CONTROL_X + CONTROL_W
				&& clickPos.y >= CONTROL_Y + CONTROL_OY
				&& clickPos.y + CONTROL_OY < CONTROL_Y + CONTROL_H) {
			int buttonX = -1;
			int buttonY = -1;
			if (clickPos != null) {
				buttonX = (clickPos.x - CONTROL_X - CONTROL_OX + CONTROL_SX)
						/ CONTROL_SX - 1;
				buttonY = (clickPos.y - CONTROL_Y - CONTROL_OY + CONTROL_SY)
						/ CONTROL_SY - 1;
			}
			Tile tile = getSelectedTile();
			if (!(tile instanceof EmptyTile)) {
				if (buttonX == 0 && buttonY == 0
						&& !(tile instanceof StaticTile))
					tile.rotateLeft();
				else if (buttonX == 3 && buttonY == 0
						&& !(tile instanceof StaticTile))
					tile.rotateRight();
				else if (buttonX == 0 && buttonY == 3
						&& tile instanceof LockedTile)
					((LockedTile) tile).toggleLock();
				else {
					if (buttonX == 0) {
						if (buttonY == 1 || buttonY == 2) {
							selectedComponent = 5;
						} else if (buttonY == 3 && tile instanceof ActionTile
								&& !(tile instanceof LauncherTile)) {
							selectedComponent = 6;
						}
					} else if (buttonX == 1 || buttonX == 2) {
						if (buttonY == 0)
							selectedComponent = 2;
						if (buttonY == 1 || buttonY == 2)
							selectedComponent = 1;
						else if (buttonY == 3)
							selectedComponent = 4;
					} else if (buttonX == 3 && (buttonY == 1 || buttonY == 2)) {
						selectedComponent = 3;
					}
				}
			}
		}

		// Colour palette event.
		else if (clickPos.x >= COLOUR_X && clickPos.x < COLOUR_X + COLOUR_W
				&& clickPos.y >= COLOUR_Y && clickPos.y < COLOUR_Y + COLOUR_H) {
			Tile tile = getSelectedTile();
			if (!(tile instanceof EmptyTile)) {
				int newColour = (clickPos.x - COLOUR_X - COLOUR_OX) / COLOUR_SX;

				// Recolour the correct component.
				if (selectedComponent == 1 && newColour != 0)
					tile.recolour(newColour);
				else if (selectedComponent == 2)
					tile.recolourTop(newColour);
				else if (selectedComponent == 3)
					tile.recolourRight(newColour);
				else if (selectedComponent == 4)
					tile.recolourBottom(newColour);
				else if (selectedComponent == 5)
					tile.recolourLeft(newColour);
				else if (tile instanceof ActionTile && selectedComponent == 6
						&& newColour != 0) {
					((ActionTile) tile).retarget(newColour);
				}
			}
		}

		// Tile palette event.
		else if (clickPos.x >= PALETTE_X && clickPos.x < PALETTE_X + PALETTE_W
				&& clickPos.y >= PALETTE_Y
				&& clickPos.y < PALETTE_Y + PALETTE_H) {
			int x = (clickPos.x - PALETTE_X) / PALETTE_SX;
			int y = (clickPos.y - PALETTE_Y) / PALETTE_SY;
			draggedIcon = x + 2 * y;
			dragOffsetX = (clickPos.x - PALETTE_X) % PALETTE_SX - PALETTE_OX;
			dragOffsetY = (clickPos.y - PALETTE_Y) % PALETTE_SY - PALETTE_OY;
		}
		// Other actions.
		else if (clickPos.x >= SAVE_X && clickPos.x < SAVE_X + SAVE_W
				&& clickPos.y >= SAVE_Y && clickPos.y < SAVE_Y + SAVE_H) {
			save();
		} else if (clickPos.x >= EXIT_X && clickPos.x < EXIT_X + EXIT_W
				&& clickPos.y >= EXIT_Y && clickPos.y < EXIT_Y + EXIT_H) {
			parent.showMenuPanel();
		} else if (clickPos.x >= RENAME_X && clickPos.x < RENAME_X + RENAME_W
				&& clickPos.y >= RENAME_Y && clickPos.y < RENAME_Y + RENAME_H) {
			rename();
		} else if (clickPos.x >= REMOVE_X && clickPos.x < REMOVE_X + REMOVE_W
				&& clickPos.y >= REMOVE_Y && clickPos.y < REMOVE_Y + REMOVE_H) {
			removeLevel(currentLevel);
		}
		repaint();
	}

	@Override
	/**
	 * Handles the mouse events from releasing the mouse button.
	 * @param evt the mouse event to process.
	 */
	public void mouseReleased(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		// Check to see if we dragged a tile into place.
		Point dropPoint = evt.getPoint();
		if (clickPos.x >= PALETTE_X && clickPos.x < PALETTE_X + PALETTE_W
				&& clickPos.y >= PALETTE_Y
				&& clickPos.y < PALETTE_Y + PALETTE_H && dropPoint.x >= VIEW_X
				&& dropPoint.x < VIEW_X + VIEW_W && dropPoint.y >= VIEW_Y
				&& dropPoint.y < VIEW_Y + VIEW_H) {
			int dropCol = (dropPoint.x + viewX - VIEW_X) / Tile.TILE_WIDTH;
			int dropRow = (dropPoint.y + viewY - VIEW_Y) / Tile.TILE_WIDTH;
			switch (draggedIcon) {
			case 0:
				grid[dropRow][dropCol] = new EmptyTile();
				break;
			case 1:
				grid[dropRow][dropCol] = new Tile(1, new int[] { 1, 1, 1, 1 });
				break;
			case 2:
				grid[dropRow][dropCol] = new StaticTile(1, new int[] { 1, 1, 1,
						1 });
				break;
			case 3:
				grid[dropRow][dropCol] = new PaintTile(1, 1, new int[] { 1, 1,
						1, 1 }, dropRow, dropCol);
				break;
			case 4:
				grid[dropRow][dropCol] = new KeyTile(1, 1, new int[] { 1, 1, 1,
						1 }, dropRow, dropCol);
				break;
			case 5:
				grid[dropRow][dropCol] = new LockedTile(1, new int[] { 1, 1, 1,
						1 });
				break;
			case 6:
				grid[dropRow][dropCol] = new LauncherTile(1, 0, new int[] { 1,
						1, 1, 1 }, dropRow, dropCol);
				break;
			case 7:
				grid[dropRow][dropCol] = new TransportTile(1, 1, new int[] { 1,
						1, 1, 1 }, dropRow, dropCol);
				break;
			case 8:
				grid[dropRow][dropCol] = new StartTile(1, new int[] { 1, 1, 1,
						1 });
				break;
			case 9:
				grid[dropRow][dropCol] = new FinishTile(1, new int[] { 1, 1, 1,
						1 });
				break;
			}
			selectRow = dropRow;
			selectCol = dropCol;
		}

		// Blank the clicking position.
		clickPos = null;
		draggedIcon = -1;
		dragPos = null;
		repaint();
	}

	/**
	 * Creates a new level.
	 * 
	 * @param name the name of the new level.
	 */
	public void newLevel(String name)
	{
		if (currentPack == null) {
			JOptionPane
					.showMessageDialog(
							this,
							"You must create a new Level Pack in order to create a level!",
							"Cannot Create Level", JOptionPane.WARNING_MESSAGE);
			return;
		}
		currentLevel = currentPack.size();
		grid = new Tile[16][24];
		for (int row = 0; row < grid.length; row++)
			for (int col = 0; col < grid[row].length; col++)
				grid[row][col] = new EmptyTile();
		currentPack.add(grid);
		levelNames.add(name);
	}

	/**
	 * Creates a new levelPack with the given name.
	 * 
	 * @param name the given name for this levelPack.
	 */
	public void newLevelPack(String name)
	{
		grid = null;
		packName = name;
		levelNames = new ArrayList<String>();
		currentPack = new ArrayList<Tile[][]>();
	}

	/**
	 * Repaints the screen.
	 * 
	 * @param g the graphics context.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		// Create an offscreen BufferedImage to simplify drawing the level view.
		BufferedImage img = new BufferedImage(VIEW_W, VIEW_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		g2D.drawImage(background, 0, 0, null);
		// Calculate the view offset.
		int offsetX = -(viewX % Tile.TILE_WIDTH);
		int offsetY = -(viewY % Tile.TILE_WIDTH);
		int firstR = viewY / Tile.TILE_WIDTH;
		int firstC = viewX / Tile.TILE_WIDTH;

		// Draw any tiles that are fully or partially in the view of the screen.
		for (int row = 0; row <= Level.VIEW_ROWS && row + firstR < grid.length; row++)
			for (int col = 0; col <= Level.VIEW_COLS
					&& col + firstC < grid[row].length; col++) {
				grid[row + firstR][col + firstC].drawTile(g2D, offsetX
						+ Tile.TILE_WIDTH * col, offsetY + Tile.TILE_WIDTH
						* row);
			}

		// Draw a selection box around the selected tile.
		g2D.drawImage(selectorBox,
				(selectCol * Tile.TILE_WIDTH - viewX - offsetX)
						/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetX,
				(selectRow * Tile.TILE_WIDTH - viewY - offsetY)
						/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetY,
				Tile.TILE_WIDTH, Tile.TILE_WIDTH, null);

		// Draw the image to the screen and add the border.
		g.drawImage(img, VIEW_X, VIEW_Y, null);
		if (borderFrame != null)
			((Graphics2D) g).drawImage(borderFrame, 0, 0, null);

		// Draw the sidebar.
		drawPrimaryControls(g);
		drawColourPalette(g);
		drawTilePalette(g);

		((Graphics2D) g).drawImage(saveButton, SAVE_X, SAVE_Y, null);
		((Graphics2D) g).drawImage(exitButton, EXIT_X, EXIT_Y, null);
		((Graphics2D) g).drawImage(renameButton, RENAME_X, RENAME_Y, null);
		((Graphics2D) g).drawImage(removeButton, REMOVE_X, REMOVE_Y, null);

		// Draw the dragged icon.
		if (draggedIcon >= 0 && draggedIcon < 10 && dragPos != null) {
			BufferedImage image = tileDragIcons[draggedIcon];
			int xPos = dragPos.x - dragOffsetX;
			int yPos = dragPos.y - dragOffsetY;
			((Graphics2D) g).drawImage(image, xPos, yPos, null);
		}
	}

	/**
	 * Publishes the current level pack and deletes the existing solution file.
	 */
	public void publishLevelPack()
	{
		if (currentPack.size() == 0) {
			JOptionPane.showMessageDialog(this,
					"You cannot publish an empty pack.", "Publishing Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Publish each map to a level object.
		Level[] pack = new Level[currentPack.size()];

		// Iterate through each level, to search for errors.
		// If errors are found notify the user and terminate this process.
		for (int i = 0; i < currentPack.size(); i++) {

			// Search the current map for errors.
			grid = currentPack.get(i);
			ArrayList<String> errors = getLevelErrors();
			if (!errors.isEmpty()) {
				StringBuilder errorStr = new StringBuilder(50);
				for (String error : errors) {
					errorStr.append('\n');
					errorStr.append(error);
				}
				JOptionPane
						.showMessageDialog(
								this,
								"The publishing process was halted due to errors in level "
										+ (i + 1)
										+ ".\nThis level could not be published due to the following errors:"
										+ errorStr.toString(), "Level Errors",
								JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Put this level in the array.
			pack[i] = new Level(grid, levelNames.get(i));

			// Delete the solution in case the levels were modified.
			File solution = new File("data\\solutions\\" + packName + ".soln");
			if (solution.exists())
				solution.delete();
		}

		// Now that each level is without errors, write them to a file.
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("data\\levels\\" + packName + ".pck"));
			out.writeObject(pack);
			out.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"An error occurred. Please try again", "Publishing Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Return to the main menu.
		parent.showMenuPanel();
	}

	/**
	 * Deletes a level from the current pack, and exits to the menu.
	 * 
	 * @param levelNumber the level to remove.
	 */
	private void removeLevel(int levelNumber)
	{
		int chosenAction = JOptionPane
				.showOptionDialog(
						parent,
						"Are you sure you wish to delete this level?\nWarning: This cannot be undone.",
						"Delete this level?", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, null,
						JOptionPane.NO_OPTION);
		if (chosenAction == JOptionPane.YES_OPTION) {
			parent.showMenuPanel();
			currentPack.remove(levelNumber);
			levelNames.remove(levelNumber);
			grid = null;
			save();
		}
	}

	/**
	 * Renames this level.
	 */
	private void rename()
	{
		String packName = JOptionPane.showInputDialog(this,
				"Enter the name of this level: ");
		if (packName == null)
			return;
		if (packName.equals("")) {
			JOptionPane.showMessageDialog(this,
					"You must type a name for this level!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		levelNames.set(currentLevel, packName);
		changeTitle();
	}

	/**
	 * Renames this pack.
	 */
	public void renamePack()
	{
		String origName = packName;
		if (duplicatePack())
			deletePack(origName);
	}

	/**
	 * Repositions a level in the current levelPack.
	 * 
	 * @param srcPos the level being shifted.
	 * @param newPos the new position for this level.
	 */
	public void repositionLevel(int srcPos, int newPos)
	{
		levelNames.add(newPos, levelNames.remove(srcPos));
	}

	/**
	 * Writes the pack to a file.
	 */
	private void save()
	{

		// Add the current Level to the pack.
		if (currentPack.size() > 0 && grid != null)
			currentPack.set(currentLevel, grid);

		// Write the level pack and names to arrays.
		Tile[][][] pack = new Tile[currentPack.size()][][];
		currentPack.toArray(pack);
		String[] names = new String[levelNames.size()];
		levelNames.toArray(names);

		// Write it to a data file.
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("data\\editor\\" + packName + ".tmpk"));
			out.writeObject(pack);
			out.writeObject(names);
			out.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"An error occurred. Please try again", "Saving Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
}
