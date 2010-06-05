package org.imse.gaitrawparser.data.calc;

import java.util.ArrayList;
import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.metric.MetricCalculator;
import org.imse.gaitrawparser.data.metric.MetricResult;
import org.imse.gaitrawparser.data.metric.StepLengthCalculator;

public class MainCalc {

	private static List<MetricCalculator> calculators = new ArrayList<MetricCalculator>();
	
	static {
		calculators.add(new StepLengthCalculator());
	}
	
	public static List<MetricResult> calculate(List<FootPrint> footPrints) {
		List<MetricResult> result = new ArrayList<MetricResult>();
		for (MetricCalculator c : calculators) {
			result.add(c.calculate(footPrints));
		}
		return result;
	}
	
}
