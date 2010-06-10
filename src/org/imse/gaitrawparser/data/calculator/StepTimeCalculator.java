package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.DoubleLine;
import org.imse.gaitrawparser.data.DoublePoint;
import org.imse.gaitrawparser.data.FootPrint;

public class StepTimeCalculator implements MetricCalculator {

    @Override
    public MetricResult calculate(List < FootPrint > footPrints){
        PerStepResult result = new PerStepResult("Step Time", "See Gatrite Document 4.5");
        
        result.setValueForStep(0, 0);

        for (int i = 0; i < footPrints.size() - 2; i++) {
            result.setValueForStep(i + 1, footPrints.get(i+1).getFirstContact() - footPrints.get(i).getFirstContact());
        }

        result.setValueForStep(footPrints.size() - 1, 0);
        
        return result;
    }

}
