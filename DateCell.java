/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

/**
 * Represents a date cell in the VisiCalc spreadsheet.
 */
public class DateCell extends Cell implements Comparable<Cell> {
    
    private int day;
    private int month;
    private int year;
    private int numericalValue;

    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 365;

    /**
     * Constructs a DateCell with the specified row, column, day, month, and year.
     * 
     * @param x the row index of the cell
     * @param y the column index of the cell
     * @param day the day of the date
     * @param month the month of the date
     * @param year the year of the date
     */
    public DateCell(int x, int y, int day, int month, int year) {
        super(x, y);
        this.day = day;
        this.month = month;
        this.year = year;
        this.numericalValue = calculateNumericalValue();
    }

    /**
     * Constructs a DateCell with the specified row, column, and date string.
     * 
     * @param x the row index of the cell
     * @param y the column index of the cell
     * @param strDate the date string in the format "MM/DD/YYYY"
     */
    public DateCell(int x, int y, String strDate) {
        super(x, y);
        try {
            this.month = Integer.parseInt(strDate.substring(0, 2));
            this.day = Integer.parseInt(strDate.substring(3, 5));
            this.year = Integer.parseInt(strDate.substring(6));
            this.numericalValue = calculateNumericalValue();
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid date format: " + strDate, e);
        }
    }

    /**
     * Returns the day of the date.
     * 
     * @return the day of the date
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the month of the date.
     * 
     * @return the month of the date
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year of the date.
     * 
     * @return the year of the date
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the day of the date.
     * 
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
        this.numericalValue = calculateNumericalValue();
    }

    /**
     * Sets the month of the date.
     * 
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
        this.numericalValue = calculateNumericalValue();
    }

    /**
     * Sets the year of the date.
     * 
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
        this.numericalValue = calculateNumericalValue();
    }

    /**
     * Returns the string representation of the date.
     * 
     * @return the date as a string in the format "MM/DD/YYYY"
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", month, day, year);
    }

    /**
     * Calculates the numerical value of the date.
     * 
     * @return the numerical value of the date
     */
    private int calculateNumericalValue() {
        return day + (month * DAYS_IN_MONTH) + (year * DAYS_IN_YEAR);
    }

    /**
     * Compares this date cell to another cell.
     * 
     * @param other the cell to compare to
     * @return a negative integer, zero, or a positive integer as this cell is less than, equal to, or greater than the specified cell
     */
    @Override
    public int compareTo(Cell other) {
        if (other instanceof DateCell) {
            return Integer.compare(this.numericalValue, ((DateCell) other).numericalValue);
        }
        return -1;
    }
}
