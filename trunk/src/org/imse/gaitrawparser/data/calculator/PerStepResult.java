package org.imse.gaitrawparser.data.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class PerStepResult extends MetricResult {

	private int stepCount = -1;
	private List<FootPrint> prints;
	
	public PerStepResult(List<FootPrint> prints, String name, String description) {
		super(name, description);
		this.prints = prints;
	}

	private Map<Integer, Double> values = new HashMap<Integer, Double>();
	
	public int getStepsCount() {
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
	
	public Foot getFootForStep(int index) {
		return prints.get(index).getFoot();
	}
	
	public double getAvg() {
		double avg = 0.0;
		double avgCount = 0.0;
		for (int i = 0; i < stepCount; i++) {
			if (getValueForStep(i) != null) {
				avg += getValueForStep(i);
				avgCount++;
			}
		}
		return avg / avgCount;
	}
	
	public double getAvgLeft() {
	    double avg = 0.0;
        double avgCount = 0.0;
        for (int i = 0; i < stepCount; i++) {
            if (getValueForStep(i) != null && prints.get(i).getPressurePoints().get(0).getFoot().equals(Foot.Left)) {
                avg += getValueForStep(i);
                avgCount++;
            }
        }
        return avg / avgCount;
	}
	
	public double getAvgRight() {
	    double avg = 0.0;
        double avgCount = 0.0;
        for (int i = 0; i < stepCount; i++) {
            if (getValueForStep(i) != null && prints.get(i).getPressurePoints().get(0).getFoot().equals(Foot.Right)) {
                avg += getValueForStep(i);
                avgCount++;
            }
        }
        return avg / avgCount;
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
