package org.imse.gaitrawparser.data.calculator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;

public class SideswingCalculator implements MetricCalculator {

	private double maxY = 100;
	
	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
	    PerStepResult result = new PerStepResult("SideswingCalculator", "Distance between Walkline and Step, where Walkline is the line, " +
	    	                                     "where the average of the distance between all steps and the line is minimal. The Sideswing is messured in Sensors");
	    
		double minimalAverageSideswing = Double.MAX_VALUE;
		double averageSideswing = 0;
		DoubleLine minimalAxis = new DoubleLine(0, 0, 0, 0);
		
		for (double i = 0.0; i <= maxY; i += 0.1) {
			for (double j = -1.0; j <= 1.0; j += .05) {
				DoubleLine axis = new DoubleLine(new DoublePoint(0, i), new DoublePoint(1, i + j));
				averageSideswing = computeAverageSideswing(axis, footPrints);
				if (averageSideswing < minimalAverageSideswing) {
				    minimalAverageSideswing = averageSideswing;
				    minimalAxis = axis;
				}
			}
		}
		
		for(int i = 0; i < footPrints.size(); i++) {
		   result.setValueForStep(i, getDistanceFromAxis(minimalAxis, footPrints.get(i).getHeelCenter())); 
		}
		
		return result;
	}
	
	private double computeAverageSideswing(DoubleLine axis, List <FootPrint> footPrints) {
        double average = 0;
        
        for (FootPrint p : footPrints) {
            average += getDistanceFromAxis(axis, p.getHeelCenter());
        }
        return average;
    }


    private double getDistanceFromAxis(DoubleLine axis, DoublePoint point) {
		DoubleLine axisNormal = new DoubleLine(point, new DoublePoint(point.x + axis.getA().x, point.y + axis.getA().y));
		return axis.getIntersection(axisNormal).getLength();
	}

}
