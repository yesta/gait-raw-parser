package org.imse.gaitrawparser.data.calculation;

import java.util.List;

import org.imse.gaitrawparser.data.input.FootPrint;

public class SwingCalculator implements MetricCalculator {

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Swing", "");
		
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == footPrints.size() - 1 || i == footPrints.size() - 2) {
				r.setValueForCycle(i, null);
				continue;
			}
			r.setValueForCycle(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getLastContact());
		}
		r.setUnit("sec");
		return r;
	}

}
