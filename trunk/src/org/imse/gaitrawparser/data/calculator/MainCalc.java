package org.imse.gaitrawparser.data.calculator;

import java.util.ArrayList;
import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class MainCalc {

	private static List<MetricCalculator> calculators = new ArrayList<MetricCalculator>();
	
	static {
		calculators.add(new StepLengthCalculator());
		calculators.add(new FirstContactCalculator());
		calculators.add(new FirstHeelContactCalculator());
		calculators.add(new LastContactCalculator());
		calculators.add(new ToeOffCalculator());
		calculators.add(new StepTimeCalculator());
		calculators.add(new StrideTimeCalculator());
		calculators.add(new AmbulationTimeCalculator());
		calculators.add(new SwingTimeCalculator());
		calculators.add(new DoubleSupportCalculator());
		calculators.add(new SideswingCalculator());
	}
	
	public static List<MetricResult> calculate(List<FootPrint> footPrints) {
		List<MetricResult> result = new ArrayList<MetricResult>();
		for (MetricCalculator c : calculators) {
			result.add(c.calculate(footPrints));
		}
		return result;
	}
	
}
