package org.imse.gaitrawparser.data.calculation;

import java.util.List;

import org.imse.gaitrawparser.data.input.DoubleLine;
import org.imse.gaitrawparser.data.input.DoublePoint;
import org.imse.gaitrawparser.data.input.FootPrint;

public class StepLengthCalculator implements MetricCalculator {
	
	@Override
	public PerFootPrintResult calculate(List<FootPrint> footPrints) {
		PerFootPrintResult result = new PerFootPrintResult(footPrints, "Step Length", "See Gatrite Document 3.4");
		
		result.setValueForFootPrint(0, null);
		
		for (int i = 0; i < footPrints.size() - 2; i++) {
			DoubleLine agLine = new DoubleLine(footPrints.get(i).getHeelCenter(), footPrints.get(i + 2).getHeelCenter());
			DoublePoint agNormal = new DoublePoint(agLine.getA().y, -agLine.getA().x);
			DoubleLine ldLine = new DoubleLine(footPrints.get(i + 1).getHeelCenter(), new DoublePoint(footPrints.get(i + 1).getHeelCenter().x + agNormal.x, footPrints.get(i + 1).getHeelCenter().y + agNormal.y));
			DoublePoint l = agLine.getIntersection(ldLine);
			DoublePoint alVector = new DoublePoint(l.x - footPrints.get(i).getHeelCenter().x, l.y - footPrints.get(i).getHeelCenter().y);
			double alLength = alVector.getLength();
			DoublePoint agVector = agLine.getA();
			if ((new DoublePoint(alVector.x + agVector.x, alVector.y + agVector.y)).getLength() < agVector.getLength()) {
				alLength *= -1;
			}
			result.setValueForFootPrint(i, SensorToDistanceConverter.convertSensorUnitToCentimeters(alLength));
		}

		result.setValueForFootPrint(footPrints.size() - 2, null);
		result.setValueForFootPrint(footPrints.size() - 1, null);
		result.setUnit("cm");
		return result;
	}

}
