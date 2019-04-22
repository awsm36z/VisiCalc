
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

    private static final double INVALID_VALUE = Double.MAX_VALUE;
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
        if (this.formula.length() == 5){
            return this.formula + "  ";
        }
        if(this.formula.length() == 8){
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

        if (newFormula.size() == 1) {
            return resolveToNumber(newFormula.get(0), cellSheet);
        }

        if (newFormula.get(0).equals("Sum")){
            //double sum = 0.0;
            String rangeStart = newFormula.get(2);
            String rangeEnd = newFormula.get(4);
            int xStart = getXPosition(rangeStart);
            int yStart = getYPosition(rangeEnd);

            System.out.println(xStart +" , "+ yStart);

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

    private int getFirstMultiplicationOrDivisionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("/") == -1) {
            return newFormula.indexOf("*");
        } else if (newFormula.indexOf("*") == -1) {
            return newFormula.indexOf("/");
        }

        return Math.min(newFormula.indexOf("/"), newFormula.indexOf("*"));
    }

    private int getFirstAdditionOrSubtractionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("-") == -1) {
            return newFormula.indexOf("+");
        } else if (newFormula.indexOf("+") == -1) {
            return newFormula.indexOf("-");
        }

        return Math.min(newFormula.indexOf("+"), newFormula.indexOf("-"));
    }

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

    private int getXPosition(String firstToken) {
        return "ABCDEFG".indexOf(firstToken.substring(0, 1));
    }

    private int getYPosition(String firstToken) {
        return Integer.parseInt(firstToken.substring(1)) - 1;
    }

    private boolean hasDivisionFirst(ArrayList<String> newFormula) {
        return newFormula.indexOf("/") > newFormula.indexOf("*");
    }

    private boolean hasSubtractionFirst(ArrayList<String> newFormula) {
        return newFormula.indexOf("-") > newFormula.indexOf("+");
    }

    /*
     * Method will recieve a string that was pre-checked to be a position for a
     * cell. The method will then return the cell value. But if the cell is not a
     * number cell, it will exit out of the method and tell the user that the input
     * is invalid.
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
            if(value.length() > 0){
                return Double.parseDouble(value);
            }
            return 0;

        } else {
            return Double.parseDouble(token);
        }
    }
}