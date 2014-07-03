package gui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The frame that holds the game and runs the program.
 * 
 * @author Austin Tripp & Sherman Ying
 * @version June 15th 2013
 */
public class RotaryFrame extends JFrame
{

	private static final long serialVersionUID = 1L;

	// Holds the size of the panel.
	public final static Dimension PANEL_SIZE = new Dimension(960, 540);

	// Holds all the folder names.
	public final static String[] FOLDERS = { "data", "data\\editor",
			"data\\levels", "data\\solutions", "textures", "textures\\default", "textures\\default\\gui",
			"textures\\default\\help", "textures\\default\\player", "textures\\default\\tile" };

	// Holds all the image names.
	public final static String[] GUI_FILES = { "activate0.png",
			"activate1.png", "background.png", "background1.png", "clock0.png",
			"clock1.png", "colourPalette.png", "colourPaletteSelect.png",
			"componentSelect.png", "counterclock0.png", "counterclock1.png",
			"drag0.png", "drag1.png", "drag2.png", "drag3.png", "drag4.png",
			"drag5.png", "drag6.png", "drag7.png", "drag8.png", "drag9.png",
			"exit.png", "frame.png", "icon.png", "levelSelect.png",
			"levelSelect0.png", "levelSelect1.png", "levelSelect2.png",
			"levelSelect3.png", "menuButtons0.png", "menuButtons1.png",
			"movedown0.png", "movedown1.png", "moveleft0.png", "moveleft1.png",
			"moveright0.png", "moveright1.png", "moveup0.png", "moveup1.png",
			"newButton.png", "next.png", "packSelect.png", "prev.png",
			"recentre0.png", "recentre1.png", "remove.png", "rename.png",
			"save.png", "selector.png", "target.png", "tilePalette.png",
			"tileView.png", "title.png", "keyConfig.png", "solve.png",
			"generateSolution.png", "step.png" };
	public final static String[] HELP_FILES = { "slide0.png", "slide1.png",
			"slide10.png", "slide11.png", "slide12.png", "slide13.png",
			"slide14.png", "slide15.png", "slide16.png", "slide17.png",
			"slide18.png", "slide19.png", "slide2.png", "slide20.png",
			"slide21.png", "slide22.png", "slide3.png", "slide4.png",
			"slide5.png", "slide6.png", "slide7.png", "slide8.png",
			"slide9.png" };
	public final static String[] PLAYER_FILES = { "player.png" };
	public final static String[] TILE_FILES = { "connector.png",
			"connector0.png", "connector1.png", "connector2.png",
			"connector3.png", "connector4.png", "connector5.png",
			"connector6.png", "connector7.png", "finish.png", "finish0.png",
			"finish1.png", "finish2.png", "finish3.png", "finish4.png",
			"finish5.png", "finish6.png", "finish7.png", "key.png", "key0.png",
			"key1.png", "key2.png", "key3.png", "key4.png", "key5.png",
			"key6.png", "key7.png", "launcher0.png", "launcher1.png",
			"launcher2.png", "launcher3.png", "lock.png", "lock0.png",
			"lock1.png", "lock2.png", "lock3.png", "lock4.png", "lock5.png",
			"lock6.png", "lock7.png", "paint.png", "paint0.png", "paint1.png",
			"paint2.png", "paint3.png", "paint4.png", "paint5.png",
			"paint6.png", "paint7.png", "paintBrush.png", "paintBrush0.png",
			"paintBrush1.png", "paintBrush2.png", "paintBrush3.png",
			"paintBrush4.png", "paintBrush5.png", "paintBrush6.png",
			"paintBrush7.png", "start.png", "static.png", "tile.png",
			"tile0.png", "tile1.png", "tile2.png", "tile3.png", "tile4.png",
			"tile5.png", "tile6.png", "tile7.png", "transport.png",
			"transport0.png", "transport1.png", "transport2.png",
			"transport3.png", "transport4.png", "transport5.png",
			"transport6.png", "transport7.png", "unlock.png", "unlock0.png",
			"unlock1.png", "unlock2.png", "unlock3.png", "unlock4.png",
			"unlock5.png", "unlock6.png", "unlock7.png" };

