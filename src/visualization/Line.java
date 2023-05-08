package visualization;

import java.awt.Color;

public class Line {
	
	private Point u;
	private Point v;
	private Color c;
	
	public Line(Point u, Point v, Color c) {
		this.u = u;
		this.v = v;
		this.c = c;
	}
	
	public Point getU() {
		return u;
	}
	
	public Point getV() {
		return v;
	}
	
	public Color getC() {
		return c;
	}
}
