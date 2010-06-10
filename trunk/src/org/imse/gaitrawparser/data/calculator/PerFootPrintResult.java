package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.Map;

public class PerFootPrintResult extends MetricResult {

	public PerFootPrintResult(String name, String description) {
		super(name, description);
	}
	
	private Map<Integer, Double> values = new HashMap<Integer, Double>();
	private int footPrintCont;
	
	public void setValueForFootPrint(int index, double value) {
		if (footPrintCont < index + 1) {
			footPrintCont = index + 1;
		}
		values.put(index, value);
	}
	
	public double getValueForFootPrint(int index) {
		return values.get(index);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("Caculator: " + getName() + ", Description: " + getDescription() + "\n");
		for (int i = 0; i < footPrintCont; i++) {
			b.append("\tFootPrint " + i + ": " + getValueForFootPrint(i) + "\n");
		}
		return b.toString();
	}

}
