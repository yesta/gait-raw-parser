package org.imse.gaitrawparser.data.metric;

import java.util.List;

import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;

public class StepLengthCalculator implements MetricCalculator {


	@Override
	public PerStepResult calculate(List<FootPrint> footPrints) {
		PerStepResult result = new PerStepResult("Step Length", "See Gatrite Document 3.4");
		
		result.setValueForStep(0, 0);
		
		for (int i = 0; i < footPrints.size() - 2; i++) {
			DoubleLine agLine = new DoubleLine(footPrints.get(i).getHeelCenter(), footPrints.get(i + 2).getHeelCenter());
			DoublePoint agNormal = new DoublePoint(agLine.getA().y, -agLine.getA().x);
			DoubleLine ldLine = new DoubleLine(footPrints.get(i + 1).getHeelCenter(), new DoublePoint(footPrints.get(i + 1).getHeelCenter().x + agNormal.x, footPrints.get(i + 1).getHeelCenter().y + agNormal.y));
			DoublePoint l = agLine.getIntersection(ldLine);
			DoublePoint alVector = new DoublePoint(l.x - footPrints.get(i).getHeelCenter().x, l.y - footPrints.get(i).getHeelCenter().y);
			result.setValueForStep(i + 1, Math.sqrt(alVector.x * alVector.x + alVector.y * alVector.y));
		}

		result.setValueForStep(footPrints.size() - 1, 0);
		
		return result;
	}

}
