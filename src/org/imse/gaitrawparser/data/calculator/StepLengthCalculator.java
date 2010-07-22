package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;

public class StepLengthCalculator implements MetricCalculator {


	@Override
	public PerStepResult calculate(List<FootPrint> footPrints) {
		PerStepResult result = new PerStepResult(footPrints, "Step Length", "See Gatrite Document 3.4");
		
		result.setValueForStep(0, null);
		
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
			result.setValueForStep(i, SensorToDistanceConverter.convertSensorUnitToCentimeters(alLength));
		}

		result.setValueForStep(footPrints.size() - 2, null);
		result.setValueForStep(footPrints.size() - 1, null);
		result.setUnit("cm");
		return result;
	}

}
