package org.imse.gaitrawparser.data.calculation;

import java.util.List;

import org.imse.gaitrawparser.data.input.FootPrint;

public interface MetricCalculator {

	public MetricResult calculate(List<FootPrint> footPrints);
	
}
