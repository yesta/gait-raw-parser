package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class SwingCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Swing", "");
		
		PerGaiteCycleResult timeResult = (PerGaiteCycleResult) (new CycleTimeCalculator()).calculate(footPrints);
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == footPrints.size() - 1 || i == footPrints.size() - 2) {
				r.setValueForCycle(i, null, null);
				continue;
			}
			double val = footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getLastContact();
			Double cTime = timeResult.getAbsValueForStep(i);
			r.setValueForCycle(i, val, (cTime == null ? null : val / cTime));
		}
		return r;
	}

}
