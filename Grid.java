/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class Grid {
	Cell[][] spreadsheet;

	public Grid(Cell[][] spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	public void print(Cell[][] cellSheet) {
		String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F", "G" };
		printFourSpaces();
		System.out.print("|");

		// Print for top row.
		for (int x = 0; x < this.spreadsheet[1].length; x++) {

			printFourSpaces();
			System.out.print(alphabet[x]);
			printFourSpaces();
			System.out.print("|");

		}
		System.out.println("");
		drawBorder();

		// draws the rows and numbers them.
		for (int y = 0; y < 10; y++) {

			if (y > 8) {
				System.out.print(" " + (y + 1) + " ");
			}
			// We now print out the rows on the relation of the
			// size of the index.
			else {
				System.out.print("  " + (y + 1) + " ");
			}

			System.out.print("|");

			for (int x = 0; x < 7; x++) {
				String content;
				// Will check to see if is formula to then
				// evaluate it using the formula cell evaluate
				// method.
				if (spreadsheet[y][x] instanceof FormulaCell) {
					content = ((FormulaCell) spreadsheet[y][x]).getValue(cellSheet);
				} else {
					content = spreadsheet[y][x].toString();
				}

				printContent(content);

			}
			System.out.println("");

			drawBorder();

		}
		System.out.println("\n \n");

	}

	private void printContent(String content) {
		if (contentSizeIsOdd(content)) {

			printOddSizedContent(content);

		} else {
			printEvenSizedContent(content);

		}
	}

	private boolean contentSizeIsOdd(String content) {
		return !(content.length() % 2 == 0);
	}

	private void printOddSizedContent(String content) {
		if (content.length() > 10) {
			// When the value is larger than the max size
			// we will shrink it and display a smaller version.

			content = content.substring(0, 9);
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print("|");
		} else if (content.length() == 0) {
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print(" |");
		} else {
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print("|");
		}
	}

	private void printEvenSizedContent(String content) {
		if (content.length() > 10) {
			// When the value is larger than the max size
			// we will shrink it and display a smaller version.

			content = content.substring(0, 9);
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print("|");
		} else if (content.length() == 0) {
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print(" |");
		} else {
			printSpaces(content);
			System.out.print(content);
			printSpaces(content);
			System.out.print("|");
		}
	}

	/**
	 * Draw border method, draws the dashes and stars
	 */
	private void drawBorder() {
		System.out.print("----*");
		for (int i = 0; i < 7; i++) {

			System.out.print("---------*");
		}
		System.out.println("");
	}

	/**
	 * @param content
	 */
	private void printSpaces(String content) {
		for (int spaces = 0; spaces < (9 - content.length()) / 2; spaces++) {
			System.out.print(" ");
		}
	}

	/**
	 * 
	 */
	private void printFourSpaces() {
		for (int spaces = 4; spaces > 0; spaces--) {
			System.out.print(" ");
		}
	}

}