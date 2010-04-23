package org.imse.gaitrawparser.viewer.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.imse.gaitrawparser.viewer.listeners.ScrollListener;

public class TimeWindowManager {

	private Shell window;
	private Slider slider;
	private Label label;
	private ScrollListener scrollListener;
	
	public TimeWindowManager(Shell window) {
		this.window = window;
		
		window.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		slider = new Slider(window, SWT.HORIZONTAL);
		slider.setEnabled(false);
		slider.setMinimum(1);
		label = new Label(window, SWT.LEFT);
		label.setText("S: 0000000000");
		
		slider.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int selection = slider.getSelection();
				label.setText("S: " + Integer.toString(selection));
				if (scrollListener != null) {
					scrollListener.onScrolled(selection);
				}
			}
		});
		
		window.open();
		window.pack();
		window.setSize(300, 50);
	}
	
	public void setScrollListener(ScrollListener listener) {
		this.scrollListener = listener;
	}
	
	public void setStepsCount(int count) {
		if (count > 0) {
			slider.setEnabled(true);
		}
		slider.setMaximum(count);
	}
	
}
