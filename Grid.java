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

	public void print() {
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
		
		
		//draws the rows and numbers them.
		for (int x = 0; x < this.spreadsheet.length; x++) {

			if (x > 8) {
				System.out.print(" " + (x + 1) + " ");
			}

			else {
				System.out.print("  " + (x + 1) + " ");
			}

			System.out.print("|");

			for (int y = 0; y < this.spreadsheet[x].length; y++) {
				String content = spreadsheet[x][y].toString();

				if (content.length() > 9) {
					content = content.substring(0, 10);
				}else if (content.length() == 0) {
					printSpaces(content);
					System.out.print(content);
					printSpaces(content);
					System.out.print(" |");
				}else {
					printSpaces(content);
					System.out.print(content);
					printSpaces(content);
					System.out.print("|");
				}



			}
			System.out.println("");

			drawBorder();

		}
		System.out.println("\n \n");

	}

	/**
	 * 
	 */
	private void drawBorder() {
		System.out.print("----+");
		for (int i = 0; i < 7; i++) {

			System.out.print("---------+");
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