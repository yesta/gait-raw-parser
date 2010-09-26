package org.imse.gaitrawparser.data.output;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.NumberFormatter;

import org.imse.gaitrawparser.data.calculation.IndexedResult;
import org.imse.gaitrawparser.data.calculation.MetricResult;
import org.imse.gaitrawparser.data.calculation.PerFootPrintResult;
import org.imse.gaitrawparser.data.calculation.PerGaiteCycleResult;
import org.imse.gaitrawparser.data.calculation.PerWalkResult;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public class CSVGenerator {

	public static String getCSVString(String patientId, String walk, List<MetricResult> results) {
		StringBuffer result = new StringBuffer();
		result.append(patientId + "," + walk + "\n");
		
		int count = -1;
		
		List<IndexedResult> indexedResults = new ArrayList<IndexedResult>();
		List<PerWalkResult> perWalkResults = new ArrayList<PerWalkResult>();
		
		for (MetricResult r : results) {
			if (r instanceof IndexedResult) {
				IndexedResult ir = (IndexedResult) r;
				if (count < ir.getIndicesCount()) {
					count = ir.getIndicesCount();
				}
				indexedResults.add(ir);
			} else if (r instanceof PerWalkResult) {
				perWalkResults.add((PerWalkResult) r);
			}
		}
		
		result.append("Step,Foot");
		for (PerWalkResult r : perWalkResults) {
			result.append("," + r.getName() + " (" + r.getUnit() + ")");
		}
		for (IndexedResult r : indexedResults) {
			result.append("," + r.getName() + " (" + r.getUnit() + ")");
		}
		result.append("\n");
		
		for (int i = 0; i < count; i++) {
			result.append(i + "," + (indexedResults.size() > 0 ? indexedResults.get(0).getFootForIndex(i).toString() : "")); 
			if (i == 0) {
				for (PerWalkResult r : perWalkResults) {
					result.append("," + r.getValue());
				}
			}  else {
				result.append(",");
			}
			for (IndexedResult r : indexedResults) {
				if (r instanceof PerGaiteCycleResult) {
					Double value  = ((PerGaiteCycleResult) r).getAbsValueForStep(i);
					result.append("," + (value == null ? "" : value));
				} else if (r instanceof PerFootPrintResult) {
					Double value = ((PerFootPrintResult) r).getValueForFootPrint(i);
					result.append("," + (value == null ? "" : value));
				}
			}
			result.append("\n");
		}
		
		return result.toString();
	}
	
	/*public static String getCSVString(String patientId, String walk, List<MetricResult> results) {
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
				for (int i = 0; i < gr.getIndicesCount(); i++) {
					if (gr.getFootForIndex(i) == Foot.Left) {
						leftValue = gr.getAbsValueForStep(i);
						rightValue = null;
					} else if (gr.getFootForIndex(i) == Foot.Right) {
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
				for (int i = 0; i < pr.getIndicesCount(); i++) {
					if (pr.getFootForIndex(i) == Foot.Left) {
						leftValue = pr.getValueForStep(i);
						rightValue = null;
					} else if (pr.getFootForIndex(i) == Foot.Right) {
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
	}*/
	
	private static String formatDouble(Double d) {
		return Double.toString(d);
	}
	
}
