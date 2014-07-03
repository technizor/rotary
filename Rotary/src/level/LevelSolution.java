package level;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores the solution to a level.
 * 
 * @author Austin Tripp
 * @version June 15, 2013
 */
public class LevelSolution extends Level
{
	private static final long serialVersionUID = 10101010101010L;

	// Stores the moves for this solution.
	private LinkedList<Integer> moves;

	// Stores what the integer moves mean.
	private final static int ROTATE_LEFT = 4;
	private final static int ROTATE_RIGHT = 5;
	private final static int ACTIVATE = 6;

	/**
	 * Returns the array of moves which solves the current level in the fewest
	 * moves.
	 * 
	 * @param level the level to be solved Postcondition: the state of level is
	 *            unchanged.
	 * @return a LinkedList holding Integers which represent the moves.
	 */
	public static LinkedList<Integer> findLevelSolution(Level level)
	{

		// Keep track of which levels were already visited.
		HashSet<Level> visitedLevels = new HashSet<Level>();

		// Store the levels waiting to be processed.
		LinkedList<Level> currentLevelQueue = new LinkedList<Level>();
		LinkedList<LinkedList<Integer>> currentMoveQueue = new LinkedList<LinkedList<Integer>>();
		currentLevelQueue.add(new Level(level));
		currentMoveQueue.add(new LinkedList<Integer>());

		// Examine all the states in the queue.
		while (!currentLevelQueue.isEmpty()) {
			LinkedList<Level> nextLevelQueue = new LinkedList<Level>();
			LinkedList<LinkedList<Integer>> nextMoveQueue = new LinkedList<LinkedList<Integer>>();

			while (!currentLevelQueue.isEmpty()) {
				Level currentLevel = currentLevelQueue.remove();
				LinkedList<Integer> moves = currentMoveQueue.remove();
				boolean[][] visited = new boolean[level.getHeight()][level
						.getWidth()];

				// Check to see if an end tile can be reached from here,
				// signaling that the level is solved.
				if (floodFill(currentLevel, visited, moves, nextLevelQueue,
						nextMoveQueue, visitedLevels)) {
					// Free up some memory.
					currentLevelQueue = null;
					currentMoveQueue = null;
					nextLevelQueue = null;
					nextMoveQueue = null;

					/*
					 * The moves currently stored must be the moves that solve
					 * the level However, they might not be the absolute
					 * shortest number of non-activation/rotation moves, so a
					 * shorter version is now found.
					 */
					return getEquivalentMoveSequence(new Level(level), moves);
				}
			}

			// Reassign the queues.
			currentLevelQueue = nextLevelQueue;
			currentMoveQueue = nextMoveQueue;
		}

		// If no solution was found return null.
		return null;
	}

