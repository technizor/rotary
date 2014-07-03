package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import level.EmptyTile;
import level.Level;
import level.Tile;

/**
 * The panel that holds the playable game.
 * 
 * @author Austin Tripp and Sherman Ying
 * @version June 15th 2013
 */
public class GamePanel extends JPanel implements MouseListener, KeyListener,
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
	private static final int EXIT_X = 871;
	private static final int EXIT_Y = 14;
	private static final int EXIT_W = 36;
	private static final int EXIT_H = 36;
	private static final int SOLVE_X = 835;
	private static final int SOLVE_Y = 14;
	private static final int SOLVE_W = 36;
	private static final int SOLVE_H = 36;
	private static final int VIEW_H = 512;
	private static final int VIEW_W = 768;
	private static final int VIEW_X = 14;
	private static final int VIEW_Y = 14;

	// Loaded images.
	private static BufferedImage borderFrame;
	private static BufferedImage selectorBox;
	private static BufferedImage[] rotateButtons;
	private static BufferedImage[] moveButtons;
	private static BufferedImage[] activateButtons;
	private static BufferedImage[] recentreButtons;
	private static BufferedImage tileViewBase;
	private static BufferedImage exitButton;
	private static BufferedImage solveButton;
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
		moveButtons = new BufferedImage[8];
		activateButtons = new BufferedImage[2];
		recentreButtons = new BufferedImage[2];
		rotateButtons[0] = ImageIO.read(new File(root
				+ "\\gui\\counterClock0.png"));
		rotateButtons[1] = ImageIO.read(new File(root
				+ "\\gui\\counterClock1.png"));
		rotateButtons[2] = ImageIO.read(new File(root + "\\gui\\clock0.png"));
		rotateButtons[3] = ImageIO.read(new File(root + "\\gui\\clock1.png"));
		moveButtons[0] = ImageIO.read(new File(root + "\\gui\\moveUp0.png"));
		moveButtons[1] = ImageIO.read(new File(root + "\\gui\\moveUp1.png"));
		moveButtons[2] = ImageIO.read(new File(root + "\\gui\\moveRight0.png"));
		moveButtons[3] = ImageIO.read(new File(root + "\\gui\\moveRight1.png"));
		moveButtons[4] = ImageIO.read(new File(root + "\\gui\\moveDown0.png"));
		moveButtons[5] = ImageIO.read(new File(root + "\\gui\\moveDown1.png"));
		moveButtons[6] = ImageIO.read(new File(root + "\\gui\\moveLeft0.png"));
		moveButtons[7] = ImageIO.read(new File(root + "\\gui\\moveLeft1.png"));
		activateButtons[0] = ImageIO.read(new File(root
				+ "\\gui\\activate0.png"));
		activateButtons[1] = ImageIO.read(new File(root
				+ "\\gui\\activate1.png"));
		recentreButtons[0] = ImageIO.read(new File(root
				+ "\\gui\\recentre0.png"));
		recentreButtons[1] = ImageIO.read(new File(root
				+ "\\gui\\recentre1.png"));
		tileViewBase = ImageIO.read(new File(root + "\\gui\\tileView.png"));
		exitButton = ImageIO.read(new File(root + "\\gui\\exit.png"));
		solveButton = ImageIO.read(new File(root + "\\gui\\solve.png"));
		background = ImageIO.read(new File(root + "\\gui\\background.png"));
		Level.loadImages(texturePack);
	}

	// Other window elements.
	private RotaryFrame parent;
	private SolutionPanel solnPanel;

	// Level pack data.
	private Level[] levelPack;
	private int levelIndex;
	private Level currentLevel;
	private String levelPackName;

	// Other variables.
	private int selectCol;
	private int selectRow;
	private int viewX;
	private int viewY;
	private Point viewPos;
	private Point clickPos;

	/**
	 * Constructs a new GamePanel.
	 * 
	 * @param frame the frame holding this panel.
	 */
	public GamePanel(RotaryFrame frame, SolutionPanel solnPanel)
	{
		// Set up panel.
		super();
		setSize(RotaryFrame.PANEL_SIZE);
		setMaximumSize(RotaryFrame.PANEL_SIZE);
		setMinimumSize(RotaryFrame.PANEL_SIZE);
		setPreferredSize(RotaryFrame.PANEL_SIZE);
		levelPack = null;
		levelIndex = 0;
		currentLevel = null;
		levelPackName = null;
		selectCol = 0;
		selectRow = 0;
		viewX = 0;
		viewY = 0;
		viewPos = null;
		clickPos = null;
		this.parent = frame;
		this.solnPanel = solnPanel;
		currentLevel = new Level(new Tile[][] { { new EmptyTile() } }, "");
		selectPlayer();
		centreView();

		// Add listeners.
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.addKeyListener(this);

		// Load images.
		try {
			loadImages("default");
		} catch (IOException e) {
		}
	}

	/**
	 * Centres the view around the selection box.
	 */
	public void centreView()
	{
		int maxX = currentLevel.getWidth() * Tile.TILE_WIDTH - VIEW_W;
		int maxY = currentLevel.getHeight() * Tile.TILE_WIDTH - VIEW_H;
		int centreX = selectCol * Tile.TILE_WIDTH - VIEW_W / 2
				+ Tile.TILE_WIDTH / 2;
		int centreY = selectRow * Tile.TILE_WIDTH - VIEW_H / 2
				+ Tile.TILE_WIDTH / 2;
		viewX = centreX > 0 ? centreX < maxX ? centreX : maxX : 0;
		viewY = centreY > 0 ? centreY < maxY ? centreY : maxY : 0;
	}

	/**
	 * Changes the title of the Frame to reflect the current state of this
	 * panel.
	 */
	public void changeTitle()
	{
		parent.setTitle("Rotary - Playing \"" + currentLevel + "\"");
	}

	/**
	 * Helper method for drawing the control box.
	 * 
	 * @param g the graphics context of the panel.
	 */
	private void drawPrimaryControls(Graphics g)
	{
		// Draw selected tile underneath.
		BufferedImage selection = new BufferedImage(Tile.TILE_WIDTH,
				Tile.TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D selectG2D = selection.createGraphics();
		getSelectedTile().drawTile(selectG2D, 0, 0);

		// Add the relevant buttons.
		BufferedImage primaryControl = new BufferedImage(CONTROL_W, CONTROL_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D controlG2D = primaryControl.createGraphics();
		controlG2D.drawImage(tileViewBase, 0, 0, null);
		controlG2D.drawImage(selection, 4, 4, 128, 128, null);

		// Draw buttons.
		int buttonX = -1;
		int buttonY = -1;
		if (clickPos != null) {
			buttonX = (int) (clickPos.getX() - CONTROL_X - CONTROL_OX + CONTROL_SX)
					/ CONTROL_SX - 1;
			buttonY = (int) (clickPos.getY() - CONTROL_Y - CONTROL_OY + CONTROL_SY)
					/ CONTROL_SY - 1;
		}
		Point playerPos = currentLevel.getPlayerPosition();
		if (playerPos.x == selectCol && playerPos.y == selectRow) {
			// Draw control buttons.
			if (currentLevel.canRotate()) {
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
			if (currentLevel.canMoveUp()) {
				if ((buttonX == 1 || buttonX == 2) && buttonY == 0)
					controlG2D.drawImage(moveButtons[0], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(moveButtons[1], CONTROL_OX,
							CONTROL_OY, null);
			}
			if (currentLevel.canMoveRight()) {
				if ((buttonY == 1 || buttonY == 2) && buttonX == 3)
					controlG2D.drawImage(moveButtons[2], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(moveButtons[3], CONTROL_OX,
							CONTROL_OY, null);
			}
			if (currentLevel.canMoveDown()) {
				if ((buttonX == 1 || buttonX == 2) && buttonY == 3)
					controlG2D.drawImage(moveButtons[4], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(moveButtons[5], CONTROL_OX,
							CONTROL_OY, null);
			}
			if (currentLevel.canMoveLeft()) {
				if ((buttonY == 1 || buttonY == 2) && buttonX == 0)
					controlG2D.drawImage(moveButtons[6], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(moveButtons[7], CONTROL_OX,
							CONTROL_OY, null);
			}
			if (currentLevel.canActivate()) {
				if (buttonX == 0 && buttonY == 3)
					controlG2D.drawImage(activateButtons[0], CONTROL_OX,
							CONTROL_OY, null);
				else
					controlG2D.drawImage(activateButtons[1], CONTROL_OX,
							CONTROL_OY, null);
			}
		} else {
			if (buttonX == 3 && buttonY == 3)
				controlG2D.drawImage(recentreButtons[0], CONTROL_OX,
						CONTROL_OY, null);
			else
				controlG2D.drawImage(recentreButtons[1], CONTROL_OX,
						CONTROL_OY, null);

		}
		((Graphics2D) g).drawImage(primaryControl, CONTROL_X, CONTROL_Y, null);
	}

	/**
	 * Gives the number of levels in the current level pack.
	 * 
	 * @return the number of levels in this pack.
	 */
	public int getNoOfLevels()
	{
		if (levelPack != null)
			return levelPack.length;
		return 0;
	}

	/**
	 * Gets a reference to the selected tile.
	 * 
	 * @return the tile that is selected.
	 */
	public Tile getSelectedTile()
	{
		return currentLevel.tileAt(selectRow, selectCol);
	}

	/**
	 * Moves the player based on the key pressed.
	 * 
	 * @param event the KeyEvent caused by the user pressing a key.
	 */
	public void keyPressed(KeyEvent event)
	{

		// Check to see if the key code matches any command, and if so activate
		// it.
		int keyCode = event.getKeyCode();
		if (keyCode == KeyInput.ACTIVATE_KEY) {
			if (currentLevel.canActivate())
				currentLevel.activate();
		} else if (keyCode == KeyInput.ROTATE_LEFT_KEY) {
			if (currentLevel.canRotate())
				currentLevel.rotateLeft();
		} else if (keyCode == KeyInput.ROTATE_RIGHT_KEY) {
			if (currentLevel.canRotate())
				currentLevel.rotateRight();
		} else if (keyCode == KeyInput.MOVE_UP_KEY) {
			if (currentLevel.canMoveUp())
				currentLevel.moveUp();
		} else if (keyCode == KeyInput.MOVE_RIGHT_KEY) {
			if (currentLevel.canMoveRight())
				currentLevel.moveRight();
		} else if (keyCode == KeyInput.MOVE_DOWN_KEY) {
			if (currentLevel.canMoveDown())
				currentLevel.moveDown();
		} else if (keyCode == KeyInput.MOVE_LEFT_KEY) {
			if (currentLevel.canMoveLeft())
				currentLevel.moveLeft();
		}
		selectPlayer();
		centreView();
		repaint();

		// Check to see if the level is finished.
		if (currentLevel.levelComplete()) {
			nextLevel();
		}

	}

	/**
	 * This method is not used.
	 */
	public void keyReleased(KeyEvent event)
	{
	}

	/**
	 * This method is not used.
	 */
	public void keyTyped(KeyEvent event)
	{
	}

	/**
	 * This method is not used.
	 */
	public void mouseClicked(MouseEvent event)
	{
	}

	/**
	 * Moves the map as the user drags the mouse
	 * 
	 * @param evt the user dragging the mouse
	 */
	public void mouseDragged(MouseEvent evt)
	{
		// Ensure that we are clicking and dragging the level view.
		if (clickPos.x >= VIEW_X && clickPos.x < VIEW_X + VIEW_W
				&& clickPos.y >= VIEW_Y && clickPos.y < VIEW_Y + VIEW_H) {
			Point dragPos = evt.getPoint();
			int changeX = clickPos.x - dragPos.x;
			int changeY = clickPos.y - dragPos.y;
			viewX = viewPos.x + changeX;
			viewY = viewPos.y + changeY;
			if (viewX < 0)
				viewX = 0;
			else if (viewX + VIEW_W > currentLevel.getWidth() * Tile.TILE_WIDTH)
				viewX = currentLevel.getWidth() * Tile.TILE_WIDTH - VIEW_W;
			if (viewY < 0)
				viewY = 0;
			else if (viewY + VIEW_H > currentLevel.getHeight()
					* Tile.TILE_WIDTH)
				viewY = currentLevel.getHeight() * Tile.TILE_WIDTH - VIEW_H;
			repaint();
		}
	}

	/**
	 * This method is not used.
	 */
	public void mouseEntered(MouseEvent event)
	{
	}

	/**
	 * This method is not used.
	 */
	public void mouseExited(MouseEvent event)
	{
	}

	/**
	 * This method is not used.
	 */
	public void mouseMoved(MouseEvent event)
	{
	}

	/**
	 * Responds to the mouse being pressed.
	 * 
	 * @param evt the user pressing the mouse.
	 */
	public void mousePressed(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		// Save the original click position and view position in order to
		// calculate screen dragging.
		clickPos = evt.getPoint();
		viewPos = new Point(viewX, viewY);
		// If we are clicking on the level view, change the selected tile.
		if (clickPos.x >= VIEW_X && clickPos.x < VIEW_X + VIEW_W
				&& clickPos.y >= VIEW_Y && clickPos.y < VIEW_Y + VIEW_H) {
			selectCol = (clickPos.x + viewX - VIEW_X) / Tile.TILE_WIDTH;
			selectRow = (clickPos.y + viewY - VIEW_Y) / Tile.TILE_WIDTH;
		}
		// Control box.
		else if (clickPos.x >= CONTROL_X + CONTROL_OX
				&& clickPos.x + CONTROL_OX < CONTROL_X + CONTROL_W
				&& clickPos.y >= CONTROL_Y + CONTROL_OY
				&& clickPos.y + CONTROL_OY < CONTROL_Y + CONTROL_H) {
			int buttonX = -1;
			int buttonY = -1;
			if (clickPos != null) {
				buttonX = (clickPos.x - CONTROL_X - CONTROL_OX) / CONTROL_SX;
				buttonY = (clickPos.y - CONTROL_Y - CONTROL_OY) / CONTROL_SY;
			}
			Point playerPos = currentLevel.getPlayerPosition();
			// Trigger the correct control.
			if (playerPos.x == selectCol && playerPos.y == selectRow) {
				if (buttonX == 0 && buttonY == 0) {
					if (currentLevel.canRotate())
						currentLevel.rotateLeft();
				} else if ((buttonX == 1 || buttonX == 2) && buttonY == 0) {
					if (currentLevel.canMoveUp())
						currentLevel.moveUp();
				} else if (buttonX == 3 && buttonY == 0) {
					if (currentLevel.canRotate())
						currentLevel.rotateRight();
				} else if ((buttonY == 1 || buttonY == 2) && buttonX == 0) {
					if (currentLevel.canMoveLeft())
						currentLevel.moveLeft();
				} else if ((buttonY == 1 || buttonY == 2) && buttonX == 3) {
					if (currentLevel.canMoveRight())
						currentLevel.moveRight();
				} else if (buttonX == 0 && buttonY == 3) {
					if (currentLevel.canActivate())
						currentLevel.activate();
				} else if ((buttonX == 1 || buttonX == 2) && buttonY == 3) {
					if (currentLevel.canMoveDown())
						currentLevel.moveDown();
				}
				selectPlayer();
				centreView();
				// Check to see if the level is finished.
				if (currentLevel.levelComplete()) {
					nextLevel();
				}
			} else {
				if (buttonX == 3 && buttonY == 3) {
					this.selectPlayer();
					this.centreView();
				}
			}
		} else if (clickPos.x >= EXIT_X && clickPos.x < EXIT_X + EXIT_W
				&& clickPos.y >= EXIT_Y && clickPos.y < EXIT_Y + EXIT_H) {
			parent.showMenuPanel();
		} else if (clickPos.x >= SOLVE_X && clickPos.x < SOLVE_X + SOLVE_W
				&& clickPos.y >= SOLVE_Y && clickPos.y < SOLVE_Y + SOLVE_H) {
			showSolution();
		}
		repaint();
	}

	/**
	 * Responds to the mouse being released.
	 * 
	 * @param evt the user releasing the mouse.
	 */
	public void mouseReleased(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		// Blank the clicking position.
		clickPos = null;
		repaint();
	}

	/**
	 * Moves the game to the next level.
	 */
	private void nextLevel()
	{
		repaint();
		levelIndex++;
		if (levelIndex < levelPack.length) {
			JOptionPane.showMessageDialog(this, "Level Complete!",
					"Congratulations", JOptionPane.INFORMATION_MESSAGE);
			currentLevel = levelPack[levelIndex];
			changeTitle();
			selectPlayer();
			centreView();
		} else {
			JOptionPane.showMessageDialog(this,
					"You've finished this Level Pack", "Pack Complete",
					JOptionPane.INFORMATION_MESSAGE);
			currentLevel = null;
			parent.showMenuPanel();
		}
		repaint();
	}

	/**
	 * Repaints the screen.
	 * 
	 * @param g the graphics context to paint to.
	 */
	public void paintComponent(Graphics g)
	{
		// Create an offscreen BufferedImage to simplify drawing the level view.
		BufferedImage img = new BufferedImage(VIEW_W, VIEW_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		g2D.drawImage(background, 0, 0, null);
		currentLevel.draw(g2D, viewX, viewY);

		// Draw a selection box around the selected tile.
		int offsetX = -(viewX % Tile.TILE_WIDTH);
		int offsetY = -(viewY % Tile.TILE_WIDTH);
		g2D.drawImage(selectorBox,
				(selectCol * Tile.TILE_WIDTH - viewX - offsetX)
						/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetX,
				(selectRow * Tile.TILE_WIDTH - viewY - offsetY)
						/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetY,
				Tile.TILE_WIDTH, Tile.TILE_WIDTH, null);

		// draw the image to the screen and add the border.
		g.drawImage(img, VIEW_X, VIEW_Y, null);
		if (borderFrame != null)
			((Graphics2D) g).drawImage(borderFrame, 0, 0, null);

		// Draw the side gui.
		drawPrimaryControls(g);
		((Graphics2D) g).drawImage(exitButton, EXIT_X, EXIT_Y, null);
		((Graphics2D) g).drawImage(solveButton, SOLVE_X, SOLVE_Y, null);
	}

	/**
	 * Moves the selection box to the location of the player. Should be used in
	 * conjunction with centreView().
	 */
	public void selectPlayer()
	{
		Point playerPos = currentLevel.getPlayerPosition();
		selectCol = playerPos.x;
		selectRow = playerPos.y;
	}

	/**
	 * Sets the current level pack.
	 * 
	 * @param packName the name of the current level pack.
	 * @throws IOException if IO does not work.
	 * @throws FileNotFoundException if the desired file is not found.
	 * @throws ClassNotFoundException if the Level class is not found.
	 */
	public void setLevelPack(String packName) throws FileNotFoundException,
			IOException, ClassNotFoundException
	{

		// Set the level pack and the associated data.
		ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(
				"data\\levels\\" + packName + ".pck"));
		levelPack = (Level[]) fileIn.readObject();
		levelIndex = 0;
		currentLevel = levelPack[levelIndex];
		fileIn.close();
		levelPackName = packName;

		// Set the solution pack.
		solnPanel.setSolutionPack(packName);
	}

	/**
	 * Starts the game at the given level.
	 * 
	 * @param levelNo the number of the current level Precondition: 0 <= levelNo
	 *            < number of levels in the current pack.
	 */
	public void setStartingLevel(int levelNo)
	{

		// Reset the level pack.
		try {
			setLevelPack(levelPackName);
		} catch (Exception e) {
		}

		// Reset the current level.
		levelIndex = levelNo;
		currentLevel = levelPack[levelIndex];
		changeTitle();
		selectPlayer();
		centreView();
	}

	/**
	 * Loads and shows the solution for this level, if it exists.
	 */
	private void showSolution()
	{
		// If there is a solution show it.
		if (solnPanel.isSolution(levelIndex)) {
			solnPanel.setViewingSolution(levelIndex);
			parent.showSolnPanel();
			return;
		}

		// Otherwise prompt the user to build one.
		int choice = JOptionPane
				.showConfirmDialog(
						this,
						"No solution exists for this level. "
								+ "Would you like to generate one?\n(This could take some time)",
						"Solution not found", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			solnPanel.generateSolution(levelIndex);
			parent.showSolnPanel();
		}
	}
}
