package org.imse.gaitrawparser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.data.FileParser;
import org.imse.gaitrawparser.data.PressurePoint;
import org.imse.gaitrawparser.viewer.WalkCanvas;

public class Main {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.pack();
		shell.setSize(1000, 400);
		shell.open();
		WalkCanvas canvas = new WalkCanvas(shell, SWT.BORDER);
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Open Raw");
		String selected = fd.open();
		canvas.setPressurePoints(FileParser.parseFile(selected));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
