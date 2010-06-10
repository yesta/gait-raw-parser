package org.imse.gaitrawparser.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public abstract class FootPrint {
    
    protected double firstContact;
    protected double lastContact;
    protected double heelContact;
    protected double toeOff;
	protected boolean[][] pixel;
	protected List<Point> takenPoints = new ArrayList<Point>();
	protected List<PressurePoint> pressurePoints = new ArrayList<PressurePoint>();
	protected List<Point> innerPoints;
	protected List<Point> outerPoints;
	protected int lenY;
	protected int lenX;

	private Point heel2Point;
	private Point toe4Point;
	
	private DoublePoint a;
	private DoublePoint l;
	private DoublePoint r;
	private DoublePoint g;

	private DoublePoint c;
	private DoublePoint e;
	private DoublePoint p;
	private DoublePoint n;
	private DoublePoint heelCenter;
	
	public FootPrint(int lenX, int lenY) {
		this.lenX = lenX;
		this.lenY = lenY;
		pixel = new boolean[lenX][lenY];
		
		firstContact = Double.MAX_VALUE;
		lastContact = Double.MIN_NORMAL;
	}
	
	public void calculateALRG() {
		calculateInnerPoints();
		double minDiff = Double.MAX_VALUE;
		double minDistance = 8;
		
		Line agLine = null;
		
		while (agLine == null) {
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
					if (allPointsOnOneSideOfLine(line, takenPoints) && diff < minDiff) {
						minDiff = diff;
						agLine = new Line(line.getP1(), line.getP2());
					}
				}
			}
			minDistance = minDistance - 1;
		}
		
		calculateOuterPoints();
		minDiff = Double.MAX_VALUE;
        minDistance = 8;
        
        Line lrLine = null;
        
		while (lrLine == null) {
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
                    if (allPointsOnOneSideOfLine(line, takenPoints) && diff < minDiff) {
                        minDiff = diff;
                        lrLine = new Line(line.getP1(), line.getP2());
                    }
                }   
		    }
		    minDistance = minDistance - 1;
		}
		
		// L berechnen
		calculateInnerPoints();
		Line normal = null;
		
		Collections.sort(takenPoints, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				if (p1.x < p2.x) {
					return -1;
				} else if (p1.x == p2.x) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		
		for (int i = 0; i < takenPoints.size(); i++) {
			normal = getHeelNormalThroughPoint(agLine, takenPoints.get(i));
			if (allPointsOnOneSideOfLine(normal, takenPoints)) {
				heel2Point = takenPoints.get(i);
				break;
			} else {
				normal = null;
			}
		}
		
		a = normal.getIntersection(agLine);
		l = normal.getIntersection(lrLine);
		
		for (int i = takenPoints.size() - 1; i >= 0; i--) {
			normal = getToeNormalThroughPoint(agLine, takenPoints.get(i));
			if (allPointsOnOneSideOfLine(normal, takenPoints)) {
				toe4Point = takenPoints.get(i);
				break;
			} else {
				normal = null;
			}
		}
		
		
		g = normal.getIntersection(agLine);
		r = normal.getIntersection(lrLine);
		
		
		/*a = new DoublePoint(normal.getP1());
		g = new DoublePoint(normal.getP2());

		l = new DoublePoint(lrLine.getP1());
		r = new DoublePoint(lrLine.getP2());*/
		
		/*a = new DoublePoint(agLine.getP1());
		l = new DoublePoint(lrLine.getP1());
		g = new DoublePoint(agLine.getP2());
		r = new DoublePoint(lrLine.getP2());*/
	}
	
	public void calculateCNPE() {
		if (g == null || r == null || a == null || l == null) {
			throw new RuntimeException("You have to calculate ALRG first.");
		}
		
		
		DoublePoint agVector = new DoublePoint(g.x - a.x, g.y - a.y);
		
		c = new DoublePoint(a.x + agVector.x * 1/3, a.y + agVector.y * 1/3);
		e = new DoublePoint(a.x + agVector.x * 2/3, a.y + agVector.y * 2/3);
		
		DoublePoint agNormal = new DoublePoint(agVector.y, -agVector.x);

		DoubleLine cn = new DoubleLine(c, new DoublePoint(c.x + agNormal.x, c.y + agNormal.y));
		DoubleLine ep = new DoubleLine(e, new DoublePoint(e.x + agNormal.x, e.y + agNormal.y));
		
		DoubleLine lrLine = new DoubleLine(l, r);
		n = lrLine.getIntersection(cn);
		p = lrLine.getIntersection(ep);
		
		heelCenter = (new DoubleLine(a, n)).getIntersection(new DoubleLine(l, c));
		
		// Calculate Hell Contact/Toe Off
		Collections.sort(pressurePoints, new Comparator<PressurePoint>() {

			@Override
			public int compare(PressurePoint o1, PressurePoint o2) {
				if (o1.getTime() < o2.getTime()) {
					return -1;
				} else if (o1.getTime() > o2.getTime()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		for (int i = 0; i < pressurePoints.size(); i++) {
			if (isPartOfHeel(pressurePoints.get(i).getPoint())) {
				heelContact = pressurePoints.get(i).getTime();
				break;
			}
		}
		
		for (int i = pressurePoints.size() - 1; i >= 0; i--) {
			if (isPartOfToes(pressurePoints.get(i).getPoint())) {
				heelContact = pressurePoints.get(i).getTime();
				break;
			}
		}
	}
	
	private boolean isPartOfHeel(Point point) {
		DoubleLine cnNormal = new DoubleLine(c, n);
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		points.add(heel2Point);
		return allPointsOnOneSideOfLine(cnNormal, points);
	}
	
	private boolean isPartOfToes(Point point) {
		DoubleLine epNormal = new DoubleLine(e, p);
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		points.add(toe4Point);
		return allPointsOnOneSideOfLine(epNormal, points);
	}
	
	private Line getHeelNormalThroughPoint(Line agLine, Point p) {
		Point normalVector = new Point(-agLine.getA().y, agLine.getA().x);
		if (Math.signum(agLine.getA().x) != Math.signum(agLine.getA().y)) {
			// Linker unterer Eckpunkt
			return new Line(p.x, p.y + 1, p.x + normalVector.x, p.y + 1 + normalVector.y);
		} else {
			// Linker oberer Eckpunkt
			return new Line(p.x, p.y, p.x + normalVector.x, p.y + normalVector.y);
		}
	}
	
	private Line getToeNormalThroughPoint(Line agLine, Point p) {
		Point normalVector = new Point(-agLine.getA().y, agLine.getA().x);
		if (Math.signum(agLine.getA().x) != Math.signum(agLine.getA().y)) {
			// Rechter oberer
			return new Line(p.x + 1, p.y, p.x + 1 + normalVector.x, p.y + normalVector.y);
		} else {
			// Rechter unterer
			return new Line(p.x + 1, p.y + 1, p.x + 1 + normalVector.x, p.y + 1 + normalVector.y);
		}
	}
	
	private double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(((double) p2.x) - ((double) p1.x), 2) + Math.pow(((double) p2.y) - ((double) p1.y), 2));
	}

	private double getAngle(Line l) {
		return Math.atan((l.getP2().y - l.getP1().y) / (l.getP2().x - l.getP1().x));
	}

	// TODO Sollte weg, wenn Line eine Subclass von DoubleLine ist
	protected boolean allPointsOnOneSideOfLine(Line l, List<Point> points) {
		return allPointsOnOneSideOfLine(new DoubleLine(l), points);
	}
	
	protected boolean allPointsOnOneSideOfLine(DoubleLine l, List<Point> points) {
		int side = 0;
		int newSide = 0;
		for (Point p : points) {
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					double px = (double) p.x + i;
					double py = (double) p.y + j;
					if (l.getA().y != 0) {
						// Keine Waagrechte
						double d = l.getXForY(py) - px;
						newSide = (int) Math.signum(d);
					} else {
						// Keine Senkrechte
						double d = l.getYForX(px) - py;
						newSide = (int) Math.signum(d);
					}
					if (side == 0) {
						side = newSide;
					}
					if (newSide != 0 && newSide != side) {
						return false;
					}
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
		updateFirstContact(p.getTime());
		updateLastContact(p.getTime());
	}
	
	private void updateLastContact(double time) {
		if (time > lastContact) {
			lastContact = time;
		}
	}

	private void updateFirstContact(double time) {
		if (time < firstContact) {
			firstContact = time;
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

	public DoublePoint getC() {
		return c;
	}
	
	public DoublePoint getE() {
		return e;
	}
	
	public DoublePoint getP() {
	    return p;
	}
	
	public DoublePoint getN() {
	    return n;
	}
	
	public DoublePoint getHeelCenter() {
		return heelCenter;
	}
	
	public double getFirstContact() {
		return firstContact;
	}
	
	public double getLastContact() {
		return lastContact;
	}
	
	public double getFirstHeelContact() {
		return heelContact;
	}
	
	public double getToeOff() {
		return toeOff;
	}
}
