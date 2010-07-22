package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imse.gaitrawparser.data.FootPrint;

public class CycleTimeCalculator implements MetricCalculator{

	@Override
	public MetricResult calculate(List<FootPrint> footPrints) {
		PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Cycle Time", "");
		Map<Integer, Double> resultMap = getCycleTimesForFootPrints(footPrints);
		for (int i = 0; i < footPrints.size(); i++) {
			r.setValueForCycle(i, resultMap.get(i));
		}
		r.setUnit("sec");
		return r;
	}
	
	public static Map<Integer, Double> getCycleTimesForFootPrints(List<FootPrint> footPrints) {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		for (int i = 0; i < footPrints.size(); i++) {
			if (i == footPrints.size() - 1 || i == footPrints.size() - 2) {
				result.put(i, null);
				continue;
			}
			result.put(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getFirstContact());
		}
		return result;
	}

}
