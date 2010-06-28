package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class CycleTimeCalculator implements MetricCalculator{

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Cycle Time", "");
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == footPrints.size() - 1 || i == footPrints.size() - 2) {
				r.setValueForCycle(i, null, null);
				continue;
			}
			r.setValueForCycle(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getFirstContact(), null);
		}
		
		return r;
	}

}
