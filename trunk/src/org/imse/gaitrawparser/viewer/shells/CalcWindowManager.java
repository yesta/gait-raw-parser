package org.imse.gaitrawparser.viewer.shells;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.data.metric.MetricCalculator;
import org.imse.gaitrawparser.data.metric.MetricResult;
import org.imse.gaitrawparser.data.metric.PerStepResult;

public class CalcWindowManager {	
	private Shell window;
	private StyledText styledText;

	public CalcWindowManager(Shell window) {
		this.window = window;
		
		window.setLayout(new GridLayout());
		
		styledText = new StyledText(window, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		    
		styledText.setLayoutData(new GridData(GridData.FILL_BOTH));

		Font font = new Font(window.getDisplay(), "Courier New", 12, SWT.NORMAL);
		styledText.setFont(font);
		styledText.setEditable(false);
		
		window.open();
		window.pack();
		window.setSize(600, 500);
	}
	
	public void setResult(List<MetricResult> results) {
		StringBuffer b = new StringBuffer();
		for (MetricResult r : results) {
			b.append(r.toString() + "\n");
		}
		styledText.setText(b.toString());
	}
}
