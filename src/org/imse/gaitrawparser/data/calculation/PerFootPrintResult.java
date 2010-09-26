package org.imse.gaitrawparser.data.calculation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imse.gaitrawparser.data.input.FootPrint;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public class PerFootPrintResult extends IndexedResult {

	private int printCount = -1;
	private List<FootPrint> prints;
	
	public PerFootPrintResult(List<FootPrint> prints, String name, String description) {
		super(name, description);
		this.prints = prints;
	}

	private Map<Integer, Double> values = new HashMap<Integer, Double>();
	
	public int getIndicesCount() {
		return printCount;
	}
	
	public void setValueForFootPrint(int printIndex, Double value) {
		if (printCount < printIndex + 1) {
			printCount = printIndex + 1;
		}
		values.put(printIndex, value);
	}
	
	public Double getValueForFootPrint(int index) {
		return values.get(index);
	}
	
	public Foot getFootForIndex(int index) {
		return prints.get(index).getFoot();
	}
	
	public Double getAvg(Foot foot) {
		double avg = 0.0;
		double avgCount = 0.0;
		for (int i = 0; i < printCount; i++) {
			if (getValueForFootPrint(i) != null) {
				if (foot != null && !prints.get(i).getFoot().equals(foot)) {
					continue;
				}
				avg += getValueForFootPrint(i);
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
		for (int i = 0; i < printCount; i++) {
			b.append("\tFoot print " + i + ", " + prints.get(i).getFoot() + "\n\t\t" + getValueForFootPrint(i) + "\n");
		}
		b.append("\tAvg:\n\t\t" + getAvg());
		b.append("\n\tAvg Left:\n\t\t" + getAvgLeft());
		b.append("\n\tAvg Right:\n\t\t" + getAvgRight());
		return b.toString();
	}
	
}
