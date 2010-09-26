package org.imse.gaitrawparser.data.calculation;

import java.util.List;

import org.imse.gaitrawparser.data.input.FootPrint;

public class StanceCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Stance", "");
		
		PerGaiteCycleResult timeResult = (PerGaiteCycleResult) (new CycleTimeCalculator()).calculate(footPrints);
		
		for (int i = 0; i < footPrints.size(); i++) {
			r.setValueForCycle(i, footPrints.get(i).getLastContact() - footPrints.get(i).getFirstContact());
		}
		r.setUnit("sec");
		return r;
	}

}
