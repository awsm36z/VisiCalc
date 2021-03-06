/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class TextCell extends Cell implements Comparable <Cell>{
	String text;
	public TextCell(int x, int y, String text) {
		super(x,y);
		// TODO Auto-generated constructor stub 
		this.text = text;
	}
	
	//String print method.
	public String toString() {
		
		if (this.text.length()>9) {
			return this.text.substring(0, 9);
		}		
		if(this.text.length()%2 == 1) {
		return this.text.substring(0,this.text.length()-1) + " ";
		}
		return this.text.substring(0,this.text.length() - 1);
		
	
	}
	
	public String getValue() {
		return this.text;
	}

	public int compareTo(Cell other) {
		if(other instanceof TextCell){
			return this.text.compareToIgnoreCase(((TextCell)other).text);
		}
		return 1;
	}

}
