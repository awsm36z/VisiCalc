
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
        
        //if the input is looking for the 
        //sum of a range of values.
        if (isSum(newFormula)) {
            // double sum = 0.0;
            String rangeStart = newFormula.get(2);
            String rangeEnd = newFormula.get(4);
            int xStart = getXPosition(rangeStart);
            int yStart = getYPosition(rangeStart);

            int xEnd = getXPosition(rangeEnd);
            int yEnd = getYPosition(rangeEnd);
            
            /*if in the same column(same x)
            ________
            |______|
            |______|
            |______|
            |______|*/
            if (isColumn(xStart, xEnd)){

            } 

            /*if it is all in one row (same y)
            ___________________________________
            |_______|________|________|_______|

            */
            else if(isRow(yStart, yEnd)){

            }

            else /*if it is a rectangle*/ {

            }


                System.out.println(xStart + " , " + yStart);

        }
        //end of isSum method.

        //method to find the average
        //of a range of values
        if(newFormula.get(0).equals("AVG")){
            double total = 0;
            
            //get the string value of the beginning
            //and ending range.
            String rangeStart = newFormula.get(2);
            String rangeEnd = newFormula.get(4);
            
            //make the string values of the starting 
            //range into integers to evaluate in the 
            //grid
            int xStart = getXPosition(rangeStart);
            int yStart = getYPosition(rangeStart);

            //make the string values of the starting range into 
            //integers to evaluate in the grid.
            int xEnd = getXPosition(rangeEnd);
            int yEnd = getYPosition(rangeEnd);
            

            for (int x = xStart; x < xEnd; x++){
                for (int y = yStart; y < yEnd; y++){
                    
                }
            }
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


    public int avg(){
        return 1;
    }


    private boolean isRow(int yStart, int yEnd) {
        return yStart == yEnd;
    }

    private boolean isColumn(int xStart, int xEnd) {
        return isRow(xStart, xEnd);
    }

    private boolean isSum(ArrayList<String> newFormula) {
        return newFormula.get(0).equals("Sum");
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