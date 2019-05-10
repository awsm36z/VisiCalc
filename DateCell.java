/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class DateCell extends Cell implements Comparable <Cell> {
		
	private int day;
	private int month;
	private int year;
	private int numericleValue;
	
	public DateCell(int x, int y, int day, int month, int year) {
		super(x,y);
		
		this.day = day;
		this.month = month;
		this.year = year;

		this.numericleValue = (this.day)+(this.month*30)+(this.year*365);
	}

	public DateCell(int x, int y, String strDate){
		super(x,y);

		int month = Integer.parseInt(strDate.substring(1, 3));
		int day = Integer.parseInt(strDate.substring(4, 6));
		int year = Integer.parseInt(strDate.substring(7));
		this.day = day;
		this.month = month;
		this.year = year;

		this.numericleValue = (this.day)+(this.month*30)+(this.year*365);
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String toString() {
		String date = this.month + "/"+ this.day + "/" + this.year;
		
		if (date.length()>9){
			return date.substring(0, 9);
		}
		if(date.length() == 8) {
			return date+" ";
		}
		return date;
		
	}
	
	public String getValue() {
		return this.month + "/"+ this.day + "/" + this.year;
	}

	public int compareTo (Cell other){
		if (other instanceof TextCell){
			return -1;
		}
		if (other instanceof FormulaCell){
			return 1;
		}
		if (other instanceof NumberCell){
			return -1;
		}
		if (other instanceof DateCell){
			return this.numericleValue - ((DateCell)other).numericleValue;
		}
		
		return 1;
	}


}
