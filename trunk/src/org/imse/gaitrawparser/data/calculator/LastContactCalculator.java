package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class LastContactCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerFootPrintResult result = new PerFootPrintResult("Last Contact", "When the last sensor is activated for a footprint.");
		
		for (int i = 0; i < footPrints.size(); i++) {
			result.setValueForFootPrint(i, footPrints.get(i).getLastContact());
		}
		
		return result;
	}

}
