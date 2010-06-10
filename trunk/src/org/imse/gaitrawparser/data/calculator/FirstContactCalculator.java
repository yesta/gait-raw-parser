package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class FirstContactCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerFootPrintResult result = new PerFootPrintResult("First Contact", "When the first sensor is activated for a footprint.");
		
		for (int i = 0; i < footPrints.size(); i++) {
			result.setValueForFootPrint(i, footPrints.get(i).getFirstContact());
		}
		
		return result;
	}

}
