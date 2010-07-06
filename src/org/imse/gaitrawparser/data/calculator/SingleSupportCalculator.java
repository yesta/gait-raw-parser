package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class SingleSupportCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Single Support", "");
		
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == 0 || i == footPrints.size() - 1) {
				r.setValueForCycle(i, null);
				continue;
			}

			r.setValueForCycle(i, footPrints.get(i + 1).getFirstContact() - footPrints.get(i - 1).getLastContact());
		}
		return r;
	}

}
