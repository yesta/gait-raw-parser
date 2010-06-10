package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.Map;

public class PerStepResult extends MetricResult {

	private int stepCount = -1;
	
	public PerStepResult(String name, String description) {
		super(name, description);
	}

	private Map<Integer, Double> values = new HashMap<Integer, Double>();
	
	public void setValueForStep(int stepIndex, double value) {
		if (stepCount < stepIndex + 1) {
			stepCount = stepIndex + 1;
		}
		values.put(stepIndex, value);
	}
	
	public double getValueForStep(int index) {
		return values.get(index);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("Caculator: " + getName() + ", Description: " + getDescription() + "\n");
		for (int i = 0; i < stepCount; i++) {
			b.append("\tStep " + i + ": " + getValueForStep(i) + "\n");
		}
		return b.toString();
	}
	
}
