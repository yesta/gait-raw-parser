package org.imse.gaitrawparser.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public abstract class FootPrint {

	private boolean[][] pixel;
	private List<Point> takenPoints = new ArrayList<Point>();
	private List<PressurePoint> pressurePoints = new ArrayList<PressurePoint>();
	private double firstTouchTime;
	
	public FootPrint(int maxX, int maxY) {
		pixel = new boolean[maxX][maxY];
	}
	
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
	
}
