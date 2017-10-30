package linkup.speed.service;

//汉字以UTF-8格式编码

public class Point {
	public int row;
	public int col;
	
	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void setPoint(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Point p = (Point) obj;
		return (this.row == p.row) && (this.col == p.col);
	}
	
	@Override
	public String toString() {
		return "[" + row + ", " + col + "]";
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
