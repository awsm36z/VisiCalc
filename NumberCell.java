/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class NumberCell extends Cell implements Comparable<NumberCell> {
	double number;
	public NumberCell(int row, int column, double number) {
		super(row, column);
		this.number = number;
		
	}
	
	public String toString() {
		return this.number + "";
	} 
	
	public String getValue() {
		String strNum = this.number + "";
		if (strNum.length()==2){
			return strNum + "  ";
		}
		return strNum;
	}

	public int compareTo(NumberCell other) {
		// TODO Auto-generated method stub
		if(this.number - other.number > 0) {
			return 1;
		}
		if (this.number - other.number < 0) {
			return -1;
		}
		return 0;
	}
}