	/**
	 * Performs a flood fill on the current Level to find all reachable
	 * locations.
	 * 
	 * @param currentLevel the level to be examined.
	 * @param visited a list of all the tiles visited.
	 * @param moves a list of all the moves made so far.
	 * @param nextLevelQueue the queue to add all levels generated to.
	 * @param nextMoveQueue the queue to add all the moves generated to.
	 * @param visitedLevels a set containing all levels that have already been
	 *            visited.
	 * @return true if the player is currently on a FinishTile, otherwise false.
	 */
	private static boolean floodFill(Level currentLevel, boolean[][] visited,
			LinkedList<Integer> moves, LinkedList<Level> nextLevelQueue,
			LinkedList<LinkedList<Integer>> nextMoveQueue,
			HashSet<Level> visitedLevels)
	{
		// Make sure this spot was not already visited.
		Point point = currentLevel.getPlayerPosition();
		if (visited[point.y][point.x])
			return false;

		// Return true if the current level is complete.
		if (currentLevel.levelComplete())
			return true;

		// Mark this tile as visited.
		visited[point.y][point.x] = true;

		// Try to reach a FinishTile in all directions.
		if (currentLevel.canMoveUp()) {
			currentLevel.moveUp();
			moves.add(Tile.UP);
			if (floodFill(currentLevel, visited, moves, nextLevelQueue,
					nextMoveQueue, visitedLevels))
				return true;
			moves.removeLast();
			currentLevel.moveDown();
		}
		if (currentLevel.canMoveRight()) {
			currentLevel.moveRight();
			moves.add(Tile.RIGHT);
			if (floodFill(currentLevel, visited, moves, nextLevelQueue,
					nextMoveQueue, visitedLevels))
				return true;
			moves.removeLast();
			currentLevel.moveLeft();
		}
		if (currentLevel.canMoveDown()) {
			currentLevel.moveDown();
			moves.add(Tile.DOWN);
			if (floodFill(currentLevel, visited, moves, nextLevelQueue,
					nextMoveQueue, visitedLevels))
				return true;
			moves.removeLast();
			currentLevel.moveUp();
		}
		if (currentLevel.canMoveLeft()) {
			currentLevel.moveLeft();
			moves.add(Tile.LEFT);
			if (floodFill(currentLevel, visited, moves, nextLevelQueue,
					nextMoveQueue, visitedLevels))
				return true;
			moves.removeLast();
			currentLevel.moveRight();
		}

		// Change the state by activating and rotating this tile and adding it
		// to the queue.
		if (currentLevel.canActivate()) {
			Level next = new Level(currentLevel);
			next.activate();
			if (visitedLevels.add(next)) {
				nextLevelQueue.add(next);
				LinkedList<Integer> newMoves = new LinkedList<Integer>(moves);
				newMoves.add(LevelSolution.ACTIVATE);
				nextMoveQueue.add(newMoves);
			}
		}
		if (currentLevel.canRotate()) {
			Level next = new Level(currentLevel);
			next.rotateLeft();
			if (visitedLevels.add(next)) {
				nextLevelQueue.add(next);
				LinkedList<Integer> newMoves = new LinkedList<Integer>(moves);
				newMoves.add(LevelSolution.ROTATE_LEFT);
				nextMoveQueue.add(newMoves);
			}
			next = new Level(currentLevel);
			next.rotateRight();
			if (visitedLevels.add(next)) {
				nextLevelQueue.add(next);
				LinkedList<Integer> newMoves = new LinkedList<Integer>(moves);
				newMoves.add(LevelSolution.ROTATE_RIGHT);
				nextMoveQueue.add(newMoves);
			}
		}

		// Un-mark this point.
		visited[point.y][point.x] = false;

		// Return false since no FinishTile was found from here.
		return false;
	}

