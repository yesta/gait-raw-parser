package org.imse.gaitrawparser.data;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class RightFootPrint extends FootPrint {

	public RightFootPrint(int lenX, int lenY) {
		super(lenX, lenY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Foot getFoot() {
		return Foot.Right;
	}

	@Override
	protected Line getInnerLine(Point p1, Point p2) {
		if (p1.y > p2.y) {
			return new Line(p1.x, p1.y, p2.x, p2.y);
		} else {
			return new Line(p1.x + 1, p1.y, p2.x + 1, p2.y);
		}
	}

	@Override
	protected double getTargetAngle() {
		return 0.1;
	}

	@Override
	protected boolean isOutside(double py, double yl, double yr) {
		if (yl > py || yr > py) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean isInnerPoint(int x, int y) {
		if (y - 1 >= 0 && pixel[x][y - 1] == false) {
			return true;
		} else {
			return false;
		}
	}

}
