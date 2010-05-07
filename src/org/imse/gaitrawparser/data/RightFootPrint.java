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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Point> getInnerPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double getTargetAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean allPointsOutside(Line l) {
		// TODO Auto-generated method stub
		return false;
	}

}
