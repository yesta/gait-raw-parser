package org.imse.gaitrawparser.data.calculation;

import java.util.ArrayList;
import java.util.List;

import org.imse.gaitrawparser.data.input.FootPrint;

public class MainCalc {

	private static List<MetricCalculator> calculators = new ArrayList<MetricCalculator>();
	
	static {
		calculators.add(new AmbulationTimeCalculator());
		calculators.add(new StepTimeCalculator());
		calculators.add(new CycleTimeCalculator());
		calculators.add(new StepLengthCalculator());
		calculators.add(new SingleSupportCalculator());
		calculators.add(new StanceCalculator());
		calculators.add(new SwingCalculator());
		calculators.add(new DoubleSupportCalculator());
	}
	
	public static List<MetricResult> calculate(List<FootPrint> footPrints) {
		List<MetricResult> result = new ArrayList<MetricResult>();
		for (MetricCalculator c : calculators) {
			result.add(c.calculate(footPrints));
		}
		return result;
	}
	
}
