package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class PerGaiteCycleResult extends MetricResult {

	private Map<Integer, Double> absValues =
		new HashMap<Integer, Double>();
	private Map<Integer, Double> relValues =
		new HashMap<Integer, Double>();
	private int cycleCount = -1;
	private List<FootPrint> prints;
	
	public PerGaiteCycleResult(List<FootPrint> prints, String name, String description) {
		super(name, description);
		this.prints = prints;
	}
	
	public int getCyclesCount() {
		return cycleCount;
	}
	
	public void setValueForCycle(int cycleStartFootIndex, Double absValue, Double relValue) {
		if (cycleCount < cycleStartFootIndex + 1) {
			cycleCount  = cycleStartFootIndex + 1;
		}
		absValues.put(cycleStartFootIndex, absValue);
		relValues.put(cycleStartFootIndex, relValue);
	}
	
	public Double getAbsValueForStep(int index) {
		return absValues.get(index);
	}
	
	public Double getRelValueForStep(int index) {
		return relValues.get(index);
	}
	
	public Foot getFootForStep(int index) {
		return prints.get(index).getFoot();
	}
	
	public Double getAvgRel() {
		double avg = 0.0;
		double avgCount = 0.0;
		for (int i = 0; i < cycleCount; i++) {
			if (getRelValueForStep(i) != null) {
				avg += getRelValueForStep(i);
				avgCount++;
			}
		}
		if (avgCount > 0) {
			return avg / avgCount;
		} else {
			return null;
		}
	}
	
	public Double getAvgAbs() {
		double avg = 0.0;
		double avgCount = 0.0;
		for (int i = 0; i < cycleCount; i++) {
			if (getAbsValueForStep(i) != null) {
				avg += getAbsValueForStep(i);
				avgCount++;
			}
		}
		if (avgCount > 0) {
			return avg / avgCount;
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("Gait Caculator: " + getName() + ", Description: " + getDescription() + "\n");
		for (int i = 0; i < cycleCount; i++) {
			b.append("\tCycle with start foot " + i + ", " + prints.get(i).getFoot() + "\n\t\tAbs:" + getAbsValueForStep(i) + "\n\t\tRel:" + getRelValueForStep(i) + "\n");
		}
		b.append("\tAvg:\n\t\tRel:" + getAvgRel() + "\n\t\tAbs:" + getAvgAbs() + "\n");
		return b.toString();
	}

}
