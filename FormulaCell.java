
/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

import java.util.*;

public class FormulaCell extends Cell implements Comparable<FormulaCell> {
    /**
     *
     */

    private static final double INVALID_VALUE = (double) Integer.MAX_VALUE;
    String formula;
    int row;
    int column;

    public FormulaCell(String formula, int row, int column) {
        super(row, column);
        this.formula = formula;
    }

    // ---------- to String method, returns the value long enough for the
    // Grid.-----------
    public String toString() {
        // For the grid toString we will only display the formula.
        if (this.formula.length() > 8) {
            return this.formula.substring(0, 9);
        }
        if (this.formula.length() == 5) {
            return this.formula + "  ";
        }
        if (this.formula.length() == 8) {
            return this.formula + "   ";
        }
        return this.formula + " ";
    }

    // -----------------Get Value Method, returns literal value of the
    // cell.--------------
    public String getValue() {
        return this.formula;
    }

    public String getValue(Cell[][] cellSheet) {
        double answer = solve(cellSheet);
        if (INVALID_VALUE == answer) {
            return ("NaN");
        }
        return answer + "";
    }

    public int compareTo(FormulaCell other) {
        // fix this to actually return comparable.

        return -1;
    }

    public double solve(Cell[][] cellSheet) {
        /*
         * We will create an arraylist that will contain all tokens. The arraylist will
         * help us then easily breakdown our formula and allow us to traverse the
         * tokens.
         */

        Scanner equationScanner = new Scanner(this.formula);
        double answer = 0.0;

        ArrayList<String> newFormula = new ArrayList<String>();

        // populate arraylist with the different tokens to calculate
        while (equationScanner.hasNext()) {
            newFormula.add(equationScanner.next());
        }
        equationScanner.close();

        if (newFormula.size() == 1) {
            return resolveToNumber(newFormula.get(0), cellSheet);
        }

        // if the input is looking for the
        // sum of a range of values.
        if (isSum(newFormula)) {
            double sum = 0.0;
            sum = getSum(cellSheet, newFormula, sum);

            return sum;
        }
        // end of isSum method.

        // method to find the average
        // of a range of values
        if (isAvg(newFormula)) {
            double total = 0;
            double sum = 0.0;
            int count = 0;

            String rangeStart = newFormula.get(2);
            String rangeEnd = newFormula.get(4);

            int xStart = getXPosition(rangeStart);
            int yStart = getYPosition(rangeStart);

            int xEnd = getXPosition(rangeEnd);
            int yEnd = getYPosition(rangeEnd);


            for (int y = yStart; y < yEnd + 1; y++) {
                for (int x = xStart; x < xEnd + 1; x++) {
                    count++;
                    value = cellSheet[y][x].getValue();
                    sum += Double.parseDouble(value);
                }
            }

            return sum/count;
        }


        int operatorIndex = -1;

        // calculate all higher order operations (/ or *) first
        while (((operatorIndex = getFirstMultiplicationOrDivisionIndex(newFormula)) != -1) && answer != INVALID_VALUE) {
            answer = evaluatAndSimplify(cellSheet, operatorIndex, newFormula);
        }

        // calculate lower order operations (+ or -) next
        while (((operatorIndex = getFirstAdditionOrSubtractionIndex(newFormula)) != -1) && answer != INVALID_VALUE) {
            answer = evaluatAndSimplify(cellSheet, operatorIndex, newFormula);
        }
        return answer;
    }

    private double getSum(Cell[][] cellSheet, ArrayList<String> newFormula, double sum) {
        String rangeStart = newFormula.get(1);
        String rangeEnd = newFormula.get(3);
        int xStart = getXPosition(rangeStart);
        int yStart = getYPosition(rangeStart);

        int xEnd = getXPosition(rangeEnd);
        int yEnd = getYPosition(rangeEnd);

        for (int y = yStart; y < yEnd + 1; y++) {
            for (int x = xStart; x < xEnd + 1; x++) {

                value = cellSheet[y][x].getValue();
                sum += Double.parseDouble(value);

            }
        }
        return sum;
    }

    private boolean isAvg(ArrayList<String> newFormula) {
        return newFormula.get(0).equalsIgnoreCase("AVG");
    }

    // ---------------------METHOD TO TAKE IN THE FORMULA AND SOLVE
    // IT----------------------

    private double evaluatAndSimplify(Cell[][] cellSheet, int operatorIndex, ArrayList<String> newFormula) {

        // Now we will check to see if the tokens before and after
        // are numbers or a Cellsheet location.
        String firstToken = newFormula.get(operatorIndex - 1);
        String secondToken = newFormula.get(operatorIndex + 1);

        // Create a first and second term to be able to add them
        double firstTerm = resolveToNumber(firstToken, cellSheet);
        double secondTerm = resolveToNumber(secondToken, cellSheet);

        if (firstTerm == INVALID_VALUE || secondTerm == INVALID_VALUE) {
            return INVALID_VALUE;
        }

        String operator = newFormula.get(operatorIndex);

        // -----------Now we return the evaluated expression--------
        double answer = calculate(operator, firstTerm, secondTerm);

        newFormula.set(newFormula.indexOf(operator), answer + "");
        // We trim down the array starting from the right
        // so that the left indicies remain valid.
        newFormula.remove(operatorIndex + 1);
        newFormula.remove(operatorIndex - 1);

        return answer;
    }
    /*
     * -----------------------------------------------------------------------------
     * -----------------------------------------------------------------------------
     * ------------------------------------ASSISTING METHODS------------------------
     * -----------------------------------------------------------------------------
     * -----------------------------------------------------------------------------
     * 
     * Table of Contents:
     * 
     * 1.calculate 2.getXposition 3.getYPosition 4.resolveToNumber 5.isRow
     * 6.isColumn 7.isSum 8.getFirstMultiplicationOrDivisionIndex
     * 9.getFirstAdditionOrSubtractionIndex
     * 
     */

