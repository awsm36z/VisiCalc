
/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

/**
 * Extra Credit:
 * 1. Order of Operations for formulas |+5
 * 2. Modulus operator for formulas	   |+2
 * 3. Exponents operator for formulas  |+2
 * 4. Rectangular Sum				   |+2
 * 5. Rectangular Average			   |+2
 * 6. Rectangular Sort				   |+3
 * 7. Sort mixed types.				   |+2
 * 8.
 * -------------------------------------------------
 * Total:------------------------------|+18
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class VisiCalc {

	static String cmd = "";

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scOne = new Scanner(System.in);
		Cell[][]  cellSheet = new Cell[10][7];

		// Populates the  cellSheet with empty cells.
		for (int i = 0; i <  cellSheet.length; i++) {
			for (int j = 0; j <  cellSheet[i].length; j++) {
				 cellSheet[i][j] = new Cell(i, j);
			}
		}

		// Creates grid and populates it with the already set  cellSheet.
		Grid spreadsheet = new Grid( cellSheet);
		spreadsheet.print( cellSheet);

		//
		boolean quit = false;

		// input loop
		while (!quit) {
			System.out.println("ENTER:");
			String input = scOne.nextLine();
			Scanner sc = new Scanner(input);

			// The first token will either be the command or the cell number
			// we will name the variable: command for simplicity, assuming cell numbers
			// are an implicit command to move to that cell.
			String command = sc.next();
			quit = processCommand(command, sc,  cellSheet, scOne);
		}
	}

	/**
	 * the following method will take the input and evaulate if it is a quit, print,
	 * call, or assignment command.
	 * 
	 * Print: prints grid
	 * 
	 * Quit: quit the program
	 * 
	 * Call: display value of the cell
	 * 
	 * Assignment: assign a cell a value.
	 * 
	 * SORTA and SORTD: sorts the arrays given upward or downward depending on
	 * 
	 * @param scOne
	 * @param cmd
	 * @param sc
	 * @param spreadsheet
	 * @param command
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	private static boolean processCommand(String command, Scanner sc, Cell[][]  cellSheet, Scanner scOne)
			throws FileNotFoundException {

		if (isQuit(command)) {
			return quit();
		}

		else if (isPrint(command)) {
			printGrid(command, cellSheet);
		}
		// Load Function code
		else if (isLoad(command)) {

			// takes in file to read from user.
			loadFileCommands(sc, cellSheet, scOne);

		} else if (isClearMethod(command)) {

			clearCellOrGrid(command, sc, cellSheet);

		} else if (isACellCoordinate(command)) {

			int x = findX(command);
			int y = findY(command);
			cmd = saveCommand(command, cmd);

			// check if the user wants to simply see cell content or if attempting
			// to create new cell and populate it.
			if (sc.hasNext()) {
				String nextToken = sc.next();
				// after cell coordinates, the next token has to be an equal sign
				// if not, then we will warn the user and request a new input.
				if (inputNotContainEqual(nextToken)) {
					System.out.println("invalid token. Expected = sign after cell number");
					return false;
				}

				assignCellToGrid(sc, cellSheet, x, y, nextToken);
			}

			/*
			 * Command to just call and find a cell
			 */
			else {

				// Command will evaluate the formula everytime you call it.
				if (isFormulaCell(cellSheet, x, y)) {

					printFormulaCell(cellSheet, x, y);

				}
				// otherwise, just print the literal value.
				else {

					printOtherCells(cellSheet, x, y);

				}
			}
		}

		else if (isHelp(command)) {

			printHelpText();
		}

		/*
		 * save command: method will either create or load a file that is passed through
		 * as a string. Then it will rewrite the file with the commands called in the
		 * program.
		 */
		else if (isSave(command)) {
			saveFile(sc);
		}

		/*
		 * this is the SORTA AND SORTD method.--------------------
		 */
		else if (isSort(command)) {
			sortGridRange(command, sc, cellSheet);
		}

		else {
			System.out.println("invalid input, pleas try again, or type \"help\" to see the possiblilities.");
		}
		return false;
	}

	private static void assignCellToGrid(Scanner sc, Cell[][] cellSheet, int x, int y, String nextToken) {
		cmd = saveCommand(nextToken, cmd);
		nextToken = sc.nextLine();
		cmd = saveCommand(nextToken, cmd) + "\n";

		cellSheet[y][x] = getCellContent(x, y, nextToken);
	}

	private static boolean isFormulaCell(Cell[][] cellSheet, int x, int y) {
		return cellSheet[y][x] instanceof FormulaCell;
	}

	private static void printFormulaCell(Cell[][] cellSheet, int x, int y) {
		System.out.println(((FormulaCell) cellSheet[y][x]).getValue(cellSheet));
	}

	private static void printOtherCells(Cell[][] cellSheet, int x, int y) {
		System.out.println(cellSheet[y][x].getValue());
	}

	private static boolean quit() {
		return true;
	}

	private static void loadFileCommands(Scanner sc, Cell[][] cellSheet, Scanner scOne) throws FileNotFoundException {
		String fileToRead = sc.next();
		Scanner commandReader = new Scanner(new File(fileToRead));
		while (commandReader.hasNext()) {
			String testInput = commandReader.next();
			processCommand(testInput, commandReader, cellSheet, scOne);
		}
	}

	private static boolean isClearMethod(String command) {
		return command.equalsIgnoreCase("clear");
	}

	private static void clearCellOrGrid(String command, Scanner sc, Cell[][] cellSheet) {
		cmd = saveCommand(command, cmd);
		command = sc.next();
		cmd = saveCommand(command, cmd);

		if (sc.hasNext()) {
			clearCell(command, cellSheet);
		} else {
			clearGrid(command);
		}
	}

	private static void clearCell(String command, Cell[][] cellSheet) {
		int x = findX(command);
		int y = findY(command);
		cellSheet[y][x] = new Cell(x, y);
	}

	private static void clearGrid(String command) {
		Cell[][] cellSheet;
		cmd = saveCommand(command, cmd);
		cellSheet = new Cell[10][7];
		for (int i = 0; i < cellSheet.length; i++) {
			for (int j = 0; j < cellSheet[i].length; j++) {
				cellSheet[i][j] = new Cell(i, j);
			}
		}
	}

	private static void printHelpText() {
		System.out.println(
				"type PRINT to print the grid\n type HELP to see the menu\n type LOAD to load the file commands\n type QUIT to quit the program");
	}

	private static boolean isSort(String command) {
		return command.equalsIgnoreCase("sorta") || command.equalsIgnoreCase("sortd");
	}

	private static void sortGridRange(String command, Scanner sc, Cell[][] cellSheet) {
		String range = sc.nextLine();
		Scanner rangeReader = new Scanner(range);

		ArrayList<String> newFormula = new ArrayList<String>();

		while (rangeReader.hasNext()) {
			newFormula.add(rangeReader.next());
		}

		String rangeStart = newFormula.get(0);
		String rangeEnd = newFormula.get(2);

		int xStart = getXPosition(rangeStart);
		int yStart = getYPosition(rangeStart);

		int xEnd = getXPosition(rangeEnd);
		int yEnd = getYPosition(rangeEnd);

		Cell[] temp = new Cell[((xEnd + 1) - xStart) * ((yEnd + 1) - yStart)];
		int count = 0;

		if (command.equalsIgnoreCase("sorta")) {
			sortUp(cellSheet, xStart, yStart, xEnd, yEnd, temp, count);
		}

		else {

			sortDown(cellSheet, xStart, yStart, xEnd, yEnd, temp, count);
		}
	}

	private static void sortUp(Cell[][] cellSheet, int xStart, int yStart, int xEnd, int yEnd, Cell[] temp,
			int count) {
		for (int x = xStart; x < xEnd + 1; x++) {
			for (int y = yStart; y < yEnd + 1; y++) {

				temp[count] = cellSheet[y][x];
				count++;

			}
		}

		Arrays.sort(temp);
		count = 0;

		for (int x = xStart; x < xEnd + 1; x++) {
			for (int y = yStart; y < yEnd + 1; y++) {

				cellSheet[y][x] = temp[count];
				count++;

			}
		}
	}

	private static void sortDown(Cell[][] cellSheet, int xStart, int yStart, int xEnd, int yEnd, Cell[] temp,
			int count) {
		for (int x = xStart; x < xEnd + 1; x++) {
			for (int y = yStart; y < yEnd + 1; y++) {

				temp[count] = cellSheet[y][x];
				count++;

			}
		}

		Arrays.sort(temp);
		count = 0;
		for (int x = xStart; x < xEnd + 1; x++) {
			for (int y = yStart; y < yEnd + 1; y++) {
				count++;
				cellSheet[y][x] = temp[(temp.length) - count];
			}
		}
	}

	private static void saveFile(Scanner sc) throws FileNotFoundException {
		String fileName = sc.next();
		PrintStream ps = new PrintStream(new File(fileName));
		ps.print(cmd);
		System.out.println("Saved " + fileName);
	}

	private static void printGrid(String command, Cell[][] cellSheet) {
		Grid spreadsheet = new Grid(cellSheet);
		cmd = saveCommand(command, cmd);
		spreadsheet.print(cellSheet);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * -------------------------- ------------------------------------ASSISTING
	 * METHODS--------------------------------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 */

	/*
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * -------------------------- -------------------------Methods to check for
	 * inputs and understand inputs.----------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 */

	private static boolean isHelp(String command) {
		return command.equalsIgnoreCase("HELP");
	}

	private static boolean inputNotContainEqual(String nextToken) {
		return !nextToken.trim().equals("=");
	}

	private static boolean isLoad(String command) {
		return command.equalsIgnoreCase("LOAD");
	}

	private static boolean isPrint(String command) {
		return command.equalsIgnoreCase("PRINT");
	}

	private static boolean isQuit(String command) {
		return command.equalsIgnoreCase("QUIT");
	}

	private static boolean isACellCoordinate(String command) {
		return "ABCDEFG".contains(command.substring(0, 1));
	}

	private static boolean isSave(String command) {
		return command.equalsIgnoreCase("SAVE");
	}

	private static boolean isAFormula(String nextToken) {
		// Check for Parenthase which signals formula
		return nextToken.contains("(");
	}

	private static boolean isAString(String nextToken) {
		return nextToken.contains("\"");
	}

	private static boolean isADate(String nextToken) {
		return nextToken.contains("/");
	}

	/*
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 */

	/*
	 * Action methods. This includes findY, findX, getCellContent, and saveCommand.
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 * -----------------------------------------------------------------------------
	 * --------------------------
	 */
	private static int findY(String command) {
		return Integer.parseInt(command.substring(1)) - 1;
	}

	private static int findX(String command) {
		return "ABCDEFG".indexOf(command.substring(0, 1));
	}

	private static String removeParenthasies(String nextToken) {
		return nextToken.substring(3, nextToken.length() - 2);
	}

	private static Cell getCellContent(int x, int y, String nextToken) {

		// Condition and Command to create TextCell
		if (isAString(nextToken)) {
		
			return newTextCell(x, y, nextToken);
		
		}

		else
		// How to check for formula and
		// create a new formula cell.
		if (isAFormula(nextToken)) {

			return newFormulaCell(x, y, nextToken);
		
		}

		// How to check for formula and
		// create a new formula cell.
		else// Condition and command to create Date cell
		if (isADate(nextToken)) {
			
			DateCell newDateCell = new DateCell(x, y, nextToken);
			return newDateCell;
		
		}
		// Condition and Command to create NumberCell
		else {
			
			String value = nextToken;
			NumberCell newNumberCell = new NumberCell(x, y, Double.parseDouble(value));
			return newNumberCell;
		
		}
	}

	private static Cell newFormulaCell(int x, int y, String nextToken) {
		// makes the formula into just a string of coordinates and
		// values, and gets rid of the parenthasies.
		nextToken = removeParenthasies(nextToken);
		// assigns the input to a new formula cell and puts it into
		return new FormulaCell(nextToken, x, y);
	}

	private static Cell newTextCell(int x, int y, String nextToken) {
		String text = nextToken.substring(2, nextToken.length());
		TextCell newTextCell = new TextCell(x, y, text);
		return newTextCell;
	}

	private static String saveCommand(String input, String cmd) {
		cmd += input + " ";
		return cmd;
	}


    /*
     * 2. method takes token and returns the X coordinate of the letter part of the
     * token. Because if uses indexOf, we will get the 0-based index which will help
     * us because our  cellSheet is also 0-based index, so (1,1) is actually (0,0).
     * 
     * @param token - String with coordinates, ex. "A4" return: int - the x position
     * of the coordinate, ex. "0".
     */
    private static int getXPosition(String token) {
        return "ABCDEFG".indexOf(token.substring(0, 1).toUpperCase());
    }

    /*
     * 3. method takes token and returns the 0 coordinate of the letter part of the
     * token. We will subtract 1 because the method gives us the number which is
     * just parsed from the token, and we want to make the YPosition 0-indexed to
     * fit our  cellSheet scheme.
     * 
     * @param String token - String with coordinates, ex. "A4" return: int - the y
     * position of the coordinate, ex. "4".
     */
    private static int getYPosition(String token) {
        return Integer.parseInt(token.substring(1)) - 1;
    }
}