package org.imse.gaitrawparser.data.calculator;

import org.imse.gaitrawparser.data.PressurePoint.Foot;

public abstract class IndexedResult extends MetricResult {

	public IndexedResult(String name, String description) {
		super(name, description);
	}
	
	public abstract int getIndicesCount();
	
	public abstract Foot getFootForIndex(int index);

}
