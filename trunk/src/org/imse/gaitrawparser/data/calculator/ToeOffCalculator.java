package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class ToeOffCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerFootPrintResult result = new PerFootPrintResult("Toe off", "See Gatrite Document 4.4");
		
		for (int i = 0; i < footPrints.size(); i++) {
			result.setValueForFootPrint(i, footPrints.get(i).getToeOff());
		}
		
		return result;
	}

}

