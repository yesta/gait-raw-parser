package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class FirstHeelContactCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerFootPrintResult result = new PerFootPrintResult("First Heel Contact", "See Gatrite Document 4.2");
		
		for (int i = 0; i < footPrints.size(); i++) {
			result.setValueForFootPrint(i, footPrints.get(i).getFirstHeelContact());
		}
		
		return result;
	}

}

