public class CellParser{
        

        public static Cell getCellContent(int x, int y, String nextToken) {

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

    // private void evaluateTextFieldInput(String s, Cell[][] cellSheet,) throws FileNotFoundException {

	// 	// input loop
	// 		String input = s;
	// 		Scanner sc = new Scanner(input);

	// 		// The first token will either be the command or the cell number
	// 		// we will name the variable: command for simplicity, assuming cell numbers
	// 		// are an implicit command to move to that cell.
	// 		String command = sc.next();
	// 		return processCommand(command, sc, cellSheet);
	// 	System.out.println("Thanks for using VisiCalc!\nMade by Yassine El Yacoubi\nP.1\nMulvayne");
	// }
    
    


	/*private  processCommand(String command, Scanner sc, Ce
        return null;
    }*/
    
    

    private static String removeParenthasies(String nextToken) {
		return nextToken.substring(3, nextToken.length() - 2);
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
}
