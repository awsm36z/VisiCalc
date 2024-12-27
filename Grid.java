/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class Grid {
	private Cell[][] spreadsheet;
	private static final int WIDTH = 9;

	public Grid(Cell[][] spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	public void print(Cell[][] cellSheet) {
		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G" };
		printFourSpaces();
		System.out.print("|");

		for (String column : alphabet) {
			printFourSpaces();
			System.out.print(column);
			printFourSpaces();
			System.out.print("|");
		}
		System.out.println();
		drawBorder();

		for (int y = 0; y < 10; y++) {
			System.out.print(y > 8 ? " " + (y + 1) + " " : "  " + (y + 1) + " ");
			System.out.print("|");

			for (int x = 0; x < 7; x++) {
				String content = spreadsheet[y][x] instanceof FormulaCell
						? ((FormulaCell) spreadsheet[y][x]).getValue(cellSheet)
						: spreadsheet[y][x].toString();
				printContent(content);
			}
			System.out.println();
			drawBorder();
		}
		System.out.println("\n \n");
	}

	private void printContent(String content) {
		if (content.length() > WIDTH) {
			content = content.substring(0, WIDTH);
		}
		printSpaces(content);
		System.out.print(content);
		printSpaces(content);
		System.out.print("|");
	}

	private void drawBorder() {
		System.out.print("----*");
		for (int i = 0; i < 7; i++) {
			System.out.print("---------*");
		}
		System.out.println();
	}

	private void printSpaces(String content) {
		for (int spaces = 0; spaces < (WIDTH - content.length()) / 2; spaces++) {
			System.out.print(" ");
		}
	}

	private void printFourSpaces() {
		for (int spaces = 4; spaces > 0; spaces--) {
			System.out.print(" ");
		}
	}
}