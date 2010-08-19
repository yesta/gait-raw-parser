package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class PerStepResult extends IndexedResult {

	private int stepCount = -1;
	private List<FootPrint> prints;
	
	public PerStepResult(List<FootPrint> prints, String name, String description) {
		super(name, description);
		this.prints = prints;
	}

	private Map<Integer, Double> values = new HashMap<Integer, Double>();
	
	public int getIndicesCount() {
		return stepCount;
	}
	
	public void setValueForStep(int stepIndex, Double value) {
		if (stepCount < stepIndex + 1) {
			stepCount = stepIndex + 1;
		}
		values.put(stepIndex, value);
	}
	
	public Double getValueForStep(int index) {
		return values.get(index);
	}
	
	public Foot getFootForIndex(int index) {
		return prints.get(index).getFoot();
	}
	
	public Double getAvg(Foot foot) {
		double avg = 0.0;
		double avgCount = 0.0;
		for (int i = 0; i < stepCount; i++) {
			if (getValueForStep(i) != null) {
				if (foot != null && !prints.get(i).getFoot().equals(foot)) {
					continue;
				}
				avg += getValueForStep(i);
				avgCount++;
			}
		}
		if (avgCount > 0) {
			return avg / avgCount;
		} else {
			return null;
		}
	}
	
	public Double getAvg() {
		return getAvg(null);
	}
	
	public Double getAvgLeft() {
	    return getAvg(Foot.Left);
	}
	
	public Double getAvgRight() {
	    return getAvg(Foot.Right);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("Caculator: " + getName() + ", Description: " + getDescription() + "\n");
		for (int i = 0; i < stepCount; i++) {
			b.append("\tStep that starts with foot " + i + ", " + prints.get(i).getFoot() + "\n\t\t" + getValueForStep(i) + "\n");
		}
		b.append("\tAvg:\n\t\t" + getAvg());
		b.append("\n\tAvg Left:\n\t\t" + getAvgLeft());
		b.append("\n\tAvg Right:\n\t\t" + getAvgRight());
		return b.toString();
	}
	
}
