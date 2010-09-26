package org.imse.gaitrawparser.data.calculation;

import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public abstract class IndexedResult extends MetricResult {

	public IndexedResult(String name, String description) {
		super(name, description);
	}
	
	public abstract int getIndicesCount();
	
	public abstract Foot getFootForIndex(int index);

}
