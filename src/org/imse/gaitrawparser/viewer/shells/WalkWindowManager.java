package org.imse.gaitrawparser.viewer.shells;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.data.FileParser;
import org.imse.gaitrawparser.data.PressurePoint;
import org.imse.gaitrawparser.data.calculator.MainCalc;
import org.imse.gaitrawparser.viewer.controlls.WalkCanvas;
import org.imse.gaitrawparser.viewer.listeners.ScrollListener;

public class WalkWindowManager {
	
	private WalkCanvas canvas;
	private Shell window;
	private TimeWindowManager timeWindowManager;
	private CalcWindowManager calcWindowManager;
	private boolean walkLoaded = false;

	public WalkWindowManager(Shell aWindow) {
		this.window = aWindow;
		window.setLayout(new FillLayout());
		canvas = new WalkCanvas(window, SWT.BORDER);

		Menu menuBar = new Menu(window, SWT.BAR);
		MenuItem walkMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		walkMenuHeader.setText("Walk");
		Menu walkMenu = new Menu(window, SWT.DROP_DOWN);
		walkMenuHeader.setMenu(walkMenu);
		MenuItem loadWalk = new MenuItem(walkMenu, SWT.PUSH);
		loadWalk.setText("Load walk...");
		loadWalk.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadWalk();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadWalk();
			}
		});
		window.setMenuBar(menuBar);
		

		final Shell timeWindow = new Shell(window);
		timeWindowManager = new TimeWindowManager(timeWindow);
		timeWindowManager.setScrollListener(new ScrollListener() {
			
			@Override
			public void onScrolled(int newVaule) {
				if (walkLoaded) {
					canvas.setStepsCount(newVaule);
				}
			}
		});
		
		final Shell calcWindow = new Shell(window);
		calcWindowManager = new CalcWindowManager(calcWindow);
		
		window.open();
		window.pack();
		window.setSize(1000, 400);
	}
	
	private void loadWalk() {

		String selected = null;

	    //selected = "/Users/matej/Uni/IDP/Raw397.Txt";


		if (selected == null) {
			FileDialog fd = new FileDialog(window, SWT.OPEN);
			fd.setText("Open Raw");
			selected = fd.open();
			if (selected == null) {
				return;
			}
		}
		FileParser.parseFile(selected);
		List<PressurePoint> points = FileParser.getPoints();
		timeWindowManager.setStepsCount(points.size());
		canvas.setData(points, FileParser.getFootPrints(), FileParser.getLenX(), FileParser.getLenY());
		/*Point size = canvas.calculateDimensionsFromPressurePoints();
		window.setSize(size);
		canvas.setSize(size);*/
		walkLoaded = true;
		
		calcWindowManager.setResult(MainCalc.calculate(FileParser.getFootPrints()));
	}

}
