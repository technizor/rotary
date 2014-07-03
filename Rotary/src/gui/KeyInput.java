package gui;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles the loading and setting of the keyboard preferences.
 * 
 * @author Sherman Ying
 * @version June 15, 2013
 */
public class KeyInput
{
	// Default key values.
	public static int MOVE_UP_KEY = KeyEvent.VK_UP;
	public static int MOVE_RIGHT_KEY = KeyEvent.VK_RIGHT;
	public static int MOVE_DOWN_KEY = KeyEvent.VK_DOWN;
	public static int MOVE_LEFT_KEY = KeyEvent.VK_LEFT;
	public static int ROTATE_LEFT_KEY = KeyEvent.VK_Q;
	public static int ROTATE_RIGHT_KEY = KeyEvent.VK_E;
	public static int ACTIVATE_KEY = KeyEvent.VK_SPACE;

	// Key tags used within the saved configuration file for viewing purposes.
	private static final String[] TAGS = { "U", "R", "D", "L", "RL", "RR", "A" };

	/**
	 * Gives the key value of the specified key command.
	 * 
	 * @param number the key command to give the key code of.
	 * @return the key code of the specified key command, or -1 if a valid key
	 *         was not given.
	 */
	public static int getKey(int number)
	{
		if (number == 0)
			return MOVE_UP_KEY;
		if (number == 1)
			return MOVE_RIGHT_KEY;
		if (number == 2)
			return MOVE_DOWN_KEY;
		if (number == 3)
			return MOVE_LEFT_KEY;
		if (number == 4)
			return ROTATE_LEFT_KEY;
		if (number == 5)
			return ROTATE_RIGHT_KEY;
		if (number == 6)
			return ACTIVATE_KEY;
		return -1;
	}

	/**
	 * Determines whether the key input is a valid key for input use.
	 * 
	 * @param newKey the key input to test.
	 * @return true if the key is any of the following keys:
	 *         `1234567890-=qwertyuiop[]\asdfghjkl;'zxcvbnm,./
	 *         [SPACE][UP][LEFT][DOWN][RIGHT], and false if it is none of these.
	 */
	private static boolean isValidKey(int newKey)
	{
		if (newKey == KeyEvent.VK_UP || newKey == KeyEvent.VK_RIGHT
				|| newKey == KeyEvent.VK_DOWN || newKey == KeyEvent.VK_LEFT)
			return true;
		if (newKey >= KeyEvent.VK_A && newKey <= KeyEvent.VK_Z)
			return true;
		if (newKey >= KeyEvent.VK_0 && newKey <= KeyEvent.VK_9)
			return true;
		if (newKey == KeyEvent.VK_BRACELEFT || newKey == KeyEvent.VK_BRACERIGHT
				|| newKey == KeyEvent.VK_MINUS || newKey == KeyEvent.VK_EQUALS
				|| newKey == KeyEvent.VK_SEMICOLON
				|| newKey == KeyEvent.VK_LESS || newKey == KeyEvent.VK_GREATER
				|| newKey == KeyEvent.VK_COMMA || newKey == KeyEvent.VK_PERIOD
				|| newKey == KeyEvent.VK_BACK_SLASH
				|| newKey == KeyEvent.VK_QUOTE || newKey == KeyEvent.VK_SPACE
				|| newKey == KeyEvent.VK_BACK_QUOTE)
			return true;
		return false;
	}

	/**
	 * Reverts the keys to the default keys.
	 */
	public static void loadDefaultKeyPreferences()
	{
		MOVE_UP_KEY = KeyEvent.VK_UP;
		MOVE_RIGHT_KEY = KeyEvent.VK_RIGHT;
		MOVE_DOWN_KEY = KeyEvent.VK_DOWN;
		MOVE_LEFT_KEY = KeyEvent.VK_LEFT;
		ROTATE_LEFT_KEY = KeyEvent.VK_Q;
		ROTATE_RIGHT_KEY = KeyEvent.VK_E;
		ACTIVATE_KEY = KeyEvent.VK_SPACE;
	}

	/**
	 * Loads the key preferences from the file.
	 */
	public static void loadKeyPreferences()
	{
		File file = new File("data\\input.ini");
		if (file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				MOVE_UP_KEY = Integer.parseInt(reader.readLine().split(":")[1]);
				MOVE_RIGHT_KEY = Integer
						.parseInt(reader.readLine().split(":")[1]);
				MOVE_DOWN_KEY = Integer
						.parseInt(reader.readLine().split(":")[1]);
				MOVE_LEFT_KEY = Integer
						.parseInt(reader.readLine().split(":")[1]);
				ROTATE_LEFT_KEY = Integer
						.parseInt(reader.readLine().split(":")[1]);
				ROTATE_RIGHT_KEY = Integer.parseInt(reader.readLine()
						.split(":")[1]);
				ACTIVATE_KEY = Integer
						.parseInt(reader.readLine().split(":")[1]);
				reader.close();
			} catch (Exception e) {
				loadDefaultKeyPreferences();
				saveKeyPreferences();
			}
		} else {
			loadDefaultKeyPreferences();
			saveKeyPreferences();
		}

	}

	/**
	 * Modifies a specific control key if the key is valid.
	 * 
	 * @param keyNumber the control key to modify.
	 * @param newKey the key to set the control key to.
	 */
	public static void modifyKey(int keyNumber, int newKey)
	{
		if (!isValidKey(newKey))
			return;
		if (keyNumber == 0) {
			MOVE_UP_KEY = newKey;
		} else if (keyNumber == 1) {
			MOVE_RIGHT_KEY = newKey;
		} else if (keyNumber == 2) {
			MOVE_DOWN_KEY = newKey;
		} else if (keyNumber == 3) {
			MOVE_LEFT_KEY = newKey;
		} else if (keyNumber == 4) {
			ROTATE_LEFT_KEY = newKey;
		} else if (keyNumber == 5) {
			ROTATE_RIGHT_KEY = newKey;
		} else if (keyNumber == 6) {
			ACTIVATE_KEY = newKey;
		}
		if (keyNumber >= 0 && keyNumber <= 6)
			saveKeyPreferences();
	}

	/**
	 * Saves the key preferences to a file.
	 * 
	 * @return true if the save operation was successful, and false it it
	 *         failed.
	 */
	public static boolean saveKeyPreferences()
	{
		try {
			File file = new File("data\\input.ini");
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(TAGS[0] + ":" + MOVE_UP_KEY);
			writer.println(TAGS[1] + ":" + MOVE_RIGHT_KEY);
			writer.println(TAGS[2] + ":" + MOVE_DOWN_KEY);
			writer.println(TAGS[3] + ":" + MOVE_LEFT_KEY);
			writer.println(TAGS[4] + ":" + ROTATE_LEFT_KEY);
			writer.println(TAGS[5] + ":" + ROTATE_RIGHT_KEY);
			writer.println(TAGS[6] + ":" + ACTIVATE_KEY);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}
}
