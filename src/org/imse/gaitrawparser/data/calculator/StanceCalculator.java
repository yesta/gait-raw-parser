package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class StanceCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Stance", "");
		
		PerGaiteCycleResult timeResult = (PerGaiteCycleResult) (new CycleTimeCalculator()).calculate(footPrints);
		
		for (int i = 0; i < footPrints.size(); i++) {
			r.setValueForCycle(i, footPrints.get(i).getLastContact() - footPrints.get(i).getFirstContact());
		}
		return r;
	}

}
