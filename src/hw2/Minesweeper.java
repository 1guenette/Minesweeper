package hw2;

import java.util.ArrayList;
import java.util.Random;
import api.Cell;
import api.CellObserver;
import api.Status;
import api.Mark;

/**
 * @author Samuel Guenette
 * @date 3/30/2016 This class encapsulates the state of a minesweeper game.
 */
public class Minesweeper {
	/**
	 * Number rows in the minesweeper Game
	 */
	private int rowSum;
	/**
	 * Number of columns in the minesweeper game
	 */
	private int colSum;
	/**
	 * Number of mines in the minesweeper game
	 */
	private int mineNum;
	/**
	 * Random number generator for placing mines in the minesweeper game
	 */
	private Random ran;
	/**
	 * Number of clicks in the minesweeper game
	 */
	private int clickNum;
	/**
	 * Number of flags implemented in the minesweeper game
	 */
	private int flagNum;
	/**
	 * Boolean value telling whether the minesweeper game is over
	 */
	private boolean over;
	/**
	 * ArrayList keeping track of the history of moves made in the minesweeper
	 * game
	 */
	private ArrayList<Cell> cellHist;
	/**
	 * 2D array representing the map of the minesweeper game
	 */
	private Cell[][] grid;

	/**
	 * Constructs an instance of the game using the given array of strings to
	 * initialize the mine locations. The jth character of the ith string
	 * corresponds to row i, column j of the Cell array. A
	 * <code>GameUtil.MINE_CHAR</code> character means the corresponding cell
	 * will be set as a mine. All strings in the given array must have the same
	 * length. Initially all cells have status HIDDEN and the counts are
	 * correct.
	 * 
	 * @param descriptor
	 *            array of strings representing mine positions
	 */
	public Minesweeper(String[] descriptor) {
		grid = GridUtil.createFromStringArray(descriptor);
		GridUtil.initCounts(grid);
		rowSum = grid.length;
		colSum = grid[0].length;
		mineNum = GridUtil.countAllMines(grid);
		clickNum = 0;
		flagNum = 0;
		over = false;
		cellHist = new ArrayList<Cell>();

	}

	/**
	 * Constructs an instance of the game of the specified size and specified
	 * initial number of mines, using the given <code>Random</code> object to
	 * select the mine locations. The selection is performed in such a way that
	 * each cell is equally likely to be selected as one of the mines. Initially
	 * all cells have status HIDDEN and the counts are correct.
	 * 
	 * @param rows
	 *            number of rows in the grid
	 * @param columns
	 *            number of columns in the grid
	 * @param numberOfMines
	 *            number of mines in the grid
	 * @param givenRandom
	 *            random number generator to use for placing mines and
	 *            <code>randomMove</code>
	 */

	public Minesweeper(int rows, int columns, int numberOfMines, Random givenRandom) {
		rowSum = rows;
		colSum = columns;
		mineNum = numberOfMines;
		ran = givenRandom;
		clickNum = 0;
		flagNum = 0;
		grid = new Cell[rowSum][colSum];
		over = false;
		cellHist = new ArrayList<Cell>();

		int x;
		int y;

		for(int i = 0; i<grid.length;i++)
		{
			for(int j = 0; j<grid[0].length; j++)
			{
				grid[i][j] = new Cell(i,j);			//initializes every cell in the grid
			}
			
		}
		for (int i = 0; i < mineNum; i++) 			//places mines in in the minesweeper game randomly  
		{
			x = ran.nextInt(rowSum);
			y = ran.nextInt(colSum);
			if (grid[x][y].isMine()) 
			{
				i--;					//adds an extra iteration to loop if there is already a mine placed at the given location		
			} 
			else 
			{
				grid[x][y].setIsMine(true);
			}

		}
		GridUtil.initCounts(grid);
	}

	/**
	 * Returns the number of clicks for revealing a cell that have been made.
	 * Note that this number may be smaller than the number of revealed cells.
	 * An operation to toggle the mark of a cell does not count as a click.
	 * 
	 * @return number of clicks
	 */
	public int getClicks() {
		return clickNum;
	}

	/**
	 * Returns the total number of mines in the grid.
	 * 
	 * @return number of mines
	 * 
	 */
	public int getNumMines() {
		return mineNum;
	}

	/**
	 * Returns the total number of cells with mark value FLAG.
	 * 
	 * @return number of flagged cells
	 */
	public int getNumFlags() {
		flagNum = GridUtil.countAllFlags(grid);
		return flagNum;
	}

	/**
	 * Returns the number of rows in the grid.
	 * 
	 * @return number of rows in the grid
	 */
	public int getRows() {
		return rowSum;
	}

	/**
	 * Returns the number of columns in the grid.
	 * 
	 * @return number of columns in the grid
	 */
	public int getColumns() {
		return colSum;
	}

	/**
	 * Returns the cell at the specified position.
	 * <p>
	 * NOTE: The caller of this method should normally not modify the returned
	 * cell.
	 * 
	 * @param row
	 *            given position row
	 * @param col
	 *            given position column
	 * @return cell at the given position
	 */
	public Cell getCell(int row, int col) {
		Cell selected = grid[row][col];
		return selected;
	}

