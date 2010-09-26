package org.imse.gaitrawparser.data.calculation;

import java.util.List;

import org.imse.gaitrawparser.data.input.DoubleLine;
import org.imse.gaitrawparser.data.input.DoublePoint;
import org.imse.gaitrawparser.data.input.FootPrint;

public class StepTimeCalculator implements MetricCalculator {

    @Override
    public MetricResult calculate(List <FootPrint> footPrints){
        PerGaiteCycleResult r = new PerGaiteCycleResult(footPrints, "Step Time", "See Gatrite Document 4.5");
        
        for (int i = 0; i < footPrints.size(); i++) {
        	if (i == footPrints.size() - 2 || i == footPrints.size() - 1) {
				r.setValueForCycle(i, null);
				continue;
			}

			r.setValueForCycle(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i + 1).getFirstContact());
        }
        r.setUnit("sec");
        return r;
    }

}
