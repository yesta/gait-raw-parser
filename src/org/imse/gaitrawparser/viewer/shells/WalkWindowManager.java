package org.imse.gaitrawparser.viewer.shells;

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
import org.eclipse.swt.widgets.Shell;
import org.imse.gaitrawparser.data.FileParser;
import org.imse.gaitrawparser.data.PressurePoint;
import org.imse.gaitrawparser.data.calculator.MainCalc;
import org.imse.gaitrawparser.data.output.CSVGenerator;
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

		MenuItem convertWalks = new MenuItem(walkMenu, SWT.PUSH);
		convertWalks.setText("Convert walks in folder...");
		convertWalks.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				convertFolder();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				convertFolder();
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
	
	private void convertFolder() {
		String selectedFolder = null;

	    //selectedFolder = "/Users/matej/Uni/IDP/KW 123";


		if (selectedFolder == null) {
			DirectoryDialog dd = new DirectoryDialog(window, SWT.OPEN);
			dd.setText("Select folder");
			selectedFolder = dd.open();
			if (selectedFolder == null) {
				return;
			}
		}
		
		File folder = new File(selectedFolder);
		selectedFolder = folder.getAbsolutePath();
		
	    File[] listOfFiles = folder.listFiles();

	    StringBuffer log = new StringBuffer();
	    
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	String inputPath = listOfFiles[i].getAbsolutePath();
	    	String inputFilename = listOfFiles[i].getName();
	    	String[] temp = inputFilename.split("\\.");
	    	if (temp.length != 2) {
	    		log.append("Skipped file [" + inputFilename + "].\n");
	    		continue;
	    	}
	    	String extension = temp[temp.length - 1].toLowerCase();
			if (!extension.equals("txt")) {
	    		log.append("Skipped file [" + inputFilename + "].\n");
				continue;
			}

			String outputFilepath = selectedFolder + "/" + temp[0] + ".csv";
			temp = temp[0].split("-");
			if (temp.length != 2) {
                log.append("Skipped file [" + inputFilename + "].\n");
                continue;
            }
			String walk = temp[1];
			temp = temp[0].split("_");
			if (temp.length != 2) {
	    		log.append("Skipped file [" + inputFilename + "].\n");
				continue;
			}
			String patientId = temp[0] + " / " + temp[1];
			
			FileParser.parseFile(inputPath);
			
			String result = CSVGenerator.getCSVString(patientId, walk, MainCalc.calculate(FileParser.getFootPrints()));
			try {
				FileWriter fwriter = new FileWriter(outputFilepath);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write(result);
				out.close();
	    		log.append("Exported patient [" + patientId + "] , walk [" + walk + "].\n");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	    }
	    
	    calcWindowManager.setText(log.toString());
		
	}

	private void loadWalk() {

		String selected = null;

	    //selected = "/Users/matej/Uni/IDP/Raw345.Txt";


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
