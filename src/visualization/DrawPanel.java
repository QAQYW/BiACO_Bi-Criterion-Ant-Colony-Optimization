package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	/*
	 * 1. seperate station node and task node
	 * 2. coordinate mapping
	 */
	
	ArrayList<Point> pointList;
	ArrayList<Line> lineList;
	
	public DrawPanel(ArrayList<Point> points, ArrayList<Line> lines) {
		this.pointList = points;
		this.lineList = lines;
	}
	
	public void paint(Graphics g) {
		double dwidth = (double) getWidth();
		double dheight = (double) getHeight();
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// draw lines
		g2d.setStroke(new BasicStroke(4));
		for (Line line : lineList) {
			g2d.setColor(line.getC());
			double ux = dwidth * line.getU().getX() / 8000.0;
			double uy = dwidth * line.getU().getY() / 8000.0;
//			double vx = dwidth * line.getV().getX() / 8000.0;
//			double vy = dwidth * line.getV().getY() / 8000.0;
			double vx = dheight * line.getV().getX() / 8000.0;
			double vy = dheight * line.getV().getY() / 8000.0;
			g2d.draw(new Line2D.Double(ux, uy, vx, vy));
		}
		
		// draw points
		g.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(5));
		for (Point point : pointList) {
			g2d.setColor(point.getC());
			double x = dwidth * point.getX() / 8000.0;
			double y = dheight * point.getY() / 8000.0;
			g2d.draw(new Line2D.Double(x, y, x, y));
		}
	}
}
