package org.imse.gaitrawparser.viewer.shellmanagers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.imse.gaitrawparser.data.calculation.MetricResult;
import org.imse.gaitrawparser.data.calculation.PerFootPrintResult;
import org.imse.gaitrawparser.data.calculation.PerGaiteCycleResult;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public class TextOutputWindowManager {

	private Shell window;
	private StyledText styledText;

	public TextOutputWindowManager(Shell window) {
		this.window = window;
		window.setText("Text Output");

		window.setLayout(new GridLayout());

		styledText = new StyledText(window, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);

		styledText.setLayoutData(new GridData(GridData.FILL_BOTH));

		Font font = new Font(window.getDisplay(), "Courier New", 12, SWT.NORMAL);
		styledText.setFont(font);
		styledText.setEditable(false);

		window.open();
		window.pack();
		window.setSize(600, 500);
	}

	public void setResult(List<MetricResult> results) {
		List<PerGaiteCycleResult> gaitRes = new ArrayList<PerGaiteCycleResult>();
		List<PerFootPrintResult> stepRes = new ArrayList<PerFootPrintResult>();
		List<MetricResult> otherRes = new ArrayList<MetricResult>();
		for (MetricResult r : results) {
			if (r instanceof PerGaiteCycleResult) {
				gaitRes.add((PerGaiteCycleResult) r);
				/*
				 * } else if (r instanceof PerStepResult) {
				 * stepRes.add((PerStepResult) r);
				 */
			} else {
				otherRes.add(r);
			}
		}
		StringBuffer buff = new StringBuffer();

		if (gaitRes.size() > 0) {
			buff.append("Gait Cycle Values:\n\n");
			int stepCount = gaitRes.get(0).getIndicesCount();
			for (int i = 0; i < stepCount; i++) {
				buff.append("\tCycle that starts with Step: " + i + ", "
						+ gaitRes.get(0).getFootForIndex(i) + "\n");
				for (PerGaiteCycleResult g : gaitRes) {
					buff.append("\t\t" + g.getName() + "\n\t\t\tAbs: "
							+ g.getAbsValueForStep(i) + "\n\t\t\tRel: "
							+ g.getRelValueForStep(i) + "\n");
				}
			}
			buff.append("\tAvgs:\n");
			for (PerGaiteCycleResult g : gaitRes) {
				buff.append("\t\t" + g.getName() + "\n\t\t\tAbs: "
						+ g.getAvgAbs() + "\n\t\t\tRel: " + g.getAvgRel()
						+ "\n");
				buff.append("\t\t\tRel Left: " + g.getAvgRel(Foot.Left)
						+ "\n\t\t\tRel Right: " + g.getAvgRel(Foot.Right)
						+ "\n");
				buff.append("\n\t\t\tAbs Left: " + g.getAvgAbs(Foot.Left)
						+ "\n\t\t\tAbs Right: " + g.getAvgAbs(Foot.Right)
						+ "\n\n");
			}
		}
		if (stepRes.size() > 0) {
			// TODO
		}

		for (MetricResult m : otherRes) {
			buff.append("\n" + m.toString());
		}

		styledText.setText(buff.toString());
	}

	public void setText(String text) {
		styledText.setText(text);
	}
}
