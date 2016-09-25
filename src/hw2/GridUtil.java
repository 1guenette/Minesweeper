package hw2;

import java.util.ArrayList;

import api.Cell;
import api.Mark;
import api.Status;

/**
 * * @author Samuel Guenette
 * 
 * @date 3/30/2016 Bowling game with a number of frames specified by the user.
 *       Utility class for an implementation of Minesweeper. This class contains
 *       methods for examining and updating a 2D array of Cell objects.
 */
public class GridUtil 
{
	/**
	 * Marker character for initializing a 2D array of cells from an array of
	 * Strings.
	 */
	public static final char MINE_CHAR = 'x';

	/**
	 * Creates a 2D array of Cells from an array of strings, where each string
	 * corresponds to one row of the returned array. The jth character of the
	 * ith string corresponds to row i, column j of the Cell array. A MINE_CHAR
	 * character means the corresponding cell will be set as a mine. All strings
	 * in the given array must have the same length. This method does not
	 * calculate the counts for the cells. Initially all cells have status
	 * HIDDEN, count 0, and mark value NONE.
	 * 
	 * @param descriptor
	 *            array of strings from which to construct the Cell array
	 * @return 2D array of Cells
	 */
	public static Cell[][] createFromStringArray(String[] descriptor) 
	{
		int width = descriptor[0].length();
		int height = descriptor.length;
		Cell[][] grid = new Cell[height][width];
		for (int row = 0; row < height; row += 1) 
		{
			for (int col = 0; col < width; col += 1) 
			{
				grid[row][col] = new Cell(row, col);
				if (descriptor[row].charAt(col) == MINE_CHAR) 
				{
					grid[row][col].setIsMine(true);
				}
			}
		}
		return grid;
	}

	/**
	 * Converts a grid into an array of strings. Each row of the grid
	 * corresponds to one string. There is one character for each cell. If
	 * <code>revealAll</code> is false, then all HIDDEN cells are are displayed
	 * as '-', 'f', or '?' depending on whether the mark value is NONE, FLAG, or
	 * QUESTION_MARK. Revealed mines are displayed with MINE_CHAR, and other
	 * revealed cells are displayed as the count.
	 * <p>
	 * if <code>revealAll</code> is true, then all mines are displayed as
	 * MINE_CHAR and all other non-mines are displayed as the count.
	 *
	 * @param grid
	 *            2D array of Cells
	 * @param revealAll
	 *            true if hidden values should be shown, false otherwise
	 * @return array of strings representing the grid
	 */
	public static String[] convertToStringArray(Cell[][] grid, boolean revealAll) 
	{
		String[] ret = new String[grid.length];
		for (int row = 0; row < grid.length; row += 1) 
		{
			String current = "";
			for (int col = 0; col < grid[0].length; col += 1) 
			{
				Cell c = grid[row][col];
				if (c.getStatus() == Status.HIDDEN && !revealAll) 
				{
					if (c.getMark() == Mark.FLAG) 
					{
						current += "f";
					} 
					else if (c.getMark() == Mark.QUESTION_MARK) 
					{
						current += "?";
					}
					else 
					{
						current += "-";
					}
				} 
				else 
				{
					if (c.isMine()) 
					{
						current += MINE_CHAR;
					} else 
					{
						current += "" + c.getCount();
					}
				}
			}
			ret[row] = current;
		}
		return ret;
	}

	/**
	 * Initialize the count value for each cell in the given 2D array. The count
	 * for a non-mine is the number of neighboring cells (left, right, top,
	 * bottom, and diagonal) that are mines. The count for a mine is -1.
	 * 
	 * @param grid
	 *            2D array of Cells
	 */
	public static void initCounts(Cell[][] grid) 
	{
		int val = 0;

		for (int i = 0; i < grid.length; i++) 		//Loops through grid 
		{
			for (int j = 0; j < grid[0].length; j++) 
			{

				if (grid[i][j].isMine() == true) 
				{
					val = -1;
				} 
				else 
				{
					val = countNeighboringMines(grid, i, j);
				}

				grid[i][j].setCount(val);
			}
		}

	}

