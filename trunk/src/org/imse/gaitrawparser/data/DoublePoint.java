package org.imse.gaitrawparser.data;

import org.eclipse.swt.graphics.Point;

public class DoublePoint {

	public double x;
	public double y;
	
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public DoublePoint(Point p) {
		x = (double) p.x;
		y = (double) p.y;
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
}
