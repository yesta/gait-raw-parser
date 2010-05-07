package org.imse.gaitrawparser.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public abstract class FootPrint {

	protected boolean[][] pixel;
	protected List<Point> takenPoints = new ArrayList<Point>();
	protected List<PressurePoint> pressurePoints = new ArrayList<PressurePoint>();
	protected double firstTouchTime;
	protected int lenY;
	protected int lenX;

	private DoublePoint a;
	private DoublePoint l;
	private DoublePoint r;
	private DoublePoint g;
	
	public FootPrint(int lenX, int lenY) {
		this.lenX = lenX;
		this.lenY = lenY;
		pixel = new boolean[lenX][lenY];
	}
	
	public void calculateALRG() {
		if (getFoot() == Foot.Right) {
			return;
		}
		List<Point> points = getInnerPoints();
		double minDiff = Double.MAX_VALUE;
		double minDistance = 15;
		while (a == null && g == null) {
			for (int i = 0; i < points.size(); i++) {
				for (int j = i + 1; j < points.size(); j++) {
					Point p1 = points.get(i);
					Point p2 = points.get(j);
					Line l = getInnerLine(p1, p2);
					if (distance(p1, p2) < minDistance) {
						continue;
					}
					double diff = Math.abs(getTargetAngle() - getAngle(l));
					if (allPointsOutside(l) && diff < minDiff) {
						minDiff = diff;
						if (l.getP1().x < l.getP2().x) {
							a = l.getP1();
							g = l.getP2();
						} else {
							a = l.getP2();
							g = l.getP1();
						}
					}
				}
			}
			minDistance = minDistance - 1;
		}
	}
	
	private double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(((double) p2.x) - ((double) p1.x), 2) + Math.pow(((double) p2.y) - ((double) p1.y), 2));
	}

	private double getAngle(Line l) {
		return Math.atan((l.getP2().y - l.getP1().y) / (l.getP2().x - l.getP1().x));
	}

	protected abstract boolean allPointsOutside(Line l);
	
	protected abstract List<Point> getInnerPoints();
	
	protected abstract Line getInnerLine(Point p1, Point p2);	
	
	protected abstract double getTargetAngle();

	public void addPressurePoint(PressurePoint p) {
		if (!pixel[p.getX()][p.getY()]) {
			takenPoints.add(new Point(p.getX(), p.getY()));
			pixel[p.getX()][p.getY()] = true;
		}
		pressurePoints.add(p);
		updateFirstTouchTime(p.getTime());
	}
	
	private void updateFirstTouchTime(double time) {
		if (time < firstTouchTime) {
			firstTouchTime = time;
		}
	}
	
	public abstract Foot getFoot();

	public DoublePoint getA() {
		return a;
	}
	
	public DoublePoint getG() {
		return g;
	}
	
}
