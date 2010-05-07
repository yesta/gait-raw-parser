package org.imse.gaitrawparser.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class LeftFootPrint extends FootPrint {

	private List<Point> innerPoints;

	public LeftFootPrint(int lenX, int lenY) {
		super(lenX, lenY);
	}

	@Override
	public Foot getFoot() {
		return Foot.Left;
	}

	@Override
	protected Line getInnerLine(Point p1, Point p2) {
		if (p1.x > p2.x) {
			Point temp = p1;
			p1 = p2;
			p2 = temp;
		}
		if (p1.y > p2.y) {
			return new Line(p1.x + 1, p1.y + 1, p2.x + 1, p2.y + 1);
		} else {
			return new Line(p1.x, p1.y + 1, p2.x, p2.y + 1);
		}
	}

	@Override
	protected List<Point> getInnerPoints() {
		if (innerPoints == null) {
			innerPoints = new ArrayList<Point>();
			for (int i = 0; i < lenX; i++) {
				for (int j = 0; j < lenY; j++) {
					if (pixel[i][j]) {
						if (j + 1 < lenY && pixel[i][j + 1] == false) {
							innerPoints.add(new Point(i, j));
						}
					}
				}
			}
		}
		return innerPoints;
	}

	@Override
	protected double getTargetAngle() {
		return -0.1;
	}

	@Override
	protected boolean allPointsOutside(Line l) {
		getInnerPoints();
		for (Point p : innerPoints) {
			double yl = l.getYForX(p.x);
			double yr = l.getYForX(p.x + 1);
			if (yl < p.y + 1 || yr < p.y + 1) {
				return false;
			}
		}
		return true;
	}

}
