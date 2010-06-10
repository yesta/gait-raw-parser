package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class StrideTimeCalculator implements MetricCalculator {

    public MetricResult calculate(List < FootPrint > footPrints) {
        PerStepResult result = new PerStepResult("Stride Time", "See Gatrite Document 4.6");
        
        result.setValueForStep(0, 0);
        result.setValueForStep(1, 0);

        for (int i = 2; i < footPrints.size(); i++){
            result.setValueForStep(i, footPrints.get(i).getFirstContact() - footPrints.get(i - 2).getFirstContact());
        }
        return result;
    }

}
