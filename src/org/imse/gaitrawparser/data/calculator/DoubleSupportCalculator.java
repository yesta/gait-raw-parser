package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class DoubleSupportCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Double Support", "");
		
		PerGaiteCycleResult timeResult = (PerGaiteCycleResult) (new CycleTimeCalculator()).calculate(footPrints);
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == 0 || i == footPrints.size() - 1) {
				r.setValueForCycle(i, null, null);
				continue;
			}
			double ds1 = footPrints.get(i - 1).getLastContact() - footPrints.get(i).getFirstContact();
			double ds2 = footPrints.get(i).getLastContact() - footPrints.get(i + 1).getFirstContact();

			Double ds = ds1 + ds2;
			
			Double cTime = timeResult.getAbsValueForStep(i);
			
			if (cTime != null) {
				r.setValueForCycle(i, ds, ds / cTime);
			} else {
				r.setValueForCycle(i, ds, null);
			}
		}
		
		return r;
		
	}

}