	public static void main(String[] args)
	{

		// Make sure all the folders are there.
		verifyFolders();

		// Send an error if there are missing images.
		ArrayList<String> missingImages = getMissingImages();
		if (!missingImages.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage
					.append("The game could not be launched due to the following missing resources:");
			for (String error : missingImages) {
				errorMessage.append('\n');
				errorMessage.append(error);
			}
			errorMessage.append("\n\nProgram will terminate");
			JOptionPane.showMessageDialog(null, errorMessage.toString(),
					"Fatal Program error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// Launch the game.
		new RotaryFrame();
	}

	/**
	 * Checks to make sure all the folders are there, and creates them if they
	 * are not.
	 */
	public static void verifyFolders()
	{
		for (String name : FOLDERS) {
			File folder = new File(name);
			if (!folder.exists())
				folder.mkdir();
		}
	}

	/**
	 * Checks to see whether all the game's images are present.
	 * 
	 * @return a list containing all the missing images.
	 */
	public static ArrayList<String> getMissingImages()
	{

		// Verify the gui.
		ArrayList<String> missingGui = new ArrayList<String>();
		for (String file : GUI_FILES)
			try {
				ImageIO.read(new File("textures\\default\\gui\\" + file));
			} catch (Exception e)// Any exception.
			{
				missingGui.add(file);
			}

		// Verify the help.
		ArrayList<String> missingHelp = new ArrayList<String>();
		for (String file : HELP_FILES)
			try {
				ImageIO.read(new File("textures\\default\\help\\" + file));
			} catch (Exception e)// Any exception.
			{
				missingHelp.add(file);
			}

		// Verify the player.
		ArrayList<String> missingPlayer = new ArrayList<String>();
		for (String file : PLAYER_FILES)
			try {
				ImageIO.read(new File("textures\\default\\player\\" + file));
			} catch (Exception e)// Any exception.
			{
				missingPlayer.add(file);
			}

		// Verify the tiles.
		ArrayList<String> missingTiles = new ArrayList<String>();
		for (String file : TILE_FILES)
			try {
				ImageIO.read(new File("textures\\default\\tile\\" + file));
			} catch (Exception e)// Any exception.
			{
				missingTiles.add(file);
			}

		// Combine them all into an error message.
		ArrayList<String> missingFiles = new ArrayList<String>();
		if (!missingGui.isEmpty()) {
			missingFiles.add("\nFolder: textures\\default\\gui:");
			missingFiles.addAll(missingGui);
		}
		if (!missingHelp.isEmpty()) {
			missingFiles.add("\nFolder: textures\\default\\help:");
			missingFiles.addAll(missingHelp);
		}
		if (!missingPlayer.isEmpty()) {
			missingFiles.add("\nFolder: textures\\default\\player:");
			missingFiles.addAll(missingPlayer);
		}
		if (!missingTiles.isEmpty()) {
			missingFiles.add("\nFolder: textures\\default\\tiles:");
			missingFiles.addAll(missingTiles);
		}
		return missingFiles;
	}

	// Other window elements.
	private final GamePanel gamePanel;
	private final MenuPanel menuPanel;
	private final EditorPanel editorPanel;
	private final SolutionPanel solnPanel;

	/**
	 * Constructs a new Rotary Game.
	 */
	public RotaryFrame()
	{
		super("Rotary");

		KeyInput.loadKeyPreferences();

		// Add the four panels.
		solnPanel = new SolutionPanel(this);
		gamePanel = new GamePanel(this, solnPanel);
		editorPanel = new EditorPanel(this);
		menuPanel = new MenuPanel(this, gamePanel, editorPanel);
		showMenuPanel();

		// Load the icon.
		try {
			this.setIconImage(ImageIO.read(new File(
					"textures\\default\\gui\\icon.png")));
		} catch (IOException e) {
		}

		// Pack the frame and centre the screen.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);

		// Make the window visible.
		setVisible(true);
	}

	/**
	 * Displays the editor panel.
	 */
	public void showEditorPanel()
	{
		solnPanel.setVisible(false);
		editorPanel.setVisible(true);
		menuPanel.setVisible(false);
		gamePanel.setVisible(false);
		getContentPane().add(editorPanel);
		editorPanel.requestFocusInWindow();
		editorPanel.changeTitle();
	}

	/**
	 * Displays the game panel.
	 */
	public void showGamePanel()
	{
		solnPanel.setVisible(false);
		editorPanel.setVisible(false);
		menuPanel.setVisible(false);
		gamePanel.setVisible(true);
		gamePanel.requestFocusInWindow();
		getContentPane().add(gamePanel);
		gamePanel.requestFocusInWindow();
		gamePanel.changeTitle();
	}

	/**
	 * Displays the menu panel.
	 */
	public void showMenuPanel()
	{
		solnPanel.setVisible(false);
		editorPanel.setVisible(false);
		menuPanel.setVisible(true);
		gamePanel.setVisible(false);
		getContentPane().add(menuPanel);
		menuPanel.requestFocusInWindow();
		setTitle("Rotary");
	}

	/**
	 * Displays the solution panel.
	 */
	public void showSolnPanel()
	{
		solnPanel.setVisible(true);
		editorPanel.setVisible(false);
		menuPanel.setVisible(false);
		gamePanel.setVisible(false);
		getContentPane().add(solnPanel);
		solnPanel.requestFocusInWindow();
		solnPanel.changeTitle();
	}
}
