package org.imse.gaitrawparser.data.calculator;
import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;


public class AmbulationTimeCalculator implements MetricCalculator {

    public MetricResult calculate(List <FootPrint> footPrints) {
        PerWalkResult result = new PerWalkResult("Ambulation Time", "See Gatrite Document 4.8");
        result.setValue(footPrints.get(footPrints.size()-1).getFirstContact() - footPrints.get(0).getFirstContact());
        return result;
    }

}