	/**
	 * Finds a sequence of moves equivalent to the given one in the given level.
	 * 
	 * @param level the level these moves are performed in.
	 * @param oldMoves the moves to find an equivalent of.
	 * @return a list of moves that also completes the level, but is likely
	 *         shorter than oldMoves due to no unnecessary player movement.
	 */
	private static LinkedList<Integer> getEquivalentMoveSequence(Level level,
			LinkedList<Integer> oldMoves)
	{

		LinkedList<Integer> equivalentMoves = new LinkedList<Integer>();
		while (!oldMoves.isEmpty()) {

			// Do all the rotations/activations.
			while (!oldMoves.isEmpty() && oldMoves.peek() >= ROTATE_LEFT) {
				int move = oldMoves.pop();
				switch (move) {
				case ROTATE_LEFT:
					level.rotateLeft();
					break;
				case ROTATE_RIGHT:
					level.rotateRight();
					break;
				case ACTIVATE:
					level.activate();
					break;
				}
				equivalentMoves.add(move);
			}

			// Find the player's starting position.
			Point start = level.getPlayerPosition();
			Point end = new Point(start);

			// Find out where they end up.
			while (!oldMoves.isEmpty() && oldMoves.peek() <= Tile.LEFT) {
				switch (oldMoves.pop()) {
				case Tile.UP:
					end.y--;
					break;
				case Tile.RIGHT:
					end.x++;
					break;
				case Tile.DOWN:
					end.y++;
					break;
				case Tile.LEFT:
					end.x--;
					break;
				}
			}

			// Path to this new position.
			LinkedList<Point> points = new LinkedList<Point>();
			points.add(start);
			LinkedList<LinkedList<Integer>> moves = new LinkedList<LinkedList<Integer>>();
			moves.add(new LinkedList<Integer>());
			boolean[][] visited = new boolean[level.getHeight()][level
					.getWidth()];
			Point startPos = null;
			LinkedList<Integer> currentMoves = null;
			do {
				startPos = points.remove();
				level.movePlayerTo(startPos.y, startPos.x);
				visited[startPos.y][startPos.x] = true;
				currentMoves = moves.remove();

				// Check all four directions.
				if (level.canMoveUp()) {
					level.moveUp();
					Point newPoint = level.getPlayerPosition();
					if (!visited[newPoint.y][newPoint.x]) {
						LinkedList<Integer> nextMoves = new LinkedList<Integer>(
								currentMoves);
						nextMoves.add(Tile.UP);
						points.add(newPoint);
						moves.add(nextMoves);
					}
					level.moveDown();
				}
				if (level.canMoveRight()) {
					level.moveRight();
					Point newPoint = level.getPlayerPosition();
					if (!visited[newPoint.y][newPoint.x]) {
						LinkedList<Integer> nextMoves = new LinkedList<Integer>(
								currentMoves);
						nextMoves.add(Tile.RIGHT);
						points.add(newPoint);
						moves.add(nextMoves);
					}
					level.moveLeft();
				}
				if (level.canMoveDown()) {
					level.moveDown();
					Point newPoint = level.getPlayerPosition();
					if (!visited[newPoint.y][newPoint.x]) {
						LinkedList<Integer> nextMoves = new LinkedList<Integer>(
								currentMoves);
						nextMoves.add(Tile.DOWN);
						points.add(newPoint);
						moves.add(nextMoves);
					}
					level.moveUp();
				}
				if (level.canMoveLeft()) {
					level.moveLeft();
					Point newPoint = level.getPlayerPosition();
					if (!visited[newPoint.y][newPoint.x]) {
						LinkedList<Integer> nextMoves = new LinkedList<Integer>(
								currentMoves);
						nextMoves.add(Tile.LEFT);
						points.add(newPoint);
						moves.add(nextMoves);
					}
					level.moveRight();
				}
			} while (!level.levelComplete()
					&& !level.getPlayerPosition().equals(end));

			// Empty out the moves found into the move list.
			while (!currentMoves.isEmpty())
				equivalentMoves.add(currentMoves.remove());

		}

		// Return the equivalent sequence.
		return equivalentMoves;
	}

	/**
	 * Constructs a new LevelSolution.
	 * 
	 * @param level the level that this object solves.
	 * @param solutionMoves the moves that solve this level.
	 */
	public LevelSolution(Level level, List<Integer> solutionMoves)
	{
		super(level);
		moves = new LinkedList<Integer>(solutionMoves);
	}

	/**
	 * Steps the solution forward one move.
	 */
	public void step()
	{

		// Make sure there is an element left in the solution.
		if (moves.peek() == null)
			return;
		int move = moves.pop();
		switch (move) {
		case Tile.UP:
			moveUp();
			break;
		case Tile.RIGHT:
			moveRight();
			break;
		case Tile.DOWN:
			moveDown();
			break;
		case Tile.LEFT:
			moveLeft();
			break;
		case ROTATE_LEFT:
			rotateLeft();
			break;
		case ROTATE_RIGHT:
			rotateRight();
			break;
		case ACTIVATE:
			activate();
			break;
		}
	}

	/**
	 * Returns a String representation of the next move to execute.
	 * 
	 * @return the next move to execute.
	 */
	public String nextMove()
	{
		if (moves.peek() == null)
			return "";
		int move = moves.peek();
		switch (move) {
		case Tile.UP:
			return "Move Up";
		case Tile.RIGHT:
			return "Move Right";
		case Tile.DOWN:
			return "Move Down";
		case Tile.LEFT:
			return "Move Left";
		case ROTATE_LEFT:
			return "Rotate Left";
		case ROTATE_RIGHT:
			return "Rotate Right";
		case ACTIVATE:
			return "Activate Tile";
		}
		return "";
	}

}
