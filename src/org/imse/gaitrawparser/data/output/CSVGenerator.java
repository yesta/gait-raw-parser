package org.imse.gaitrawparser.data.output;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.NumberFormatter;

import org.imse.gaitrawparser.data.PressurePoint.Foot;
import org.imse.gaitrawparser.data.calculator.MetricResult;
import org.imse.gaitrawparser.data.calculator.PerGaiteCycleResult;
import org.imse.gaitrawparser.data.calculator.PerStepResult;
import org.imse.gaitrawparser.data.calculator.PerWalkResult;

public class CSVGenerator {

	public static String getCSVString(String patientId, String walk, List<MetricResult> results) {
		StringBuffer result = new StringBuffer();
		result.append(patientId + "," + walk + "\n");
		List<String> lines = new ArrayList<String>();
		for (MetricResult r : results) {
			if (r instanceof PerGaiteCycleResult) {
				PerGaiteCycleResult gr = (PerGaiteCycleResult) r;
				Double leftValue;
				Double rightValue;
				String leftLine = gr.getName() + " L," + gr.getUnit() + ",";
				String rightLine = gr.getName() + " R," + gr.getUnit() + ",";
				for (int i = 0; i < gr.getCyclesCount(); i++) {
					if (gr.getFootForStep(i) == Foot.Left) {
						leftValue = gr.getAbsValueForStep(i);
						rightValue = null;
					} else if (gr.getFootForStep(i) == Foot.Right) {
						rightValue = gr.getAbsValueForStep(i);
						leftValue = null;
					} else {
						throw new RuntimeException("This can't happen...");
					}
					if (leftValue != null) {
						leftLine += formatDouble(leftValue);
					} else {
						leftLine += " ";
					}
					leftLine += ",";
					if (rightValue != null) {
						rightLine += formatDouble(rightValue);
					} else {
						rightLine += " ";
					}
					rightLine += ",";
				}
				lines.add(leftLine.substring(0, leftLine.length() - 1));
				lines.add(rightLine.substring(0, rightLine.length() - 1));
			} else if (r instanceof PerWalkResult) {
				PerWalkResult wr = (PerWalkResult) r;
				String line = wr.getName() + "," + wr.getUnit() + "," + formatDouble(wr.getValue());
				lines.add(line);
			} else if (r instanceof PerStepResult) {
				PerStepResult pr = (PerStepResult) r;
				Double leftValue;
				Double rightValue;
				String leftLine = pr.getName() + " L," + pr.getUnit() + ",";
				String rightLine = pr.getName() + " R," + pr.getUnit() + ",";
				for (int i = 0; i < pr.getStepsCount(); i++) {
					if (pr.getFootForStep(i) == Foot.Left) {
						leftValue = pr.getValueForStep(i);
						rightValue = null;
					} else if (pr.getFootForStep(i) == Foot.Right) {
						rightValue = pr.getValueForStep(i);
						leftValue = null;
					} else {
						throw new RuntimeException("This can't happen...");
					}
					if (leftValue != null) {
						leftLine += formatDouble(leftValue);
					} else {
						leftLine += "";
					}
					leftLine += ",";
					if (rightValue != null) {
						rightLine += formatDouble(rightValue);
					} else {
						rightLine += "";
					}
					rightLine += ",";
				}
				lines.add(leftLine.substring(0, leftLine.length() - 1));
				lines.add(rightLine.substring(0, rightLine.length() - 1));
			}
			lines.add("");
		}
		
		for (String line : lines) {
			result.append(",," + line + "\n");
		}
		
		return result.toString();
	}
	
	private static String formatDouble(Double d) {
		return Double.toString(d);
	}
	
}
