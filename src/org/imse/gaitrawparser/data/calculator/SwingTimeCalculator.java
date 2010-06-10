package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public class SwingTimeCalculator implements MetricCalculator {

    @Override
    public MetricResult calculate(List <FootPrint> footPrints) {
        PerStepResult result = new PerStepResult("Swing Time", "See Gatrite Document 4.20");
        
        for(int i = 0; i < footPrints.size() - 2; i++) {
            result.setValueForStep(i, footPrints.get(i + 2).getFirstContact() - footPrints.get(i).getLastContact());
        }
        
        result.setValueForStep(footPrints.size() - 2, 0);
        return result;
    }

}
