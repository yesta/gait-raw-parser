package org.imse.gaitrawparser.data;

import org.eclipse.swt.graphics.Point;

public class Line {

	private Point p2;
	private Point p1;
	private Point A;

	public Line(Point p1, Point p2) {
		if (p1.x < p2.x) {
			this.p1 = p1;
			this.p2 = p2;
		} else {
			this.p1 = p2;
			this.p2 = p1;
		}
		A = new Point(p2.x - p1.x, p2.y - p1.y);
	}
	
	public Line(int p1x, int p1y, int p2x, int p2y) {
		this(new Point(p1x, p1y), new Point(p2x, p2y));
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}
	
	public Point getA() {
		return A;
	}
	
	public double getYForX(double x) {
		double lambda = (x - ((double) p1.x)) / ((double) A.x);
		return ((double) p1.y) + lambda * ((double) A.y);
	}
	
	public double getXForY(double y) {
		double lambda = (y - ((double) p1.y)) / ((double) A.y);
		return ((double) p1.x) + lambda * ((double) A.x);
	}

	public DoublePoint getIntersection(Line agLine) {
		return LineHelper.getIntersection(new DoubleLine(this), new DoubleLine(agLine));
	}
	
	
}
