package gui;

import java.awt.Color;
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
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The panel for the game's main menu/starting interface.
 * 
 * @author Sherman Ying and Austin Tripp
 * @version June 15th 2013
 */
public class MenuPanel extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener
{
	private static final long serialVersionUID = 8475751505006519027L;

	// GUI positioning constants.
	private static final int LEVEL_X = 144;
	private static final int LEVEL_W = 512;
	private static final int LEVEL_SX = 64;
	private static final int BUTTON_X = 803;
	private static final int BUTTON_Y = 179;
	private static final int BUTTON_W = 136;
	private static final int BUTTON_H = 180;
	private static final int BUTTON_SX = 136;
	private static final int BUTTON_SY = 36;
	private static final int LEVEL_Y = 95;
	private static final int LEVEL_H = 320;
	private static final int LEVEL_SY = 64;
	private static final int PACK_X = 139;
	private static final int PACK_Y = 90;
	private static final int PACK_W = 520;
	private static final int PACK_H = 360;
	private static final int PACK_SX = 260;
	private static final int PACK_SY = 36;
	private static final int VIEW_X = 14;
	private static final int VIEW_Y = 14;
	private static final int VIEW_W = 768;
	private static final int VIEW_H = 512;
	private static final int NEW_X = 336;
	private static final int NEW_Y = 464;
	private static final int NEW_W = 128;
	private static final int NEW_H = 56;
	private static final int PREV_X = 129;
	private static final int PREV_Y = 460;
	private static final int PREV_W = 32;
	private static final int PREV_H = 32;
	private static final int NEXT_X = 637;
	private static final int NEXT_Y = 460;
	private static final int NEXT_W = 32;
	private static final int NEXT_H = 32;
	private static final int NEXTHELP_X = 742;
	private static final int NEXTHELP_Y = 486;
	private static final int NEXTHELP_W = 40;
	private static final int NEXTHELP_H = 40;
	private static final int PREVHELP_X = 14;
	private static final int PREVHELP_Y = 486;
	private static final int PREVHELP_W = 40;
	private static final int PREVHELP_H = 40;
	private static final int DELETE_X = 601;
	private static final int DELETE_Y = 464;
	private static final int DELETE_W = 28;
	private static final int DELETE_H = 28;
	private static final int DUPLICATE_X = 565;
	private static final int DUPLICATE_Y = 464;
	private static final int DUPLICATE_W = 28;
	private static final int DUPLICATE_H = 28;
	private static final int KEY_X = 137;
	private static final int KEY_Y = 91;
	private static final int KEY_W = 520;
	private static final int KEY_H = 360;
	private static final int KEY_SX = 120;
	private static final int KEY_SY = 45;
	private static final int TITLE_STATE = 0;
	private static final int PLAY_PACK_STATE = 100;
	private static final int PLAY_LEVEL_STATE = 101;
	private static final int EDIT_PACK_STATE = 200;
	private static final int EDIT_LEVEL_STATE = 201;
	private static final int OPTION_STATE = 300;
	private static final int HELP_STATE = 400;
	private static final int PACK_PAGE_SIZE = 20;
	private static final int LEVEL_PAGE_SIZE = 40;
	private static final String ABOUT = "Created by Sherman Ying and Austin Tripp. \u00A92013";

