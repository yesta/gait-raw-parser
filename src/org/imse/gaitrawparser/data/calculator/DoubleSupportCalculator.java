package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class DoubleSupportCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {

		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Double Support", "");
		
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == 0 || i == footPrints.size() - 1 || i == footPrints.size() - 2) {
				r.setValueForCycle(i, null);
				continue;
			}
			double ds1 = footPrints.get(i - 1).getLastContact() - footPrints.get(i).getFirstContact();
			double ds2 = footPrints.get(i).getLastContact() - footPrints.get(i + 1).getFirstContact();
			double ds3 = footPrints.get(i + 1).getLastContact() - footPrints.get(i + 2).getFirstContact();

			r.setValueForCycle(i, ds1 + ds2);
		}
		
		r.setUnit("sec");
		return r;
		
	}

}
