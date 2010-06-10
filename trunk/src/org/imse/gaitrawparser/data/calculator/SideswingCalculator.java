package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;

public class SideswingCalculator implements MetricCalculator {

	private double maxY = 100;
	
	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		double minimalSideswing = Double.MAX_VALUE;
		
		for (double i = 0.0; i <= maxY; i += 0.1) {
			for (double j = 0.0; j <= 2.0; j += .05) {
				DoubleLine axis = new DoubleLine(new DoublePoint(0, i), new DoublePoint(1, j));
				for (FootPrint f : footPrints) {
					
				}
			}
		}
		
		return null;
	}
	
	private double getDistanceFromAxis(DoubleLine axis, DoublePoint point) {
		DoubleLine axisNormal = new DoubleLine(point, new DoublePoint(point.x + axis.getA().x, point.y + axis.getA().y));
		return axis.getIntersection(axisNormal).getLength();
	}

}
