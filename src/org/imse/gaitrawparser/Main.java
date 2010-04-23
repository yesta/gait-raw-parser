package org.imse.gaitrawparser;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.viewer.shells.TimeWindowManager;
import org.imse.gaitrawparser.viewer.shells.WalkWindowManager;

public class Main {
	public static void main(String[] args) {
		Display display = new Display();
		
		final Shell walkWindow = new Shell(display);
		final WalkWindowManager walkWindowManager = new WalkWindowManager(walkWindow);

		
		while (!walkWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
