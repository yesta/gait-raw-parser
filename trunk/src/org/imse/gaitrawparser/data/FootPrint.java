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
	protected List<Point> innerPoints;
	protected List<Point> outerPoints;
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
		calculateInnerPoints();
		double minDiff = Double.MAX_VALUE;
		double minDistance = 8;
		while (a == null && g == null) {
			for (int i = 0; i < innerPoints.size(); i++) {
				for (int j = i + 1; j < innerPoints.size(); j++) {
					Point p1 = innerPoints.get(i);
					Point p2 = innerPoints.get(j);
					if (p1.x > p2.x) {
						Point temp = p1;
						p1 = p2;
						p2 = temp;
					}
					Line line = getInnerLine(p1, p2);
					if (distance(p1, p2) < minDistance) {
						continue;
					}
					double diff = Math.abs(getTargetAngleAG() - getAngle(line));
					if (allPointsOutside(line, true) && diff < minDiff) {
						minDiff = diff;
						if (line.getP1().x < line.getP2().x) {
							a = line.getP1();
							g = line.getP2();
						} else {
							a = line.getP2();
							g = line.getP1();
						}
					}
				}
			}
			minDistance = minDistance - 1;
		}
		
		calculateOuterPoints();
		minDiff = Double.MAX_VALUE;
        minDistance = 8;
		while ( l == null && r == null){
		    for (int i = 0; i < outerPoints.size(); i++) {
                for (int j = i + 1; j < outerPoints.size(); j++) {
                    Point p1 = outerPoints.get(i);
                    Point p2 = outerPoints.get(j);
                    if (p1.x > p2.x) {
                        Point temp = p1;
                        p1 = p2;
                        p2 = temp;
                    }
                    Line line = getOuterLine(p1, p2);
                    if (distance(p1, p2) < minDistance) {
                        continue;
                    }
                    double diff = Math.abs(getTargetAngleLR() - getAngle(line));
                    if (allPointsOutside(line, false) && diff < minDiff) {
                        minDiff = diff;
                        if (line.getP1().x < line.getP2().x) {
                            l = line.getP1();
                            r = line.getP2();
                        } else {
                            l = line.getP2();
                            r = line.getP1();
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

	protected boolean allPointsOutside(Line l, boolean calculatingInnerLine) {
	    List<Point> points;
	    if (calculatingInnerLine){
	        calculateInnerPoints();
	        points = innerPoints;
	    } else {
	        calculateOuterPoints();
	        points = outerPoints;
	    }
		
		for (Point p : points) {
			if ((p.x == (int) l.getP1().x && p.y == (int) l.getP1().y) ||
					(p.x == (int) l.getP2().x && p.y == (int) l.getP2().y)) {
				continue;
			}
			double yl = l.getYForX(p.x);
			double yr = l.getYForX(p.x + 1);
			if (calculatingInnerLine){
			    if (!isOutsideInnerLine(p.y, yl, yr)) {
			        return false;
			    }
			} else{
			    if (!isOutsideOuterLine(p.y, yl, yr)) {
                    return false;
                }
			}
		}
		return true;
	}
	

	public void calculateInnerPoints() {
		if (innerPoints == null) {
			innerPoints = new ArrayList<Point>();
			for (int i = 0; i < lenX; i++) {
				for (int j = 0; j < lenY; j++) {
					if (pixel[i][j]) {
						if (isInnerPoint(i, j)) {
							innerPoints.add(new Point(i, j));
						}
					}
				}
			}
		}
	}
	
	public void calculateOuterPoints() {
	    if(outerPoints == null) {
	        outerPoints = new ArrayList<Point>();
	        for (int i = 0; i < lenX; i++) {
                for (int j = 0; j < lenY; j++) {
                    if (pixel[i][j]) {
                        if (isOuterPoint(i, j)) {
                            outerPoints.add(new Point(i, j));
                        }
                    }
                }
            }
	    }
	}
	
	protected abstract boolean isOutsideInnerLine(double py, double yl, double yr);
	
	protected abstract boolean isOutsideOuterLine(double py, double yl, double yr);
	
	protected abstract Line getInnerLine(Point p1, Point p2);
	
	protected abstract Line getOuterLine(Point p1, Point p2);
	
	protected abstract boolean isInnerPoint(int x, int y);
	
	protected abstract boolean isOuterPoint(int x, int y);
	
	protected abstract double getTargetAngleAG();
	
	protected abstract double getTargetAngleLR();

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
	
	public DoublePoint getL() {
	    return l;
	}
	
	public DoublePoint getR() {
	    return r;
	}
}
