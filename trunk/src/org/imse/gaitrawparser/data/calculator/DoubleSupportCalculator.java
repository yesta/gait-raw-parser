package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class DoubleSupportCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerStepResult result = new PerStepResult("Double Support", "See Gatrite Document 4.13");
	
		for (int i = 0; i < footPrints.size() - 1; i++) {
			result.setValueForStep(i, footPrints.get(i).getToeOff() - footPrints.get(i + 1).getFirstHeelContact());
		}
		
		return result;
	}

}
