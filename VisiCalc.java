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
 * 8. Jframe 						   |+10
 * 9. Parenthasies                     |+5
 * 10. Basic Error Checking            |+1
 * 11.Instant Reload        
 * -------------------------------------------------
 * Total:------------------------------|+34
 */

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class VisiCalc {
	// frame
	private JFrame frame;
	// Table
	private JTable table;
	// load file textfield
	private JTextField textField;
	// load button
	private JButton loadButton;
	// Quit Button
	private JButton quitButton;

	// Grid
	private static Cell[][] cellSheet;

	// Constructor
	public VisiCalc(Cell[][] cellsheet) {
		cellSheet = cellsheet;
		initializeFrame();
		initializeTable(cellsheet);
		initializeComponents();
	}

	private void initializeFrame() {
		frame = new JFrame("VisiCalc");
		frame.setSize(800, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initializeTable(Cell[][] cellsheet) {
		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G" };
		table = new JTable(cellsheet, columnNames) {
			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				cellSheet[rowIndex][columnIndex] = CellParser.getCellContent(rowIndex, columnIndex, (String) aValue);
			}

			@Override
			public String getValueAt(int row, int column) {
				if (cellSheet[row][column] instanceof FormulaCell) {
					return ((FormulaCell) cellSheet[row][column]).getValue(cellSheet);
				}
				return cellSheet[row][column].toString();
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.setBounds(30, 40, 200, 300);
		JScrollPane sp = new JScrollPane(table);
		frame.add(sp);
	}

	private void initializeComponents() {
		textField = new JTextField("", 20);
		textField.setBounds(305, 230, 200, 40);

		quitButton = new JButton("Quit!");
		quitButton.setBounds(640, 300, 140, 40);
		quitButton.addActionListener(e -> {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			System.exit(0);
		});

		loadButton = new JButton("Load!");
		loadButton.setBounds(330, 300, 140, 40);
		loadButton.addActionListener(e -> {
			String inputString = textField.getText();
			try (Scanner sc = new Scanner(inputString)) {
				String command = sc.next();
				processCommand(command, sc, cellSheet);
			} catch (FileNotFoundException ex) {
				System.out.println("File Not Found");
				textField.setText("File Not Found");
			}
			textField.setText("");
			table.repaint();
		});

		frame.add(quitButton);
		frame.add(textField);
		frame.add(loadButton);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Cell[][] cellSheet = new Cell[10][7];
		VisiCalc visicalc = new VisiCalc(cellSheet);
		visicalc.start();
	}

	private void start() throws FileNotFoundException {
		try (Scanner scOne = new Scanner(System.in)) {
			initializeCellSheet();
			Grid spreadsheet = new Grid(cellSheet);
			spreadsheet.print(cellSheet);

			boolean quit = false;
			while (!quit) {
				System.out.println("ENTER:");
				String input = scOne.nextLine();
				try (Scanner sc = new Scanner(input)) {
					String command = sc.next();
					quit = processCommand(command, sc, cellSheet);
				}
			}
		}
		System.out.println("Thanks for using VisiCalc!\nMade by Yassine El Yacoubi\nP.1\nMulvayne");
	}

	private void initializeCellSheet() {
		for (int i = 0; i < cellSheet.length; i++) {
			for (int j = 0; j < cellSheet[i].length; j++) {
				cellSheet[i][j] = new Cell(i, j);
			}
		}
	}

	private boolean processCommand(String command, Scanner sc, Cell[][] cellSheet) throws FileNotFoundException {
		if (isQuit(command)) {
			return true;
		} else if (isPrint(command)) {
			printGrid(command, cellSheet);
		} else if (isLoad(command)) {
			loadFileCommands(sc, cellSheet);
		} else if (isClearMethod(command)) {
			clearCellOrGrid(command, sc, cellSheet);
		} else if (isACellCoordinate(command)) {
			handleCellCoordinate(command, sc, cellSheet);
		} else if (isHelp(command)) {
			printHelpText();
		} else if (isSave(command)) {
			saveFile(sc);
		} else if (isSort(command)) {
			sortGridRange(command, sc, cellSheet);
		} else {
			System.out.println("Invalid input, please try again, or type \"help\" to see the possibilities. " + command);
		}
		return false;
	}

	private void handleCellCoordinate(String command, Scanner sc, Cell[][] cellSheet) {
		int x = findX(command);
		int y = findY(command);
		cmd = saveCommand(command, cmd) + " ";

		if (sc.hasNext()) {
			String nextToken = sc.next();
			if (inputNotContainEqual(nextToken)) {
				System.out.println("Invalid token. Expected = sign after cell number");
				return;
			}
			assignCellToGrid(sc, cellSheet, x, y, nextToken);
		} else {
			printCellContent(cellSheet, x, y);
		}
	}

	private void printCellContent(Cell[][] cellSheet, int x, int y) {
		if (isFormulaCell(cellSheet, x, y)) {
			printFormulaCell(cellSheet, x, y);
		} else {
			printOtherCells(cellSheet, x, y);
		}
	}

	private void assignCellToGrid(Scanner sc, Cell[][] cellSheet, int x, int y, String nextToken) {
		cmd = saveCommand(nextToken, cmd);
		nextToken = sc.nextLine();
		cmd = saveCommand(nextToken, cmd) + "\n";

		cellSheet[y][x] = CellParser.getCellContent(x, y, nextToken);
	}

	private boolean isFormulaCell(Cell[][] cellSheet, int x, int y) {
		return cellSheet[y][x] instanceof FormulaCell;
	}

	private void printFormulaCell(Cell[][] cellSheet, int x, int y) {
		System.out.println(((FormulaCell) cellSheet[y][x]).getValue(cellSheet));
	}

	private void printOtherCells(Cell[][] cellSheet, int x, int y) {
		System.out.println(cellSheet[y][x].getValue());
	}

	private void loadFileCommands(Scanner sc, Cell[][] cellSheet) throws FileNotFoundException {
		String fileToRead = sc.next();
		Scanner commandReader = new Scanner(new File(fileToRead));
		while (commandReader.hasNext()) {
			String testInput = commandReader.next();
			processCommand(testInput, commandReader, cellSheet);
		}
	}

	private boolean isClearMethod(String command) {
		return command.equalsIgnoreCase("clear");
	}

	private void clearCellOrGrid(String command, Scanner sc, Cell[][] cellSheet) {
		cmd = saveCommand(command, cmd);

			command = sc.next();
			cmd = saveCommand(command, cmd);
			cmd += "\n";
			clearCell(command, cellSheet);
	}

	private void clearCell(String command, Cell[][] cellSheet) {
		if (command.equalsIgnoreCase(".")) {
			clearGrid();
		} 
		else {

			int x = findX(command);
			int y = findY(command);
			cellSheet[y][x] = new Cell(x, y);

		}
	}

	private void clearGrid() {
		for (int i = 0; i < cellSheet.length; i++) {
			for (int j = 0; j < cellSheet[i].length; j++) {
				cellSheet[i][j] = new Cell(i, j);
			}
		}
	}

	private void printHelpText() {
		System.out.println(
				"type PRINT to print the grid\n type HELP to see the menu\n type LOAD to load the file commands\n type QUIT to quit the program");
	}

	private boolean isSort(String command) {
		return command.equalsIgnoreCase("sorta") || command.equalsIgnoreCase("sortd");
	}

	private void sortGridRange(String command, Scanner sc, Cell[][] cellSheet) {
		String range = sc.nextLine();
		Scanner rangeReader = new Scanner(range);

		ArrayList<String> newFormula = new ArrayList<String>();

		while (rangeReader.hasNext()) {
			newFormula.add(rangeReader.next());
		}
		rangeReader.close();

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

	private void sortUp(Cell[][] cellSheet, int xStart, int yStart, int xEnd, int yEnd, Cell[] temp, int count) {
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

	private void sortDown(Cell[][] cellSheet, int xStart, int yStart, int xEnd, int yEnd, Cell[] temp, int count) {
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

	private void saveFile(Scanner sc) throws FileNotFoundException {
		String fileName = sc.next();
		PrintStream ps = new PrintStream(new File(fileName));
		ps.print(cmd);
		System.out.println("Saved " + fileName);
	}

	private void printGrid(String command, Cell[][] cellSheet) {
		Grid spreadsheet = new Grid(cellSheet);
		cmd = saveCommand(command, cmd);
		cmd += "\n";
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

	private int findY(String command) {
		return Integer.parseInt(command.substring(1)) - 1;
	}

	private int findX(String command) {
		return "ABCDEFG".indexOf(command.substring(0, 1));
	}

	private boolean isHelp(String command) {
		return command.equalsIgnoreCase("HELP");
	}

	private boolean inputNotContainEqual(String nextToken) {
		return !nextToken.trim().equals("=");
	}

	private boolean isLoad(String command) {
		return command.equalsIgnoreCase("LOAD");
	}

	private boolean isPrint(String command) {
		return command.equalsIgnoreCase("PRINT");
	}

	private boolean isQuit(String command) {
		return command.equalsIgnoreCase("QUIT");
	}

	private boolean isACellCoordinate(String command) {
		return "ABCDEFG".contains(command.substring(0, 1));
	}

	private boolean isSave(String command) {
		return command.equalsIgnoreCase("SAVE");
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

	private String saveCommand(String input, String cmd) {
		cmd += input + "";
		return cmd;
	}

	/*
	 * 2. method takes token and returns the X coordinate of the letter part of the
	 * token. Because if uses indexOf, we will get the 0-based index which will help
	 * us because our cellSheet is also 0-based index, so (1,1) is actually (0,0).
	 * 
	 * @param token - String with coordinates, ex. "A4" return: int - the x position
	 * of the coordinate, ex. "0".
	 */
	private int getXPosition(String token) {
		return "ABCDEFG".indexOf(token.substring(0, 1).toUpperCase());
	}

	/*
	 * 3. method takes token and returns the 0 coordinate of the letter part of the
	 * token. We will subtract 1 because the method gives us the number which is
	 * just parsed from the token, and we want to make the YPosition 0-indexed to
	 * fit our cellSheet scheme.
	 * 
	 * @param String token - String with coordinates, ex. "A4" return: int - the y
	 * position of the coordinate, ex. "4".
	 */
	private int getYPosition(String token) {
		return Integer.parseInt(token.substring(1)) - 1;
	}
}