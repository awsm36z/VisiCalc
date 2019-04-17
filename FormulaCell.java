
/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

import java.util.*;

public class FormulaCell extends Cell implements Comparable<FormulaCell> {
    String formula;
    int row;
    int column;

    public FormulaCell(String formula, int row, int column) {
        super(row, column);
        this.formula = formula;
    }
    //---------- to String method, returns the value long enough for the Grid.-----------
    public String toString() {
        // For the grid toString we will only display the formula.
        if (this.formula.length() > 9) {
            return this.formula.substring(0, 9);
        }
        return this.formula;
    }
    //-----------------Get Value Method, returns literal value of the cell.--------------
     public String getValue() {
         return this.formula;
    }

    public String toString(Cell[][] cellSheet, String s) {
        if (this.formula.length() > 9) {
            return this.formula.substring(0, 10);
        }
        return this.formula;
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

        // We will create variable "operator" to hold what operation we are running 
        // and use it to find the first and second tokens
        // -- instead of just re-writing the code but changing the operator
        // for the indexOf portion, we will just have it look for the operator
        // which will be stored as a String.
        String operator = "";
        ArrayList<String> newFormula = new ArrayList<String>();

        //populate arraylist with the different tokens to calculate
        while (equationScanner.hasNext()) {
            newFormula.add(equationScanner.next());
        }

        // if the formula has Multiplication or
        // Division, then evaluate those first.

        // We check to see division first, but
        // in case there is multiplication...

        if (hasDivision(newFormula)) {

            // We will also check for that.
            // Then we will go on to check to see
            if (hasMultiplication(newFormula)) {

                // if the formula evaluates division before multiplication,
                // then go to the division place and evaluate it.
                if (hasDivisionFirst(newFormula)) {
                    answer = divide(cellSheet, newFormula);
                } else {
                    answer = multiply(cellSheet, newFormula);
                }
            } else {
                answer = divide(cellSheet, newFormula);
            }
        }   else if(hasAddition(newFormula)){

                    if (hasSubtraction(newFormula)){

                        if(hasSubtractionFirst(newFormula)){
                            answer = subtract(cellSheet, newFormula);
                        }

                        else{
                            answer = add(cellSheet, newFormula);
                        }


                    }
                    else{
                        answer = add(cellSheet, newFormula);
                    }
                }

                else if(hasSubtraction(newFormula)){
                    answer = subtract(cellSheet, newFormula);
                }

                else if(hasMultiplication(newFormula)){
                    answer = subtract(cellSheet, newFormula);
                }


        return answer;
    }

    private double multiply(Cell[][] cellSheet, ArrayList<String> newFormula) {
        double answer;
        String operator;
        operator = "*";
        answer = EvaluateDivision(cellSheet, operator, newFormula);
        return answer;
    }

    private double subtract(Cell[][] cellSheet, ArrayList<String> newFormula) {
        double answer;
        String operator;
        operator = "-";
        answer = EvaluateDivision(cellSheet, operator, newFormula);
        return answer;
    }

    private double add(Cell[][] cellSheet, ArrayList<String> newFormula) {
        double answer;
        String operator;
        operator = "+";
        answer = EvaluateDivision(cellSheet, operator, newFormula);
        return answer;
    }

    private double divide(Cell[][] cellSheet, ArrayList<String> newFormula) {
        double answer;
        String operator;
        operator = "/";
        answer = EvaluateDivision(cellSheet, operator, newFormula);
        return answer;
    }

    private boolean hasMultiplication(ArrayList<String> newFormula) {
        return newFormula.contains("*");
    }

    private boolean hasDivision(ArrayList<String> newFormula) {
        return newFormula.contains("/");
    }

    private boolean hasAddition(ArrayList<String> newFormula) {
        return newFormula.contains("+");
    }

    private boolean hasSubtraction(ArrayList<String> newFormula) {
        return newFormula.contains("-");
    }

    private double EvaluateDivision(Cell[][] cellSheet, String operator, ArrayList<String> newFormula) {

        // Now we will check to see if the tokens before and after
        // are numbers or a Cellsheet location.
        String firstToken = newFormula.get(getFirstTokenLocation(operator, newFormula));
        String secondToken = newFormula.get(newFormula.indexOf(operator) + 1);

        // Create a first and second term to be able to add them
        double firstTerm = resolveToNumber(firstToken, cellSheet);
        double secondTerm = resolveToNumber(secondToken, cellSheet);

        // -----------Now we return the evaluated expression--------
        double answer = 0.0;
        //Division
        if(operator.equals("/")){
            answer = firstTerm / secondTerm;
        }
        //Multiplication
        else if(operator.equals("*")){
            answer = firstTerm * secondTerm;
        }
        //Addition
        else if(operator.equals("+")){
            answer = firstTerm + secondTerm;
        }
        //Subtraction
        else {
            answer = firstTerm - secondTerm;
        }
        newFormula.set(newFormula.indexOf(operator), answer+"");
        newFormula.remove(getFirstTokenLocation(operator, newFormula));
        return answer;
    }

    private int getFirstTokenLocation(String operator, ArrayList<String> newFormula) {
        return newFormula.indexOf(operator) - 1;
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
    Method will recieve a string that was pre-checked
    to be a position for a cell. The method will then 
    return the cell value. But if the cell is not a number 
    cell, it will exit out of the method and tell the user that the input
    is invalid.
    */
    public double resolveToNumber(String token, Cell[][] cellSheet) {
        if ("ABCDEFG".contains(token.substring(0, 1))) {
            int xPos = getXPosition(token);
            int yPos = getYPosition(token);
            if(cellSheet[yPos][xPos] instanceof NumberCell){
                return Double.parseDouble(cellSheet[yPos][xPos].getValue()); 
            }
            return Double.MIN_VALUE;
        } else {
            return Double.parseDouble(token);
        }
    }
}