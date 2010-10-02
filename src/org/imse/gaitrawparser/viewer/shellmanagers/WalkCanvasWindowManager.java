package org.imse.gaitrawparser.viewer.shellmanagers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.data.calculation.MainCalc;
import org.imse.gaitrawparser.data.input.FileParser;
import org.imse.gaitrawparser.data.input.PressurePoint;
import org.imse.gaitrawparser.data.output.CSVGenerator;
import org.imse.gaitrawparser.viewer.controlls.WalkCanvas;

public class WalkCanvasWindowManager {
	
	private WalkCanvas canvas;
	private Shell window;
	private MainWindowManager calcWindowManager;

	public WalkCanvasWindowManager(Shell aWindow) {
		this.window = aWindow;
		window.setText("Walk Canvas");
		window.setLayout(new FillLayout());
		canvas = new WalkCanvas(window, SWT.BORDER);
		
		window.open();
		window.pack();
		window.setSize(1000, 400);
	}
	
	public WalkCanvas getWalkCanvas() {
		return canvas;
	}
	

}