	/**
	 * Counts the number of neighbors of the given position (left, right, top,
	 * bottom, and diagonal) that are mines.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @param givenRow
	 *            given position row
	 * @param givenCol
	 *            given position column
	 * @return number of neighbors that are mines
	 */
	public static int countNeighboringMines(Cell[][] grid, int givenRow, int givenCol) {
		int num = 0;//Sum of neighboring mines
		int upper = Math.max(0, givenRow - 1);
		int lower = Math.min(grid.length - 1, givenRow + 1);		//Accounts for boundary locations on grid
		int left = Math.max(0, givenCol - 1);
		int right = Math.min(grid[0].length - 1, givenCol + 1);

		//iterates over all cells except for the center one
		for (int row = upper; row <= lower; row += 1) 
		{
			for (int col = left; col <= right; col += 1) 
			{
				if (!(row == givenRow && col == givenCol)) 
				{
					Cell d = grid[row][col];

					if (d.isMine() == true) 
					{
						num++;
					}

				}
			}
		}

		return num;

	}

	/**
	 * Determines whether all the non-mine cells have status REVEALED.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @return true if all non-mine cells are revealed, false otherwise
	 */
	public static boolean areAllCellsRevealed(Cell[][] grid) 
	{
		boolean result = true;
		for (int i = 0; i < grid.length; i++) //Loops through grid to find if cells are both revealed and not mines
		{
			for (int j = 0; j < grid[0].length; j++) 
			{
				if (grid[i][j].getStatus() == Status.HIDDEN && grid[i][j].isMine() == false) 
				{
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Sets all mine cells to have status REVEALED. Other cells are not
	 * modified.
	 * 
	 * @param grid
	 *            2D array of Cells
	 */
	public static void revealAllMines(Cell[][] grid) 
	{
		for (int row = 0; row < grid.length; row++) //Loops through grid and reveals cells with mines
		{
			for (int col = 0; col < grid[0].length; col++) 
			{
				if (grid[row][col].isMine() == true) 
				{
					grid[row][col].setStatus(Status.REVEALED);
				}
			}
		}
	}

	/**
	 * Returns the total number of mines in the given array.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @return number of mines in the array
	 */
	public static int countAllMines(Cell[][] grid) {
		int count = 0;
		for (int row = 0; row < grid.length; row++) //Loops through grid to find mines
		{
			for (int col = 0; col < grid[0].length; col++) 
			{
				if (grid[row][col].isMine() == true) 
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the total number of cells marked as FLAG in the given array.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @return number of flagged cells
	 */
	public static int countAllFlags(Cell[][] grid) {
		int sum = 0;
		for (int row = 0; row < grid.length; row++) 	//Loops through grid to find cells with flag mark
		{
			for (int col = 0; col < grid[0].length; col++) 
			{
				if (grid[row][col].getMark() == Mark.FLAG) 
				{
					sum++;
				}
			}
		}
		return sum;
	}

	/**
	 * Returns one neighbor (left, right, top, bottom, diagonal) of the given
	 * position that is hidden and has count greater than zero or null if there
	 * is no such cell.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @param givenRow
	 *            given position row
	 * @param givenCol
	 *            given position column
	 * @return a hidden, non-mine cell that neighbors the given position
	 */
	public static Cell findOneHiddenNeighbor(Cell[][] grid, int givenRow, int givenCol) 
	{
		int upper = Math.max(0, givenRow - 1);
		int lower = Math.min(grid.length - 1, givenRow + 1);
		int left = Math.max(0, givenCol - 1);					//Accounts for locations of cells if on the boundary of grid
		int right = Math.min(grid[0].length - 1, givenCol + 1);
		Cell d = null;
		// now we can iterate over all cells except for the center one
		for (int row = upper; row <= lower; row += 1) 
		{
			for (int col = left; col <= right; col += 1) 
			{
				if (!(row == givenRow && col == givenCol)) 
				{
					d = grid[row][col];
					if (d.isMine() == false && d.getStatus() == Status.HIDDEN && d.getCount() > 0) 
					{
						return d;
					}

				}
			}
		}

		return d;
	}

	/**
	 * Sets all mine cells to have mark value <code>Mark.FLAG</code>.
	 * 
	 * @param grid
	 *            2D array of Cells
	 */
	public static void flagAllMines(Cell[][] grid) 
	{
		for (int row = 0; row < grid.length; row++) 
		{
			for (int col = 0; col < grid[0].length; col++) 
			{
				if (grid[row][col].isMine() == true) 
				{
					grid[row][col].setMark(Mark.FLAG);
				}
			}
		}

	}

	/**
	 * Reveals all neighbors of the Cell at the given position that are not
	 * mines and have count greater than zero. If the given
	 * <code>ArrayList</code> is non-null, all revealed cells are added to the
	 * list, in the order in which they are revealed.
	 * 
	 * @param grid
	 *            2D array of Cells
	 * @param givenRow
	 *            given position row
	 * @param givenCol
	 *            given position column
	 * @param history
	 *            list to which revealed cells are added
	 */
	public static void revealNeighbors(Cell[][] grid, int givenRow, int givenCol, ArrayList<Cell> history) {
		// find the boundary around the given cell; this will normally be a 3x3
		// region, but we may be against one or both of the borders
		int upper = Math.max(0, givenRow - 1);
		int lower = Math.min(grid.length - 1, givenRow + 1);		
		int left = Math.max(0, givenCol - 1);
		int right = Math.min(grid[0].length - 1, givenCol + 1);

		// now we can iterate over all cells except for the center one
		for (int row = upper; row <= lower; row += 1) 
		{
			for (int col = left; col <= right; col += 1) 
			{
				if (!(row == givenRow && col == givenCol)) 
				{
					Cell d = grid[row][col];
					if (d.getCount() > 0) 
					{
						d.setStatus(Status.REVEALED);
						if (history != null) 
						{
							history.add(d);
						}
					}
				}
			}
		}
	}

	/**
	 * Performs a "flood fill" algorithm to reveal a region of all reachable
	 * cells with count zero, plus the cells at the boundary of the region,
	 * starting at the given position. If the cell at the given position does
	 * not have count 0, this method does nothing. If the given
	 * <code>ArrayList</code> is non-null, all revealed cells are added to the
	 * list, in the order in which they are revealed.
	 * 
	 * @param grid
	 *            2D
	 * @param row
	 *            initial cell row
	 * @param col
	 *            initial cell column
	 * @param history
	 *            list to which revealed cells are added
	 * 
	 */
	public static void clearRegion(Cell[][] grid, int row, int col, ArrayList<Cell> history) {
		int upper = Math.max(0, row - 1);
		int lower = Math.min(grid.length - 1, row + 1);
		int left = Math.max(0, col - 1);						//Accounts for corner cells
		int right = Math.min(grid[0].length - 1, col + 1);

		Cell c = grid[row][col];
		c.setStatus(Status.SEEN);
		c.setStatus(Status.EXPLORE_UP);

		if (grid[upper][col].getStatus() == Status.HIDDEN && grid[upper][col].getCount() == 0) //Searches through nearby 0s with recursion
		{
			clearRegion(grid, upper, col, history);
		}
		c.setStatus(Status.EXPLORE_LEFT);

		if (grid[row][left].getStatus() == Status.HIDDEN && grid[row][left].getCount() == 0) 
		{
			clearRegion(grid, row, left, history);
		}
		c.setStatus(Status.EXPLORE_DOWN);

		if (grid[lower][col].getStatus() == Status.HIDDEN && grid[lower][col].getCount() == 0) 
		{
			clearRegion(grid, lower, col, history);
		}
		c.setStatus(Status.EXPLORE_RIGHT);

		if (grid[row][right].getStatus() == Status.HIDDEN && grid[row][right].getCount() == 0) 
		{
			clearRegion(grid, row, right, history);
		}
		c.setStatus(Status.REVEALED);
		revealNeighbors(grid, row, col, history);

	}

}
