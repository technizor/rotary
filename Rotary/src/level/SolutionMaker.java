package level;

import gui.SolutionPanel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * Solves a level in Rotary.
 * 
 * @author Austin Tripp
 * @version June 15th 2013
 */
public class SolutionMaker implements Runnable
{
	// Data.
	private Level[] levelPack;
	private String packName;
	private int currentLevel;
	private boolean solving;
	private boolean isPossible;

	// Containing panel.
	private final SolutionPanel holder;

	// Running thread.
	private Thread solver;

	/**
	 * Constructs a new SolutionMaker.
	 * 
	 * @param holder the SolutionPanel to notify when solved.
	 */
	public SolutionMaker(SolutionPanel holder)
	{
		levelPack = null;
		currentLevel = -1;
		isPossible = true;
		solving = false;
		solver = null;
		this.holder = holder;
	}

	/**
	 * Sets the level pack to solve to the one specified.
	 * 
	 * @param packName the pack to generate solutions for.
	 * @throws FileNotFoundException if the file is not found.
	 * @throws IOException if IO does not work.
	 * @throws ClassNotFoundException if the required classes are not found.
	 */
	public void setLevelPack(String packName) throws FileNotFoundException,
			IOException, ClassNotFoundException
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
				"data\\levels\\" + packName + ".pck"));
		levelPack = (Level[]) in.readObject();
		this.packName = packName;
		in.close();
	}

	/**
	 * Solves the given level Precondition: a level pack has been set and the
	 * index given is valid for that level pack.
	 * 
	 * @param level index to be solved.
	 */
	public void solve(int level)
	{
		solving = true;
		currentLevel = level;
		solver = new Thread(this);
		solver.start();
	}

	/**
	 * Ends the solving process prematurely.
	 */
	public void terminateSolving()
	{
		if (solver != null)
			solver.interrupt();
	}

	/**
	 * Checks to see whether this solver is currently solving a level.
	 * 
	 * @return true if it is currently solving a level, otherwise false.
	 */
	public boolean isSolving()
	{
		return solving;
	}

	/**
	 * Checks to see whether the given level is solvable Precondition: a level
	 * has been given and solved already.
	 * 
	 * @return true if the level can be solved, otherwise false.
	 */
	public boolean isPossible()
	{
		return isPossible;
	}

	/**
	 * Solves the current level.
	 */
	public void run()
	{
		LinkedList<Integer> solution = LevelSolution
				.findLevelSolution(levelPack[currentLevel]);
		solving = false;
		isPossible = solution != null;
		if (isPossible)
			writeSolution(solution);
		notifyCompletion();

	}

	/**
	 * Writes the solution to the current level to a file.
	 * 
	 * @param levelSolution the list of moves that solve the current level.
	 */
	private void writeSolution(LinkedList<Integer> levelSolution)
	{

		// Open the existing solutions.
		LevelSolution[] solutions = null;
		try {

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"data\\solutions\\" + packName + ".soln"));
			solutions = (LevelSolution[]) in.readObject();
			in.close();
		} catch (Exception e) {
			solutions = new LevelSolution[levelPack.length];
		}

		// Insert this solution into the existing ones.
		solutions[currentLevel] = new LevelSolution(levelPack[currentLevel],
				levelSolution);

		// Write the whole pack to a file.
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("data\\solutions\\" + packName
							+ ".soln"));
			out.writeObject(solutions);
			out.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Notifies the SolutionPanel that the solution is ready.
	 */
	private void notifyCompletion()
	{
		holder.generationComplete();
	}

}
