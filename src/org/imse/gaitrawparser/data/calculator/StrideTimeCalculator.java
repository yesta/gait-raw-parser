package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class StrideTimeCalculator implements MetricCalculator {

    public MetricResult calculate(List < FootPrint > footPrints) {
        PerStepResult result = new PerStepResult("Stride Time", "See Gatrite Document 4.6");

        for (int i = 0; i < footPrints.size() - 2; i++){
            result.setValueForStep(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getFirstContact());
        }
        result.setValueForStep(footPrints.size() - 2, 0);
        return result;
    }

}
