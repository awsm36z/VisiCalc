/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class DateCell extends Cell {
		
	int day;
	int month;
	int year;
	
	public DateCell(int x, int y, int day, int month, int year) {
		super(x,y);
		
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public DateCell(int x, int y, String strDate){
		super(x,y);

		int month = Integer.parseInt(strDate.substring(1, 3));
		int day = Integer.parseInt(strDate.substring(4, 6));
		int year = Integer.parseInt(strDate.substring(7));
		this.day = day;
		this.month = month;
		this.year = year;
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
		return this.month + "/"+ this.day + "/" + this.year;
	}
	
	public String getValue() {
		return this.month + "/"+ this.day + "/" + this.year;
	}
}
