/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

/**
 * Represents a cell in the VisiCalc spreadsheet.
 */
public class Cell implements Comparable<Cell> {
    private int row;
    private int column;
    private String value = "";

    /**
     * Constructs a Cell with the specified row and column.
     * 
     * @param row the row index of the cell
     * @param column the column index of the cell
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the string representation of the cell's value.
     * 
     * @return the cell's value as a string
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the value of the cell.
     * 
     * @return the cell's value
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts the cell's value to a double.
     * 
     * @return the cell's value as a double
     * @throws NumberFormatException if the value cannot be parsed as a double
     */
    public double toDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // Handle the exception or rethrow it
            throw new NumberFormatException("Value cannot be converted to double: " + value);
        }
    }

    /**
     * Compares this cell to another cell.
     * 
     * @param other the cell to compare to
     * @return a negative integer, zero, or a positive integer as this cell is less than, equal to, or greater than the specified cell
     */
    @Override
    public int compareTo(Cell other) {
        if (other instanceof TextCell) {
            return -1;
        }
        if (other instanceof NumberCell) {
            return -1;
        }
        if (other instanceof DateCell) {
            return -1;
        }
        if (other instanceof FormulaCell) {
            return -1;
        }
        return 0;
    }
}