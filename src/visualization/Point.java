package visualization;

import java.awt.Color;

public class Point {
	
	private double x;
	private double y;
	private Color c;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}
	
//	public double getDistance(Point p) {
//		double dx = this.x - p.getX();
//		double dy = this.y - p.getY();
//		return Math.sqrt(dx * dx + dy * dy);
//	}
}
