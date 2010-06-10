package org.imse.gaitrawparser.data.calculator;

import java.util.List;

import org.imse.gaitrawparser.data.FootPrint;

public interface MetricCalculator {

	public MetricResult calculate(List<FootPrint> footPrints);
	
}