    /*
     * 1. method to finish calculation by preforming simple task of multiplying the
     * first and second term together.
     * 
     * @param: String operator - the operator will tell us what operation to perform
     * double firstTerm - the first number before the operator double secondTerm -
     * the first number AFTER the operator
     * 
     * Used in method Evaluate and Simplify.
     */
    private double calculate(String operator, double firstTerm, double secondTerm) {
        double answer = 0.0;
        // Division
        if (operator.equals("/")) {
            answer = firstTerm / secondTerm;
        }

        // Multiplication
        else if (operator.equals("*")) {
            answer = firstTerm * secondTerm;
        }

        // Addition
        else if (operator.equals("+")) {
            answer = firstTerm + secondTerm;
        }

        // Subtraction
        else {
            answer = firstTerm - secondTerm;
        }
        return answer;
    }

    /*
     * 2. method takes token and returns the X coordinate of the letter part of the
     * token. Because if uses indexOf, we will get the 0-based index which will help
     * us because our cellsheet is also 0-based index, so (1,1) is actually (0,0).
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
     * fit our cellsheet scheme.
     * 
     * @param String token - String with coordinates, ex. "A4" return: int - the y
     * position of the coordinate, ex. "4".
     */
    private int getYPosition(String token) {
        return Integer.parseInt(token.substring(1)) - 1;
    }

    /**
     * 4. Method will recieve a string that was pre-checked to be a position for a
     * cell. The method will then return the cell value. But if the cell is not a
     * number cell, it will exit out of the method and tell the user that the input
     * is invalid.
     * 
     * @param String token Cell[][] cellsheet return double
     * 
     */
    public double resolveToNumber(String token, Cell[][] cellSheet) {
        if ("ABCDEFG".contains(token.substring(0, 1))) {
            int xPos = getXPosition(token);
            int yPos = getYPosition(token);
            if (cellSheet[yPos][xPos] instanceof FormulaCell) {
                return Double.parseDouble(((FormulaCell) cellSheet[yPos][xPos]).getValue(cellSheet));
            } else if (cellSheet[yPos][xPos] instanceof NumberCell) {
                return cellSheet[yPos][xPos].toDouble();
            }

            String value = cellSheet[yPos][xPos].getValue();
            if (value.length() > 0) {
                return Double.parseDouble(value);
            }
            return 0;

        } else {
            return Double.parseDouble(token);
        }
    }

    /**
     * 5. Method will check to see if the two y coordinates are the same, ex. "A4 -
     * C4"
     * 
     * @param yStart
     * @param yEnd
     * @return boolean
     */
    private boolean isColumn(int yStart, int yEnd) {
        return yStart == yEnd;
    }

    /*
     * 6. Method will check to see if the two x coordinates are the same, ex.
     * "A1 - A9"
     * 
     * @param xStart
     * 
     * @param xEnd
     * 
     * @return
     */
    private boolean isRow(int xStart, int xEnd) {
        return isColumn(xStart, xEnd);
    }

    /**
     * 7. Method will check to see if the ArrayList starts with SUM (ignores case)
     * 
     * @param newFormula
     * @return
     */
    private boolean isSum(ArrayList<String> newFormula) {
        return newFormula.get(0).equalsIgnoreCase("Sum");
    }

    /*
     * 8. Assertion: ArrayList will contain multiplication or division - the method
     * is called after the check asserted previously passes.
     * 
     * The following method's goal is to achieve the index of the higher order
     * opperation. To do so, we will check to see if newFormula contains a division
     * sign, if no, then it returns the index of the the multiplication sign. If
     * yes, it checks to see if the array contains the multiplication sign, if no,
     * then it retruns the index of the division sign. Otherwise, it will return the
     * smaller index of the two operators using Math.min
     * 
     * @param newFormula
     * 
     * @return
     */
    private int getFirstMultiplicationOrDivisionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("/") == -1) {
            return newFormula.indexOf("*");
        } else if (newFormula.indexOf("*") == -1) {
            return newFormula.indexOf("/");
        }

        return Math.min(newFormula.indexOf("/"), newFormula.indexOf("*"));
    }

    /*
     * 9. Assertion: ArrayList will contain addition or subtraction - the method is
     * called after the check asserted previously passes.
     * 
     * The following method's goal is to achieve the index of the lower order
     * opperation. To do so, we will check to see if newFormula contains a
     * subtraction sign, if no, then it returns the index of the the addition sign.
     * If yes, it checks to see if the array contains the addition sign, if no, then
     * it retruns the index of the subtraction sign. Otherwise, it will return the
     * smaller index of the two operators using Math.min
     * 
     * @param newFormula
     * 
     * @return
     */
    private int getFirstAdditionOrSubtractionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("-") == -1) {
            return newFormula.indexOf("+");
        } else if (newFormula.indexOf("+") == -1) {
            return newFormula.indexOf("-");
        }

        return Math.min(newFormula.indexOf("+"), newFormula.indexOf("-"));
    }
}