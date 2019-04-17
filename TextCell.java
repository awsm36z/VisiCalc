/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class TextCell extends Cell implements Comparable <TextCell>{
	String text;
	public TextCell(int x, int y, String text) {
		super(x,y);
		// TODO Auto-generated constructor stub 
		this.text = text;
	}
	public String toString() {
		
		if (this.text.length()>9) {
			return this.text.substring(0, 9);
		}		
		return this.text.substring(0,this.text.length());

	
	}
	
	public String getValue() {
		return this.text;
	}

	public int compareTo(TextCell other) {
		return this.text.compareToIgnoreCase(other.text);
	}

}
