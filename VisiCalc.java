
/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class VisiCalc {

	static String cmd = "";

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scOne = new Scanner(System.in);
		Cell[][] cellsheet = new Cell[10][7];

		// Populates the cellsheet with empty cells.
		for (int i = 0; i < cellsheet.length; i++) {
			for (int j = 0; j < cellsheet[i].length; j++) {
				cellsheet[i][j] = new Cell(i, j);
			}
		}

		// Creates grid and populates it with the already set cellsheet.
		Grid spreadsheet = new Grid(cellsheet);
		spreadsheet.print();

		//
		boolean quit = false;
		while (!quit) {
			System.out.println("ENTER:");
			String input = scOne.nextLine();
			Scanner sc = new Scanner(input);

			// The first token will either be the command or the cell number
			// we will name the variable: command for simplicity, assuming cell numbers
			// are an implicit command to move to that cell.
			String command = sc.next();
			quit = processCommand(command, sc, cellsheet, scOne);
		}
	}

	/**
	 * @param scOne
	 * @param cmd
	 * @param sc
	 * @param spreadsheet
	 * @param command
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	private static boolean processCommand(String command, Scanner sc, Cell[][] cellSheet, Scanner scOne)
			throws FileNotFoundException {

		if (command.equalsIgnoreCase("QUIT")) {
			return true;
		}

		else if (command.equalsIgnoreCase("PRINT")) {
			Grid spreadsheet = new Grid(cellSheet);
			cmd = saveCommand(command, cmd);
			spreadsheet.print();
		}
		// Load Function code
		else if (command.equalsIgnoreCase("LOAD")) {
			// takes in file to read from user.
			String fileToRead = sc.next();
			Scanner commandReader = new Scanner(new File(fileToRead));

			while (commandReader.hasNext()) {
				String testInput = commandReader.next();
				processCommand(testInput, commandReader, cellSheet, scOne);
			}
		} else if (isACellCoordinate(command)) {

			int x = findX(command);
			int y = findY(command);
			cmd = saveCommand(command, cmd);

			// check if the user wants to simply see cell content or if attempting
			// to create new cell and populate it.
			if (sc.hasNext()) {
				String nextToken = sc.next();
				// after cell coordinates, the next token has to be an equal sign
				if (!nextToken.trim().equals("=")) {
					System.out.println("invalid token. Expected = sign after cell number");
					return false;
				}

				cmd = saveCommand(nextToken, cmd);
				nextToken = sc.nextLine();
				cmd = saveCommand(nextToken, cmd) + "\n";

				cellSheet[y][x] = getCellContent(x, y, nextToken);
			}

			/*
			 * Command to just call and find a cell
			 */
			else {
				//Command will evaluate the formula everytime you call it.
				if(cellSheet[y][x] instanceof FormulaCell){
					System.out.println(((FormulaCell)cellSheet[y][x]).getValue(cellSheet));
				}
				//otherwise, just print the literal value.
				else{
					System.out.println(cellSheet[y][x].getValue());
				}
			}
		}

		else if (command.equalsIgnoreCase("HELP")) {
			System.out.println(
				"type PRINT to print the grid\\n type HELP to see the menu\\n type LOAD to load the file commands\\n type QUIT to quit the program");
		} 
		else if (command.equalsIgnoreCase("SAVE")) {
			String fileName = sc.next();
			PrintStream ps = new PrintStream(new File(fileName));
			ps.print(cmd);
		} else {
			System.out.println(command);
		}
		return false;
	}

	private static int findY(String command) {
		return Integer.parseInt(command.substring(1, 2)) - 1;
	}

	private static int findX(String command) {
		return "ABCDEFG".indexOf(command.substring(0, 1));
	}

	private static boolean isACellCoordinate(String command) {
		return "ABCDEFG".contains(command.substring(0, 1));
	}

	private static Cell getCellContent(int x, int y, String nextToken) {

		// Condition and Command to create TextCell
		if (isAString(nextToken)) {
			String text = nextToken.substring(2, nextToken.length());
			TextCell newTextCell = new TextCell(x, y, text);
			return newTextCell;
		}

		else
		// How to check for formula and
		// create a new formula cell.
		if (isAFormula(nextToken)) {
			// makes the formula into just a string of coordinates and
			// values, and gets rid of the parenthasies.
			nextToken = removeParenthasies(nextToken);
			// assigns the input to a new formula cell and puts it into
			return new FormulaCell(nextToken, x, y);
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

	private static String removeParenthasies(String nextToken) {
		return nextToken.substring(3, nextToken.length() - 2);
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

	private static String saveCommand(String input, String cmd) {
		cmd += input + " ";
		return cmd;
	}

}