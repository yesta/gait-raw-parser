package org.imse.gaitrawparser.data.calculator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.PressurePoint;

public class SideswingCalculator implements MetricCalculator {
	
	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
	    PerStepResult result = new PerStepResult(footPrints, "SideswingCalculator", "Distance between Walkline and Step, where Walkline is the line, " +
	    	                                     "where the average of the distance between all steps and the line is minimal. The Sideswing is messured in Sensors");
	    
		
		
		for(int i = 0; i < footPrints.size(); i++) {
		   double value = getDistanceFromAxis(calculateMinimalAxis(footPrints), footPrints.get(i).getHeelCenter());
		   result.setValueForStep(i, SensorToDistanceConverter.convertSensorUnitToCentimeters(value)); 
		}
		
		return result;
	}
	
	public DoubleLine calculateMinimalAxis(List<FootPrint> footPrints) {
		double minimalAverageSideswing = Double.MAX_VALUE;
		double averageSideswing = 0;
		DoubleLine minimalAxis = null;

		double maxY = 0;
		double minY = Double.MAX_VALUE;
		for (FootPrint f : footPrints) {
			for (PressurePoint p : f.getPressurePoints()) {
				if (p.getY() > maxY) {
					maxY = p.getY();
				}
				if (p.getY() < minY) {
					minY = p.getY();
				}
			}
		}
		
		for (double start = minY; start <= maxY; start += 0.1) {
			for (int angle = -45; angle <= 45; angle++) {
				double y = 5 * Math.tan(((double) angle) * Math.PI / 180.0) + start;
				DoubleLine axis = new DoubleLine(new DoublePoint(0, start), new DoublePoint(5, y));
				averageSideswing = computeAverageSideswing(axis, footPrints);
				if (averageSideswing < minimalAverageSideswing) {
				    minimalAverageSideswing = averageSideswing;
				    minimalAxis = axis;
				}
			}
		}
		return minimalAxis;
	}
	
	private double computeAverageSideswing(DoubleLine axis, List <FootPrint> footPrints) {
        double average = 0;
        
        for (FootPrint p : footPrints) {
            average += getDistanceFromAxis(axis, p.getHeelCenter());
        }
        return average;
    }


    private double getDistanceFromAxis(DoubleLine axis, DoublePoint point) {
    	DoublePoint axisNormal = new DoublePoint(axis.getA().y, -axis.getA().x);
		DoubleLine  axisNormalLine = new DoubleLine(point, new DoublePoint(point.x + axisNormal.x, point.y - axisNormal.y));
		DoublePoint intersection = axis.getIntersection(axisNormalLine);
		DoublePoint normal = new DoublePoint(intersection.x - point.x, intersection.y - point.y);
		return normal.getLength();
	}

}
