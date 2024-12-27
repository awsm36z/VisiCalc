/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/

import java.util.*;

public class FormulaCell extends Cell implements Comparable<Cell> {
    private static final double INVALID_VALUE = Double.MAX_VALUE;
    private String formula;

    public FormulaCell(String formula, int row, int column) {
        super(row, column);
        this.formula = formula;
    }

    @Override
    public String toString() {
        if (this.formula.length() > 8) {
            return this.formula.substring(0, 9);
        }
        return this.formula + " ";
    }

    @Override
    public String getValue() {
        return this.formula;
    }

    public String getValue(Cell[][] cellSheet) {
        double answer = solve(cellSheet, this.formula);
        if (INVALID_VALUE == answer) {
            return "NaN";
        }
        return formatAnswer(answer);
    }

    private String formatAnswer(double answer) {
        String strNum = String.valueOf(answer);
        switch (strNum.length()) {
            case 2:
            case 3:
            case 5:
            case 7:
                return strNum + "  ";
            case 4:
            case 6:
                return strNum + " ";
            default:
                return strNum + " ";
        }
    }

    @Override
    public int compareTo(Cell other) {
        if (other instanceof TextCell || other instanceof NumberCell || other instanceof DateCell) {
            return -1;
        }
        if (other instanceof FormulaCell) {
            return 0;
        }
        return 1;
    }

    public double solve(Cell[][] cellSheet, String formula) {
        Scanner equationScanner = new Scanner(formula);
        ArrayList<String> newFormula = new ArrayList<>();

        while (equationScanner.hasNext()) {
            newFormula.add(equationScanner.next());
        }
        equationScanner.close();

        if (newFormula.size() == 1) {
            return resolveToNumber(newFormula.get(0), cellSheet);
        }

        if (isSum(newFormula)) {
            return getSum(cellSheet, newFormula);
        }

        if (isAvg(newFormula)) {
            return getAvg(cellSheet, newFormula);
        }

        return solveWithOrderOfOps(cellSheet, newFormula);
    }

    private double solveWithOrderOfOps(Cell[][] cellSheet, ArrayList<String> newFormula) {
        while (newFormula.contains("(")) {
            int beginningIndex = newFormula.lastIndexOf("(");
            int endIndex = newFormula.indexOf(")");
            ArrayList<String> temp = new ArrayList<>(newFormula.subList(beginningIndex + 1, endIndex));
            double tempAnswer = solveWithOrderOfOps(cellSheet, temp);

            for (int i = 0; i <= endIndex - beginningIndex; i++) {
                newFormula.remove(beginningIndex);
            }
            newFormula.add(beginningIndex, String.valueOf(tempAnswer));
        }

        int operatorIndex;
        while ((operatorIndex = getExponentsIndex(newFormula)) != -1) {
            evaluateAndSimplify(cellSheet, operatorIndex, newFormula);
        }

        while ((operatorIndex = getFirstMultiplicationOrDivisionIndex(newFormula)) != -1) {
            evaluateAndSimplify(cellSheet, operatorIndex, newFormula);
        }

        while ((operatorIndex = getFirstAdditionOrSubtractionIndex(newFormula)) != -1) {
            evaluateAndSimplify(cellSheet, operatorIndex, newFormula);
        }

        return Double.parseDouble(newFormula.get(0));
    }

