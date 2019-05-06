/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class Cell implements Comparable<Cell> {
	int row;
	int column;
	String value = "";
	public Cell(int row, int column) {
		this.row = row;
		this.column = column;
	}
	public String toString() {
		return value;
	}
	public String getValue() {
		return value;
	}
	public double toDouble(){
		return Double.parseDouble(value);
	}
	
	public int compareTo(Cell other){
		if(other instanceof TextCell){
            return -1;
        }
        if(other instanceof NumberCell){
            return -1;
        }
        if (other instanceof DateCell){
            return -1;
        }
        if (other instanceof FormulaCell){
            return -1;
		}
		return 0;
	}
}