	/**
	 * Returns this game's grid as an array of strings, according to the
	 * conventions described in <code>GridUtil.convertToStringArray</code>.
	 * 
	 * @param revealAll
	 *            true if hidden cell values should be shown
	 * @return array of strings representing this game's grid
	 */

	public String[] getGridAsStringArray(boolean revealAll) 
	{
		String[] descriptor = new String[rowSum];			

		for (int i = 0; i < grid.length; i++) 		//Loops through grid and places value of each cell in an string array
		{
			for (int j = 0; j < grid[0].length; j++) 
			{
				if (grid[i][j].isMine() == true) 
				{
					descriptor[i] += "x";
				} 
				else 
				{
					descriptor[i] += " ";
				}
			}
		}
		return null;
	}

	/**
	 * Returns a reference to the list of all revealed cells, in the order they
	 * were revealed.
	 * <p>
	 * NOTE: The caller of this method should normally not modify the returned
	 * list or the cells it contains.
	 * 
	 * @return list of all revealed cells
	 */
	public ArrayList<Cell> getHistory() 
	{
		return cellHist;
	}

	/**
	 * Returns true if the game is over, false otherwise. The game is over if
	 * the player attempts to reveal a mine, or if all non-mine cells are
	 * revealed.
	 * 
	 * @return true if the game is over, false otherwise
	 */
	public boolean isOver() 
	{
		return over;
	}

	/**
	 * Toggle the mark value on the cell at the given position. The values
	 * should cycle through <code>Mark.NONE</code>, <code>Mark.FLAG</code>, and
	 * <code>Mark.QUESTION_MARK</code>, in that order.
	 * 
	 * @param row
	 *            given position row
	 * @param col
	 *            given position column
	 */
	public void toggleMark(int row, int col) 
	{
		if (grid[row][col].getMark() == Mark.NONE) 			//Changes mark on a cell on the grid based on its previous mark
		{
			grid[row][col].setMark(Mark.FLAG);
		} else if (grid[row][col].getMark() == Mark.FLAG) 
		{
			grid[row][col].setMark(Mark.QUESTION_MARK);
		} else 
		{
			grid[row][col].setMark(Mark.NONE);
		}
	}

	/**
	 * Processes a selection by the player to reveal the cell at the given
	 * position. In general revealing a mine should end the game, and revealing
	 * a cell with count zero should initiate a call to
	 * <code>GridUtil.clearRegion</code>. However, a special case is made for
	 * the first selection: if the player selects a mine as the first move, the
	 * cell is first converted to a non-mine and the count is adjusted, and then
	 * the selection is processed normally. This method does nothing if the game
	 * is over.
	 * 
	 * @param row
	 *            given position row
	 * @param col
	 *            given position column
	 */
	public void play(int row, int col) 
	{
		if (over == false) 
		{
			grid[row][col].setStatus(Status.REVEALED);

			if (grid[row][col].getCount() == 0) 
			{
				GridUtil.clearRegion(grid, row, col, cellHist);		//Clears region if player clicks on a cell with 0-value
			}

			if (grid[row][col].isMine() && clickNum == 0) 
			{
				grid[row][col].setIsMine(false);
				GridUtil.initCounts(grid);						//Accounts for clicking on a mine for the first click
			} 
			else if (grid[row][col].isMine()) 
			{
				GridUtil.revealAllMines(grid);	//Accounts for clicking on a mine
				over = true;
			}
			if (isWon() == true) 
			{
				GridUtil.flagAllMines(grid);	//Accounts for last and winning click 
				over = true;
			}

			cellHist.add(grid[row][col]);
			clickNum++;
		}
	}

	/**
	 * Returns whether or not the game has been won.
	 * 
	 * @return true if the game is won, false otherwise
	 */
	public boolean isWon() 
	{
		
		boolean won = true;
		for (int i = 0; i < rowSum; i++) 
		{
			for (int j = 0; j < colSum; j++) 
			{
				if (grid[i][j].getStatus() == Status.HIDDEN && grid[i][j].isMine() == false) 
				{
					won = false;
				}
			}
		}
		return won;
	}

	/**
	 * Iterates over the history of revealed cells, in reverse order, to find a
	 * neighboring cell that is still hidden and has count greater than zero,
	 * and then reveals the first such cell found. If the history is empty or no
	 * such cell exists, this method does nothing. (The latter can only occur if
	 * all remaining hidden non-mine cells have count zero.)
	 */
	public void hint() {
		
		while(over == false)
		{			
			for (int i = cellHist.size() - 1; i >= 0; i--) {
				Cell hidden = GridUtil.findOneHiddenNeighbor(grid, cellHist.get(i).getRow(), cellHist.get(i).getCol());  //Gets cell value from history arraylist to find nearby cell
				if (hidden != null && hidden.isMine() != true && hidden.getCount() > 0) 
				{
					grid[hidden.getRow()][hidden.getCol()].setStatus(Status.REVEALED);
					cellHist.add(hidden);
					return;
				}
			}
		}	
	}

	/**
	 * Calls setObserver with the given <code>CellObserver</code> on every cell
	 * of the grid.
	 * 
	 * @param observer
	 *            reference to a <code>CellObserver</code>
	 */
	public void setObserver(CellObserver observer) {
		for (int i = 0; i < rowSum; i++) 
		{
			for (int j = 0; j < colSum; j++) 
			{
				grid[i][j].setObserver(observer);
			}
		}

	}
}
