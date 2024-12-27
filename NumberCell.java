/*
Yassine El Yacoubi
P.1
Mulvaney
VisiCalc Project
*/
public class NumberCell extends Cell implements Comparable<Cell> {
	private double number;

	public NumberCell(int row, int column, double number) {
		super(row, column);
		this.number = number;
	}

	@Override
	public String getValue() {
		return String.valueOf(this.number);
	}

	@Override
	public double toDouble() {
		return number;
	}

	@Override
	public String toString() {
		String strNum = String.valueOf(this.number);
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
		if (other instanceof NumberCell) {
			return Double.compare(this.number, ((NumberCell) other).number);
		}
		if (other instanceof TextCell) {
			return -1;
		}
		return 1;
	}
}