	// Loaded images.
	private static BufferedImage borderFrame;
	private static BufferedImage[] menuButtons;
	private static BufferedImage packSelect;
	private static BufferedImage[] levelSelect;
	private static BufferedImage[] helpSlides;
	private static BufferedImage newButton;
	private static BufferedImage titleScreen;
	private static BufferedImage background;
	private static BufferedImage prevHelpButton;
	private static BufferedImage nextHelpButton;
	private static BufferedImage keyConfig;

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
		menuButtons = new BufferedImage[2];
		levelSelect = new BufferedImage[4];
		menuButtons[0] = ImageIO
				.read(new File(root + "\\gui\\menuButtons0.png"));
		menuButtons[1] = ImageIO
				.read(new File(root + "\\gui\\menuButtons1.png"));
		packSelect = ImageIO.read(new File(root + "\\gui\\packSelect.png"));
		levelSelect[0] = ImageIO
				.read(new File(root + "\\gui\\levelSelect0.png"));
		levelSelect[1] = ImageIO
				.read(new File(root + "\\gui\\levelSelect1.png"));
		levelSelect[2] = ImageIO
				.read(new File(root + "\\gui\\levelSelect2.png"));
		levelSelect[3] = ImageIO
				.read(new File(root + "\\gui\\levelSelect3.png"));
		newButton = ImageIO.read(new File(root + "\\gui\\newButton.png"));
		titleScreen = ImageIO.read(new File(root + "\\gui\\title.png"));
		background = ImageIO.read(new File(root + "\\gui\\background.png"));
		helpSlides = new BufferedImage[23];
		for (int slide = 0; slide < 23; slide++) {
			helpSlides[slide] = ImageIO.read(new File(root + "\\help\\slide"
					+ slide + ".png"));
		}
		prevHelpButton = ImageIO.read(new File(root + "\\gui\\prev.png"));
		nextHelpButton = ImageIO.read(new File(root + "\\gui\\next.png"));
		keyConfig = ImageIO.read(new File(root + "\\gui\\keyConfig.png"));
	}

	// Other window elements.
	private final RotaryFrame parent;
	private final GamePanel gamePanel;
	private final EditorPanel editorPanel;

	// Menu state variables.
	private int pageNumber;
	private int screenState;
	private int keyChosen;
	private Point clickPos;
	private Point mousePos;

	/**
	 * Constructs a new MenuPanel.
	 * 
	 * @param parent the frame containing this panel.
	 * @param gamePanel the panel for the gameplay.
	 * @param editorPanel the panel for level editing.
	 */
	public MenuPanel(RotaryFrame parent, GamePanel gamePanel,
			EditorPanel editorPanel)
	{
		// Set up panel.
		super();
		setSize(RotaryFrame.PANEL_SIZE);
		setMaximumSize(RotaryFrame.PANEL_SIZE);
		setMinimumSize(RotaryFrame.PANEL_SIZE);
		setPreferredSize(RotaryFrame.PANEL_SIZE);
		setFocusable(true);
		screenState = TITLE_STATE;
		pageNumber = 0;
		keyChosen = -1;
		this.parent = parent;
		this.gamePanel = gamePanel;
		this.editorPanel = editorPanel;

		// Add listeners.
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		// Load images.
		try {
			loadImages("default");
		} catch (IOException e) {
		}
	}

	/**
	 * Helper method for drawing the help menu.
	 * 
	 * @param g2D the graphics context of the main screen.
	 */
	private void drawHelpScreen(Graphics2D g2D)
	{
		g2D.drawImage(helpSlides[pageNumber], 0, 0, null);
		g2D.drawImage(prevHelpButton, PREVHELP_X - VIEW_X, PREVHELP_Y - VIEW_Y,
				null);
		g2D.drawImage(nextHelpButton, NEXTHELP_X - VIEW_X, NEXTHELP_Y - VIEW_Y,
				null);
	}

	/**
	 * Helper method for drawing the level menu.
	 * 
	 * @param g2D the graphics context of the main screen.
	 */
	private void drawLevelScreen(Graphics2D g2D)
	{
		if (screenState == EDIT_LEVEL_STATE)
			g2D.drawImage(levelSelect[0], 0, 0, null);
		if (screenState == PLAY_LEVEL_STATE)
			g2D.drawImage(levelSelect[1], 0, 0, null);

		int levelCount = screenState == PLAY_LEVEL_STATE ? gamePanel
				.getNoOfLevels() : editorPanel.getNoOfLevels();
		int levelChosen = ((mousePos.x - LEVEL_X - VIEW_X) / LEVEL_SX) % 8
				+ ((mousePos.y - LEVEL_Y - VIEW_Y) / LEVEL_SY) * 8;
		for (int level = 0; level < levelCount; level++) {
			int drawX = LEVEL_X + (level % 8) * LEVEL_SX - VIEW_X;
			int drawY = LEVEL_Y + (level / 8) * LEVEL_SY - VIEW_Y;
			if (level == levelChosen)
				g2D.drawImage(levelSelect[3].getSubimage(drawX, drawY,
						LEVEL_SX, LEVEL_SY), drawX, drawY, null);
			else
				g2D.drawImage(levelSelect[2].getSubimage(drawX, drawY,
						LEVEL_SX, LEVEL_SY), drawX, drawY, null);
		}

		// Add level button for edit.
		if (screenState == EDIT_LEVEL_STATE)
			g2D.drawImage(newButton, NEW_X - VIEW_X, NEW_Y - VIEW_Y, null);
	}

	/**
	 * Helper method for drawing the menu buttons on the side bar.
	 * 
	 * @param g the graphics context of the panel.
	 */
	private void drawMenuButtons(Graphics g)
	{
		BufferedImage buttons = new BufferedImage(BUTTON_W, BUTTON_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = buttons.createGraphics();
		g2D.drawImage(menuButtons[0], 0, 0, null);

		// Draw a pressed version of the clicked button.
		if (clickPos != null && clickPos.x >= BUTTON_X
				&& clickPos.y < BUTTON_X + BUTTON_W && clickPos.y >= BUTTON_Y
				&& clickPos.y < BUTTON_Y + BUTTON_H) {
			int buttonPressed = (clickPos.y - BUTTON_Y) / BUTTON_SY;
			g2D.drawImage(menuButtons[1].getSubimage(0, BUTTON_SY
					* buttonPressed, BUTTON_SX, BUTTON_SY), 0, BUTTON_SY
					* buttonPressed, null);
		}
		((Graphics2D) g).drawImage(buttons, BUTTON_X, BUTTON_Y, null);
	}

	/**
	 * Helper method for drawing the options menu.
	 * 
	 * @param g2D the graphics context of the main screen.
	 */
	private void drawOptionScreen(Graphics2D g2D)
	{
		g2D.drawImage(keyConfig, 0, 0, null);

		// Display the current key configuration.
		for (int key = 0; key < 7; key++) {
			if (keyChosen != key)
				g2D.drawString(KeyEvent.getKeyText(KeyInput.getKey(key)), KEY_X
						- VIEW_X + KEY_W - KEY_SX, KEY_Y - VIEW_Y + KEY_SY / 2
						+ KEY_SY * key);
			else
				g2D.drawString("Press any key",
						KEY_X - VIEW_X + KEY_W - KEY_SX, KEY_Y - VIEW_Y
								+ KEY_SY / 2 + KEY_SY * key);
		}
	}

	/**
	 * Helper method for drawing the pack menu.
	 * 
	 * @param g2D the graphics context of the main screen.
	 */
	private void drawPackScreen(Graphics2D g2D)
	{
		g2D.drawImage(packSelect, 0, 0, null);

		// Draw the names of this page of 20 packs.
		ArrayList<String> packs = null;
		if (screenState == PLAY_PACK_STATE)
			packs = getFileNames("data\\levels\\", ".pck");
		else if (screenState == EDIT_PACK_STATE)
			packs = getFileNames("data\\editor\\", ".tmpk");
		g2D.setColor(Color.WHITE);
		int packOffset = pageNumber * PACK_PAGE_SIZE;
		for (int pack = 0; pack < 20 && pack < packs.size(); pack++) {
			g2D.drawString(packs.get(pack + packOffset), PACK_X + (pack % 2)
					* PACK_SX, PACK_Y + (pack / 2) * PACK_SY + 8);
		}

		// Add level button.
		if (screenState == EDIT_PACK_STATE)
			g2D.drawImage(newButton, NEW_X - VIEW_X, NEW_Y - VIEW_Y, null);
	}

	/**
	 * Helper method for drawing the main screen based on the screen state.
	 * 
	 * @param g the graphics context of the panel.
	 */
	private void drawScreenState(Graphics g)
	{
		// Draw background image.
		BufferedImage img = new BufferedImage(VIEW_W, VIEW_H,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		g2D.drawImage(background, 0, 0, null);

		// Draw screen state.
		if (screenState == TITLE_STATE) {
			drawTitleScreen(g2D);
		} else if (screenState == PLAY_PACK_STATE
				|| screenState == EDIT_PACK_STATE) {
			drawPackScreen(g2D);
		} else if (screenState == PLAY_LEVEL_STATE
				|| screenState == EDIT_LEVEL_STATE) {
			drawLevelScreen(g2D);
		} else if (screenState == OPTION_STATE) {
			drawOptionScreen(g2D);
		} else if (screenState == HELP_STATE) {
			drawHelpScreen(g2D);
		}

		// Draw to panel.
		g.drawImage(img, VIEW_X, VIEW_Y, null);
	}

	/**
	 * Helper method for drawing the title screen.
	 * 
	 * @param g2D the graphics context of the main screen.
	 */
	private void drawTitleScreen(Graphics2D g2D)
	{
		g2D.drawImage(titleScreen, 0, 0, null);
		g2D.setColor(Color.WHITE);
		g2D.drawString(ABOUT, VIEW_W/8*5, VIEW_H-14);
	}

	/**
	 * Begins to edit a selected level.
	 * 
	 * @param selection the number of the selected level.
	 */
	public void editLevel(int selection)
	{
		editorPanel.loadLevel(selection);
		parent.showEditorPanel();
	}

	/**
	 * Helper method for triggering an action in the level edit screen.
	 */
	private void editLevelAction()
	{
		// Add a new level.
		if (clickPos.x >= NEW_X && clickPos.x < NEW_X + NEW_W
				&& clickPos.y >= NEW_Y && clickPos.y < NEW_Y + NEW_H
				&& gamePanel.getNoOfLevels() < LEVEL_PAGE_SIZE) {
			String levelName = JOptionPane.showInputDialog(this,
					"Enter the name of this level: ");
			if (levelName == null)
				return;
			if (levelName.equals("")) {
				JOptionPane.showMessageDialog(this,
						"You must type a name for this level!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			editorPanel.newLevel(levelName);
			parent.showEditorPanel();
		}

		// Load an existing level.
		else if (clickPos.x >= LEVEL_X && clickPos.x < LEVEL_X + LEVEL_W
				&& clickPos.y >= LEVEL_Y && clickPos.y < LEVEL_Y + LEVEL_H) {
			int levelChosen = ((clickPos.x - LEVEL_X) / LEVEL_SX) % 8
					+ ((clickPos.y - LEVEL_Y) / LEVEL_SY) * 8;
			if (levelChosen >= editorPanel.getNoOfLevels())
				return;
			editorPanel.loadLevel(levelChosen);
			parent.showEditorPanel();
		}

		// Publish the level.
		else if (clickPos.x >= PREV_X && clickPos.x < PREV_X + PREV_W
				&& clickPos.y >= PREV_Y && clickPos.y < PREV_Y + PREV_H) {
			editorPanel.publishLevelPack();
			screenState = TITLE_STATE;
		}

		// Rename the pack.
		else if (clickPos.x >= NEXT_X && clickPos.x < NEXT_X + NEXT_W
				&& clickPos.y >= NEXT_Y && clickPos.y < NEXT_Y + NEXT_H) {
			editorPanel.renamePack();
		}

		// Delete this pack.
		else if (clickPos.x >= DELETE_X && clickPos.x < DELETE_X + DELETE_W
				&& clickPos.y >= DELETE_Y && clickPos.y < DELETE_Y + DELETE_H) {
			int chosenAction = JOptionPane
					.showOptionDialog(
							parent,
							"Are you sure you wish to delete this pack?\nWarning: This cannot be undone.",
							"Delete this level?", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, null,
							JOptionPane.NO_OPTION);
			if (chosenAction == JOptionPane.YES_OPTION) {
				editorPanel.deletePack(editorPanel.getPackName());
				screenState = EDIT_PACK_STATE;
			}
		}

		// Duplicate this pack.
		else if (clickPos.x >= DUPLICATE_X
				&& clickPos.x < DUPLICATE_X + DUPLICATE_W
				&& clickPos.y >= DUPLICATE_Y
				&& clickPos.y < DUPLICATE_Y + DUPLICATE_H) {
			editorPanel.duplicatePack();
		}
	}

	/**
	 * Helper method for triggering an action in the pack edit screen.
	 */
	private void editPackAction()
	{
		ArrayList<String> packs = getFileNames("data\\editor\\", ".tmpk");

		// Add a new pack.
		if (clickPos.x >= NEW_X && clickPos.x < NEW_X + NEW_W
				&& clickPos.y >= NEW_Y && clickPos.y < NEW_Y + NEW_H) {
			String packName = JOptionPane.showInputDialog(this,
					"Enter the name of this level pack: ");
			if (packName == null)
				return;
			if (packName.equals("")) {
				JOptionPane.showMessageDialog(this,
						"You must type a name for this level pack!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			editorPanel.newLevelPack(packName);
			screenState = EDIT_LEVEL_STATE;
			pageNumber = 0;
		}

		// Load an existing pack.
		else if (clickPos.x >= PACK_X && clickPos.x < PACK_X + PACK_W
				&& clickPos.y >= PACK_Y && clickPos.y < PACK_Y + PACK_H) {

			int packNo = pageNumber * PACK_PAGE_SIZE;
			packNo += ((clickPos.x - PACK_X) / PACK_SX) % 2;
			packNo += ((clickPos.y - PACK_Y) / PACK_SY) * 2;
			if (packNo < packs.size()) {
				String packName = packs.get(packNo);
				try {
					editorPanel.loadLevelPack(packName);
					screenState = EDIT_LEVEL_STATE;
					pageNumber = 0;
				} catch (ClassNotFoundException e) {
				} catch (IOException e) {
				}
			}
		}

		// Turn the page.
		else if (clickPos.x >= PREV_X && clickPos.x < PREV_X + PREV_W
				&& clickPos.y >= PREV_Y && clickPos.y < PREV_Y + PREV_H) {
			int noOfPacks = packs.size();
			int noOfPages = noOfPacks / PACK_PAGE_SIZE + noOfPacks
					% PACK_PAGE_SIZE == 0 ? 0 : 1;
			if (noOfPages == 0)
				noOfPages = 1;
			pageNumber = (noOfPages + pageNumber - 1) % noOfPages;
		} else if (clickPos.x >= NEXT_X && clickPos.x < NEXT_X + NEXT_W
				&& clickPos.y >= NEXT_Y && clickPos.y < NEXT_Y + NEXT_H) {
			int noOfPacks = packs.size();
			int noOfPages = noOfPacks / PACK_PAGE_SIZE + noOfPacks
					% PACK_PAGE_SIZE == 0 ? 0 : 1;
			if (noOfPages == 0)
				noOfPages = 1;
			pageNumber = (pageNumber + 1) % noOfPages;
		}
	}

	/**
	 * Finds all file names with the given extension
	 * 
	 * @param fileLoc the location of the files to search for
	 * @param fileType the file type (extension) to search for Precondition: the
	 *            extension starts with the '.' character
	 * @return an ArrayList containing the name of every level pack available
	 */
	public ArrayList<String> getFileNames(String fileLoc, String fileType)
	{

		// Find the level directory and the levels within it.
		File folder = new File(fileLoc);
		File[] levels = folder.listFiles();

		// Add all appropriate files to the list.
		ArrayList<String> fileNames = new ArrayList<String>();
		if (levels != null)
			for (File file : levels) {
				String name = file.getName();
				int extensionPos = name.lastIndexOf('.');
				if (extensionPos >= 0) {
					String extension = name.substring(extensionPos);
					if (extension.equals(fileType))
						fileNames.add(name.substring(0, extensionPos));
				}
			}
		return fileNames;
	}

	/**
	 * Helper method for triggering an action in the help screen.
	 */
	private void helpAction()
	{
		// Turn the help page.
		if (clickPos.x >= PREVHELP_X && clickPos.x < PREVHELP_X + PREVHELP_W
				&& clickPos.y >= PREVHELP_Y
				&& clickPos.y < PREVHELP_Y + PREVHELP_H) {
			pageNumber = (helpSlides.length + pageNumber - 1)
					% helpSlides.length;
		} else if (clickPos.x >= NEXTHELP_X
				&& clickPos.x < NEXTHELP_X + NEXTHELP_W
				&& clickPos.y >= NEXTHELP_Y
				&& clickPos.y < NEXTHELP_Y + NEXTHELP_H) {
			pageNumber = (pageNumber + 1) % helpSlides.length;
		}
	}

	@Override
	/**
	 * Handles the key configuration.
	 * @param evt the key event to modify the key controls with.
	 */
	public void keyPressed(KeyEvent evt)
	{
		if (keyChosen != -1 && screenState == OPTION_STATE) {
			KeyInput.modifyKey(keyChosen, evt.getKeyCode());
			keyChosen = -1;
			repaint();
		}
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void keyReleased(KeyEvent evt)
	{

	}

	@Override
	/**
	 * This method is not used.
	 */
	public void keyTyped(KeyEvent evt)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseClicked(MouseEvent evt)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseDragged(MouseEvent evt)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseEntered(MouseEvent evt)
	{
	}

	@Override
	/**
	 * This method is not used.
	 */
	public void mouseExited(MouseEvent evt)
	{
	}

	@Override
	/**
	 * Updates the mouse over highlighting of a level selection screen.
	 * @param evt the mouse event to update the screen with.
	 */
	public void mouseMoved(MouseEvent evt)
	{
		mousePos = evt.getPoint();
		if (screenState == PLAY_LEVEL_STATE || screenState == EDIT_LEVEL_STATE)
			repaint();
	}

	@Override
	/**
	 * Updates the menu.
	 * @param evt the mouse event to update the menu with.
	 */
	public void mousePressed(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		clickPos = evt.getPoint();

		// Menu click.
		if (clickPos.x >= BUTTON_X && clickPos.x < BUTTON_X + BUTTON_W
				&& clickPos.y >= BUTTON_Y && clickPos.y < BUTTON_Y + BUTTON_H) {
			int buttonPressed = (clickPos.y - BUTTON_Y) / BUTTON_SY;
			if (buttonPressed == 0) {
				// Play.
				screenState = PLAY_PACK_STATE;
				pageNumber = 0;
			} else if (buttonPressed == 1) {
				// Editor.
				screenState = EDIT_PACK_STATE;
				pageNumber = 0;
			} else if (buttonPressed == 2) {
				// Options.
				screenState = OPTION_STATE;
				keyChosen = -1;
				pageNumber = 0;
			} else if (buttonPressed == 3) {
				// Help.
				screenState = HELP_STATE;
				pageNumber = 0;
			} else if (buttonPressed == 4) {
				// Exit or back out.
				if (screenState == TITLE_STATE)
					System.exit(0);
				else
					screenState = TITLE_STATE;
			}
		}

		// Main screen click.
		else if (screenState == PLAY_PACK_STATE) {
			playPackAction();
		} else if (screenState == PLAY_LEVEL_STATE) {
			playLevelAction();
		} else if (screenState == EDIT_PACK_STATE) {
			editPackAction();
		} else if (screenState == EDIT_LEVEL_STATE) {
			editLevelAction();
		} else if (screenState == OPTION_STATE) {
			optionAction();
		} else if (screenState == HELP_STATE) {
			helpAction();
		}
		repaint();
	}

	@Override
	/**
	 * Handles mouse release events.
	 * @param evt the mouse release event to process.
	 */
	public void mouseReleased(MouseEvent evt)
	{
		if (evt.getButton() != 1)
			return;
		clickPos = null;
		repaint();
	}

	/**
	 * Creates a new level pack for editing.
	 * 
	 * @param packName the name of the Level Pack to be created.
	 */
	public void newLevelPack(String packName)
	{
		editorPanel.newLevelPack(packName);
	}

	/**
	 * Helper method for triggering an action in the option screen.
	 */
	private void optionAction()
	{
		if (clickPos.x >= KEY_X && clickPos.x < KEY_X + KEY_W
				&& clickPos.y >= KEY_Y && clickPos.y < KEY_Y + KEY_H) {
			keyChosen = (clickPos.y - KEY_Y) / KEY_SY;
		}
	}

	/**
	 * Repaints the menu screen.
	 * 
	 * @param g the graphics context to paint to.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		drawScreenState(g);
		if (borderFrame != null)
			((Graphics2D) g).drawImage(borderFrame, 0, 0, null);

		drawMenuButtons(g);
	}

	/**
	 * Begins to play a selected level.
	 * 
	 * @param selection the number of the selected level.
	 */
	public void playLevel(int selection)
	{
		gamePanel.setStartingLevel(selection);
		parent.showGamePanel();
	}

	/**
	 * Helper method for triggering an action in the level play screen.
	 */
	private void playLevelAction()
	{
		if (clickPos.x >= LEVEL_X && clickPos.x < LEVEL_X + LEVEL_W
				&& clickPos.y >= LEVEL_Y && clickPos.y < LEVEL_Y + LEVEL_H) {
			int levelChosen = ((clickPos.x - LEVEL_X) / LEVEL_SX) % 8
					+ ((clickPos.y - LEVEL_Y) / LEVEL_SY) * 8;
			if (levelChosen >= gamePanel.getNoOfLevels())
				return;
			gamePanel.setStartingLevel(levelChosen);
			parent.showGamePanel();
		}
	}

	/**
	 * Helper method for triggering an action in the pack play screen.
	 */
	private void playPackAction()
	{
		ArrayList<String> packs = getFileNames("data\\levels\\", ".pck");
		if (clickPos.x >= PREV_X && clickPos.x < PREV_X + PREV_W
				&& clickPos.y >= PREV_Y && clickPos.y < PREV_Y + PREV_H) {
			int noOfPacks = packs.size();
			int noOfPages = noOfPacks / PACK_PAGE_SIZE + noOfPacks
					% PACK_PAGE_SIZE == 0 ? 0 : 1;
			if (noOfPages == 0)
				noOfPages = 1;
			pageNumber = (noOfPages + pageNumber - 1) % noOfPages;

		} else if (clickPos.x >= NEXT_X && clickPos.x < NEXT_X + NEXT_W
				&& clickPos.y >= NEXT_Y && clickPos.y < NEXT_Y + NEXT_H) {
			int noOfPacks = packs.size();
			int noOfPages = noOfPacks / PACK_PAGE_SIZE + noOfPacks
					% PACK_PAGE_SIZE == 0 ? 0 : 1;
			if (noOfPages == 0)
				noOfPages = 1;
			pageNumber = (pageNumber + 1) % noOfPages;
		} else if (clickPos.x >= PACK_X && clickPos.x < PACK_X + PACK_W
				&& clickPos.y >= PACK_Y && clickPos.y < PACK_Y + PACK_H) {

			int packNo = pageNumber * PACK_PAGE_SIZE;
			packNo += ((clickPos.x - PACK_X) / PACK_SX) % 2;
			packNo += ((clickPos.y - PACK_Y) / PACK_SY) * 2;
			if (packNo < packs.size()) {
				String packName = packs.get(packNo);
				try {
					gamePanel.setLevelPack(packName);
					screenState = PLAY_LEVEL_STATE;
					pageNumber = 0;
				} catch (ClassNotFoundException e) {
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Selects the given level pack for editing.
	 * 
	 * @param packName the name of the Level Pack being selected.
	 */
	public void selectEditPack(String packName)
	{
		try {
			editorPanel.loadLevelPack(packName);
		} catch (Exception e) {
		}
	}

	/**
	 * Selects the given level pack for play.
	 * 
	 * @param packName the name of the Level Pack being selected.
	 */
	public void selectPlayPack(String packName)
	{
		try {
			gamePanel.setLevelPack(packName);
		} catch (Exception e) {
		}
	}
}