    private double getSum(Cell[][] cellSheet, ArrayList<String> newFormula) {
        String rangeStart = newFormula.get(1);
        String rangeEnd = newFormula.get(3);
        int xStart = getXPosition(rangeStart);
        int yStart = getYPosition(rangeStart);
        int xEnd = getXPosition(rangeEnd);
        int yEnd = getYPosition(rangeEnd);

        double sum = 0.0;
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                sum += resolveToNumber(cellSheet[y][x].getValue(), cellSheet);
            }
        }
        return sum;
    }

    private double getAvg(Cell[][] cellSheet, ArrayList<String> newFormula) {
        String rangeStart = newFormula.get(1);
        String rangeEnd = newFormula.get(3);
        int xStart = getXPosition(rangeStart);
        int yStart = getYPosition(rangeStart);
        int xEnd = getXPosition(rangeEnd);
        int yEnd = getYPosition(rangeEnd);

        double sum = 0.0;
        int count = 0;
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                sum += resolveToNumber(cellSheet[y][x].getValue(), cellSheet);
                count++;
            }
        }
        return sum / count;
    }

    private double evaluateAndSimplify(Cell[][] cellSheet, int operatorIndex, ArrayList<String> newFormula) {
        String firstToken = newFormula.get(operatorIndex - 1);
        String secondToken = newFormula.get(operatorIndex + 1);

        double firstTerm = resolveToNumber(firstToken, cellSheet);
        double secondTerm = resolveToNumber(secondToken, cellSheet);

        if (firstTerm == INVALID_VALUE || secondTerm == INVALID_VALUE) {
            return INVALID_VALUE;
        }

        String operator = newFormula.get(operatorIndex);
        double answer = calculate(operator, firstTerm, secondTerm);

        newFormula.set(operatorIndex, String.valueOf(answer));
        newFormula.remove(operatorIndex + 1);
        newFormula.remove(operatorIndex - 1);

        return answer;
    }

    private double calculate(String operator, double firstTerm, double secondTerm) {
        switch (operator) {
            case "/":
                return firstTerm / secondTerm;
            case "*":
                return firstTerm * secondTerm;
            case "+":
                return firstTerm + secondTerm;
            case "%":
                return firstTerm % secondTerm;
            case "^":
                return Math.pow(firstTerm, secondTerm);
            default:
                return firstTerm - secondTerm;
        }
    }

    private int getXPosition(String token) {
        return "ABCDEFG".indexOf(token.substring(0, 1).toUpperCase());
    }

    private int getYPosition(String token) {
        return Integer.parseInt(token.substring(1)) - 1;
    }

    private double resolveToNumber(String token, Cell[][] cellSheet) {
        if ("ABCDEFG".contains(token.substring(0, 1))) {
            int xPos = getXPosition(token);
            int yPos = getYPosition(token);
            if (cellSheet[yPos][xPos] instanceof FormulaCell) {
                return Double.parseDouble(((FormulaCell) cellSheet[yPos][xPos]).getValue(cellSheet));
            } else if (cellSheet[yPos][xPos] instanceof NumberCell) {
                return cellSheet[yPos][xPos].toDouble();
            }
            return Double.parseDouble(cellSheet[yPos][xPos].getValue());
        } else {
            return Double.parseDouble(token);
        }
    }

    private boolean isSum(ArrayList<String> newFormula) {
        return newFormula.get(0).equalsIgnoreCase("SUM");
    }

    private boolean isAvg(ArrayList<String> newFormula) {
        return newFormula.get(0).equalsIgnoreCase("AVG");
    }

    private int getExponentsIndex(ArrayList<String> newFormula) {
        return newFormula.indexOf("^");
    }

    private int getFirstMultiplicationOrDivisionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("/") == -1) {
            if (newFormula.indexOf("*") == -1) {
                return newFormula.indexOf("%");
            }
            if (newFormula.indexOf("%") == -1) {
                return newFormula.indexOf("*");
            }
            return Math.min(newFormula.indexOf("%"), newFormula.indexOf("*"));
        } else if (newFormula.indexOf("*") == -1) {
            if (newFormula.indexOf("/") == -1) {
                return newFormula.indexOf("%");
            }
            return Math.min(newFormula.indexOf("%"), newFormula.indexOf("/"));
        }

        return Math.min(Math.min(newFormula.indexOf("/"), newFormula.indexOf("*")), newFormula.indexOf("%"));
    }

    private int getFirstAdditionOrSubtractionIndex(ArrayList<String> newFormula) {
        if (newFormula.indexOf("-") == -1) {
            return newFormula.indexOf("+");
        } else if (newFormula.indexOf("+") == -1) {
            return newFormula.indexOf("-");
        }

        return Math.min(newFormula.indexOf("+"), newFormula.indexOf("-"));
    }
}