package org.imse.gaitrawparser.data;

public class Line {

	private DoublePoint p2;
	private DoublePoint p1;
	private DoublePoint A;

	public Line(DoublePoint p1, DoublePoint p2) {
		if (p1.x < p2.x) {
			this.p1 = p1;
			this.p2 = p2;
		} else {
			this.p1 = p2;
			this.p2 = p1;
		}
		A = new DoublePoint(p2.x - p1.x, p2.y - p1.y);
	}
	
	public Line(double p1x, double p1y, double p2x, double p2y) {
		this(new DoublePoint(p1x, p1y), new DoublePoint(p2x, p2y));
	}

	public DoublePoint getP1() {
		return p1;
	}

	public DoublePoint getP2() {
		return p2;
	}
	
	public double getYForX(double x) {
		double lambda = (x - p1.x) / A.x;
		return p1.y + lambda * A.y;
	}
	
}
