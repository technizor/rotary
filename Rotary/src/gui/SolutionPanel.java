package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import level.EmptyTile;
import level.Level;
import level.LevelSolution;
import level.SolutionMaker;
import level.Tile;

/**
 * Panel that holds a solution to a level
 * 
 * @author Austin Tripp and Sherman Ying
 * @version June 15th 2013
 */
public class SolutionPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private static final long serialVersionUID = 1234567890;

	// GUI positioning elements.
	private static final int STEP_X = 835;
	private static final int STEP_Y = 14;
	private static final int STEP_W = 36;
	private static final int STEP_H = 36;
	private static final int EXIT_X = 871;
	private static final int EXIT_Y = 14;
	private static final int EXIT_W = 36;
	private static final int EXIT_H = 36;
	private static final int VIEW_H = 512;
	private static final int VIEW_W = 768;
	private static final int VIEW_X = 14;
	private static final int VIEW_Y = 14;
	private static final int MOVE_X = 830;
	private static final int MOVE_Y = 70;

	// Loaded images.
	private static BufferedImage borderFrame;
	private static BufferedImage selectorBox;
	private static BufferedImage[] rotateButtons;
	private static BufferedImage[] moveButtons;
	private static BufferedImage[] activateButtons;
	private static BufferedImage[] recentreButtons;
	private static BufferedImage exitButton;
	private static BufferedImage generateSolution;
	private static BufferedImage noSolution;
	private static BufferedImage stepButton;
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
		generateSolution = ImageIO.read(new File(root
				+ "\\gui\\generateSolution.png"));
		noSolution = ImageIO.read(new File(root + "\\gui\\noSolution.png"));
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
		exitButton = ImageIO.read(new File(root + "\\gui\\exit.png"));
		background = ImageIO.read(new File(root + "\\gui\\background.png"));
		stepButton = ImageIO.read(new File(root + "\\gui\\step.png"));
		Level.loadImages(texturePack);
	}

	// Other window elements.
	private RotaryFrame parent;
	private SolutionMaker solver;

	// Level pack data.
	private LevelSolution[] solutionPack;
	private int levelIndex;
	private LevelSolution currentSolution;
	private String solnPackName;

	// Other variables.
	private int selectCol;
	private int selectRow;
	private int viewX;
	private int viewY;
	private Point viewPos;
	private Point clickPos;

	/**
	 * Constructs a new GamePanel
	 * 
	 * @param frame the frame holding this panel
	 */
	public SolutionPanel(RotaryFrame frame)
	{

		// Set up panel.
		super();
		setSize(RotaryFrame.PANEL_SIZE);
		setMaximumSize(RotaryFrame.PANEL_SIZE);
		setMinimumSize(RotaryFrame.PANEL_SIZE);
		setPreferredSize(RotaryFrame.PANEL_SIZE);
		solutionPack = null;
		levelIndex = 0;
		currentSolution = new LevelSolution(new Level(
				new Tile[][] { { new EmptyTile() } }, ""),
				new LinkedList<Integer>());
		solnPackName = null;
		selectCol = 0;
		selectRow = 0;
		viewX = 0;
		viewY = 0;
		viewPos = null;
		clickPos = null;
		solver = new SolutionMaker(this);
		parent = frame;
		selectPlayer();
		centreView();

		// Add listeners.
		addMouseListener(this);
		addMouseMotionListener(this);

		// Load images.
		try {
			loadImages("default");
		} catch (IOException e) {
		}
	}

	/**
	 * Cancel the generation of the current solutions.
	 */
	public void cancelGeneration()
	{
		solver.terminateSolving();
	}

	/**
	 * Centres the view around the selection box.
	 */
	public void centreView()
	{
		int maxX = currentSolution.getWidth() * Tile.TILE_WIDTH - VIEW_W;
		int maxY = currentSolution.getHeight() * Tile.TILE_WIDTH - VIEW_H;
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
		if(currentSolution != null)
			parent.setTitle("Rotary - Solution for \"" + currentSolution + "\"");
		else
			parent.setTitle("Rotary - Attempting to Solve");
	}

	/**
	 * Generates a solution to the given level.
	 * 
	 * @param levelNo the index of the level to solve Precondition: levelNo is a
	 *            valid array index.
	 */
	public void generateSolution(int levelNo)
	{
		currentSolution = null;
		repaint();
		solver.solve(levelNo);
		levelIndex = levelNo;
	}

	/**
	 * Updates the panel to show that generation of a solution is complete.
	 */
	public void generationComplete()
	{
		setSolutionPack(solnPackName);
		if (solver.isPossible())
			setViewingSolution(levelIndex);
		repaint();
	}

	/**
	 * Gives the number of levels in the current level pack.
	 * 
	 * @return the number of levels in this pack.
	 */
	public int getNoOfLevels()
	{
		if (solutionPack != null)
			return solutionPack.length;
		return 0;
	}

	/**
	 * Gets a reference to the selected tile.
	 * 
	 * @return the tile that is selected.
	 */
	public Tile getSelectedTile()
	{
		return currentSolution.tileAt(selectRow, selectCol);
	}

	/**
	 * Checks to see whether a solution exists for a given level in this pack.
	 * 
	 * @param levelNo the index of the level to check Precondition: levelNo is a
	 *            valid array index.
	 * @return true if a solution exists, otherwise false.
	 */
	public boolean isSolution(int levelNo)
	{
		if (solutionPack == null)
			return false;
		if (solutionPack[levelNo] == null)
			return false;
		return true;
	}

	/**
	 * This method is not used.
	 */
	public void mouseClicked(MouseEvent event)
	{
	}

	/**
	 * Responds to the mouse being dragged.
	 * 
	 * @param evt the mouse being dragged.
	 */
	public void mouseDragged(MouseEvent evt)
	{
		if (currentSolution == null)
			return;

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
			else if (viewX + VIEW_W > currentSolution.getWidth()
					* Tile.TILE_WIDTH)
				viewX = currentSolution.getWidth() * Tile.TILE_WIDTH - VIEW_W;
			if (viewY < 0)
				viewY = 0;
			else if (viewY + VIEW_H > currentSolution.getHeight()
					* Tile.TILE_WIDTH)
				viewY = currentSolution.getHeight() * Tile.TILE_WIDTH - VIEW_H;
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
	 * Responds to the user pressing the mouse.
	 * 
	 * @param evt the user pressing the mouse.
	 */
	public void mousePressed(MouseEvent evt)
	{
		if (evt.getButton() != 1 || currentSolution == null)
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
		} else if (clickPos.x >= EXIT_X && clickPos.x < EXIT_X + EXIT_W
				&& clickPos.y >= EXIT_Y && clickPos.y < EXIT_Y + EXIT_H) {
			if (solver.isSolving())
				solver.terminateSolving();
			parent.showGamePanel();
		} else if (clickPos.x >= STEP_X && clickPos.x < STEP_X + STEP_W
				&& clickPos.y >= STEP_Y && clickPos.y < STEP_Y + STEP_H) {
			if (!solver.isSolving())
				currentSolution.step();
		}
		repaint();
	}

	/**
	 * Responds to the mouse being released.
	 * 
	 * @param evt the mouse being released.
	 */
	public void mouseReleased(MouseEvent evt)
	{
		if (evt.getButton() != 1 || currentSolution == null)
			return;
		// Blank the clicking position.
		clickPos = null;
		repaint();
	}

	/**
	 * Repaints the screen.
	 * 
	 * @param g the graphics context.
	 */
	public void paintComponent(Graphics g)
	{
		// Create an offscreen BufferedImage to simplify drawing the level view.
		BufferedImage img = new BufferedImage(VIEW_W, VIEW_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		g2D.drawImage(background, 0, 0, null);
		if (currentSolution != null && !solver.isSolving() && solver.isPossible()) {
				currentSolution.draw(g2D, viewX, viewY);

				// Draw a selection box around the selected tile.
				int offsetX = -(viewX % Tile.TILE_WIDTH);
				int offsetY = -(viewY % Tile.TILE_WIDTH);
				g2D.drawImage(selectorBox,
						(selectCol * Tile.TILE_WIDTH - viewX - offsetX)
								/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetX,
						(selectRow * Tile.TILE_WIDTH - viewY - offsetY)
								/ Tile.TILE_WIDTH * Tile.TILE_WIDTH + offsetY,
						Tile.TILE_WIDTH, Tile.TILE_WIDTH, null);
		}
		if (solver.isSolving()) {
			g2D.drawImage(generateSolution, 0, 0, null);
		} else if (!solver.isPossible()) {
			g2D.drawImage(noSolution, 0, 0, null);
		}
		g.drawImage(img, VIEW_X, VIEW_Y, null);

		// draw the image to the screen and add the border.
		if (borderFrame != null)
			((Graphics2D) g).drawImage(borderFrame, 0, 0, null);

		// Draw the side GUI.
		if (currentSolution != null && !solver.isSolving()
				&& solver.isPossible()) {
			g.setColor(Color.WHITE);
			g.drawString(currentSolution.nextMove(), MOVE_X, MOVE_Y);
		}
		((Graphics2D) g).drawImage(stepButton, STEP_X, STEP_Y, null);
		((Graphics2D) g).drawImage(exitButton, EXIT_X, EXIT_Y, null);
	}

	/**
	 * Moves the selection box to the location of the player. Should be used in
	 * conjunction with centreView().
	 */
	public void selectPlayer()
	{
		Point playerPos = currentSolution.getPlayerPosition();
		selectCol = playerPos.x;
		selectRow = playerPos.y;
	}

	/**
	 * Sets the current solution pack.
	 * 
	 * @param packName the name of the current level pack.
	 * @throws IOException if IO does not work.
	 * @throws FileNotFoundException if the desired file is not found.
	 * @throws ClassNotFoundException if the Level class is not found.
	 */
	public void setSolutionPack(String packName)
	{

		// Try to read in the solution file.
		try {
			// Do this first in case the input file is not found.
			solnPackName = packName;
			solver.setLevelPack(packName);
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream("data\\solutions\\" + packName
							+ ".soln"));
			solutionPack = (LevelSolution[]) fileIn.readObject();
			fileIn.close();
			currentSolution = solutionPack[levelIndex];
		} catch (Exception e) {
		}
	}

	/**
	 * Starts the game at the given level.
	 * 
	 * @param levelNo the number of the current level Precondition: 0 <= levelNo
	 *            < number of levels in the current pack.
	 */
	public void setViewingSolution(int levelNo)
	{

		// Reset the level pack.
		try {
			setSolutionPack(solnPackName);
		} catch (Exception e) {
		}

		levelIndex = levelNo;
		currentSolution = solutionPack[levelIndex];
		changeTitle();
		selectPlayer();
		centreView();
	}

}