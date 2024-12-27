/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class TextCell extends Cell implements Comparable<Cell> {
	private String text;

	public TextCell(int x, int y, String text) {
		super(x, y);
		this.text = text;
	}

	@Override
	public String toString() {
		if (this.text.length() > 9) {
			return this.text.substring(0, 9);
		}
		if (this.text.length() % 2 == 1) {
			return this.text.substring(0, this.text.length() - 1) + " ";
		}
		return this.text.substring(0, this.text.length() - 1);
	}

	@Override
	public String getValue() {
		return this.text;
	}

	@Override
	public int compareTo(Cell other) {
		if (other instanceof TextCell) {
			return this.text.compareToIgnoreCase(((TextCell) other).text);
		}
		return 1;
	}
}
