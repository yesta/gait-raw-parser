package org.imse.gaitrawparser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.viewer.shellmanagers.MainWindowManager;
import org.imse.gaitrawparser.viewer.shellmanagers.TextOutputWindowManager;
import org.imse.gaitrawparser.viewer.shellmanagers.WalkCanvasWindowManager;

public class Main {
	public static void main(String[] args) {
		Display display = new Display();
		

		final Shell outputWindow = new Shell(display, SWT.TITLE | SWT.RESIZE);
		final TextOutputWindowManager outputWindowManager = new TextOutputWindowManager(outputWindow);
		
		final Shell canvasWindow = new Shell(display, SWT.TITLE | SWT.RESIZE);
		final WalkCanvasWindowManager canvasWindowManager = new WalkCanvasWindowManager(canvasWindow);
		
		final Shell mainWindow = new Shell(display, SWT.CLOSE | SWT.RESIZE);
		final MainWindowManager mainWindowManager = new MainWindowManager(mainWindow);

		mainWindowManager.setCanvasWindowManager(canvasWindowManager);
		mainWindowManager.setOutputWindowManager(outputWindowManager);
		
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
