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
	
	public String getValue() {
		return this.number + "";
	} 

	public double toDouble(){
		return number;
	}
	
	public String toString() {
		String strNum = this.number + "";
		if (strNum.length()==2){
			return strNum + "  ";
		}
		if(strNum.length() == 3){
			return strNum + "  ";
		}
		if (strNum.length() == 4){
			return strNum + " ";
		}
		if (strNum.length() == 5){
			return strNum + "  ";
		}
		if (strNum.length() == 6){
			return strNum + " ";
		}
		if (strNum.length() == 7){
			return strNum + "  ";
		}
		return strNum + " ";
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
