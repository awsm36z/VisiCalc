public class CellParser {

    /**
     * Parses the content of a cell based on the provided token.
     * 
     * @param x the row index of the cell
     * @param y the column index of the cell
     * @param nextToken the content to be parsed
     * @return the parsed Cell object
     */
    public static Cell getCellContent(int x, int y, String nextToken) {
        if (isAString(nextToken)) {
            return newTextCell(x, y, nextToken);
        } else if (isAFormula(nextToken)) {
            return newFormulaCell(x, y, nextToken);
        } else if (isADate(nextToken)) {
            return new DateCell(x, y, nextToken);
        } else {
            try {
                double value = Double.parseDouble(nextToken);
                return new NumberCell(x, y, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid cell content: " + nextToken, e);
            }
        }
    }

    /**
     * Checks if the token is a string.
     * 
     * @param token the token to check
     * @return true if the token is a string, false otherwise
     */
    private static boolean isAString(String token) {
        // Implement the logic to check if the token is a string
        return token.startsWith("\"") && token.endsWith("\"");
    }

    /**
     * Checks if the token is a formula.
     * 
     * @param token the token to check
     * @return true if the token is a formula, false otherwise
     */
    private static boolean isAFormula(String token) {
        // Implement the logic to check if the token is a formula
        return token.startsWith("=");
    }

    /**
     * Checks if the token is a date.
     * 
     * @param token the token to check
     * @return true if the token is a date, false otherwise
     */
    private static boolean isADate(String token) {
        // Implement the logic to check if the token is a date
        return token.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    /**
     * Creates a new TextCell.
     * 
     * @param x the row index of the cell
     * @param y the column index of the cell
     * @param value the content of the cell
     * @return the created TextCell
     */
    private static TextCell newTextCell(int x, int y, String value) {
        return new TextCell(x, y, value);
    }

    /**
     * Creates a new FormulaCell.
     * 
     * @param x the row index of the cell
     * @param y the column index of the cell
     * @param value the content of the cell
     * @return the created FormulaCell
     */
    private static FormulaCell newFormulaCell(int x, int y, String value) {
        return new FormulaCell(x, y, value);
    }
}
