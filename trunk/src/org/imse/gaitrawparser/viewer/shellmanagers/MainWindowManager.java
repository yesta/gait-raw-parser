package org.imse.gaitrawparser.viewer.shellmanagers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.imse.gaitrawparser.data.calculation.MainCalc;
import org.imse.gaitrawparser.data.calculation.MetricCalculator;
import org.imse.gaitrawparser.data.calculation.MetricResult;
import org.imse.gaitrawparser.data.calculation.PerFootPrintResult;
import org.imse.gaitrawparser.data.calculation.PerGaiteCycleResult;
import org.imse.gaitrawparser.data.input.FileParser;
import org.imse.gaitrawparser.data.input.FileParserResult;
import org.imse.gaitrawparser.data.input.FileParserResultType;
import org.imse.gaitrawparser.data.input.FootPrint;
import org.imse.gaitrawparser.data.input.PressurePoint;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;
import org.imse.gaitrawparser.data.output.CSVGenerator;

public class MainWindowManager {	
	private Shell window;
	private Button calculateParameters;
	private Slider pressurePointsSlider;
	private Label pressurePointsScrollValueLabel;
	private Slider footPrintsSlider;
	private Label footPrintsScrollValueLabel;
	
	private FileParserResult fileParserResult;

	private WalkCanvasWindowManager canvasWindowManager;
	private TextOutputWindowManager outputWindowManager;

	public MainWindowManager(Shell window) {
		this.window = window;
		window.setText("GaitRawParser");
		
		window.setLayout(new GridLayout());
		
		final Composite buttonComposit = new Composite(window, SWT.NONE);
	    GridLayout gridLayout1 = new GridLayout();
	    gridLayout1.numColumns = 2;
	    buttonComposit.setLayout(gridLayout1);
	    
	    final Button loadWalkButton = new Button(buttonComposit, SWT.PUSH);
	    loadWalkButton.setText("Load walk from raw txt file...");
	    loadWalkButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadWalk();
			}
				
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadWalk();
			}
		});
	    
	    final Button convertFolderButton = new Button(buttonComposit, SWT.PUSH);
	    convertFolderButton.setText("Convert folder with raw txt files...");
	    convertFolderButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				convertFolder();
			}
				
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				convertFolder();
			}
		});
	    
		
		final Composite pressurePointscrollComposit = new Composite(window, SWT.NONE);
	    GridLayout gridLayout2 = new GridLayout();
	    gridLayout2.numColumns = 3;
	    pressurePointscrollComposit.setLayout(gridLayout2);
		

		final Label pressurePointsScrollTextLabel = new Label(pressurePointscrollComposit, SWT.LEFT);
		pressurePointsScrollTextLabel.setText("Scroll through pressure points: ");
	    
		pressurePointsSlider = new Slider(pressurePointscrollComposit, SWT.HORIZONTAL);
		pressurePointsSlider.setEnabled(false);
		pressurePointsSlider.setIncrement(1);
		pressurePointsSlider.setMinimum(0);
		pressurePointsScrollValueLabel = new Label(pressurePointscrollComposit, SWT.LEFT);
		pressurePointsScrollValueLabel.setText("Pressure Point: 0000000000");
		
		pressurePointsSlider.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int selection = pressurePointsSlider.getSelection() - 1;
				pressurePointsScrollValueLabel.setText("Pressure Point: " + Integer.toString(selection));
				canvasWindowManager.getWalkCanvas().setPressurePointsCount(selection);
			}
		});
		
		final Composite footPrintscrollComposit = new Composite(window, SWT.NONE);
	    GridLayout gridLayout3 = new GridLayout();
	    gridLayout3.numColumns = 3;
	    footPrintscrollComposit.setLayout(gridLayout3);
		

		final Label footPrintsScrollTextLabel = new Label(footPrintscrollComposit, SWT.LEFT);
		footPrintsScrollTextLabel.setText("Scroll through foot prints: ");
	    
		footPrintsSlider = new Slider(footPrintscrollComposit, SWT.HORIZONTAL);
		footPrintsSlider.setEnabled(false);
		footPrintsSlider.setMinimum(0);
		footPrintsSlider.setIncrement(1);
		footPrintsScrollValueLabel = new Label(footPrintscrollComposit, SWT.LEFT);
		footPrintsScrollValueLabel.setText("Foot Print: 0000000000");
		
		footPrintsSlider.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int selection = footPrintsSlider.getSelection() - 1;
				footPrintsScrollValueLabel.setText("Foot Print: " + Integer.toString(selection));
				canvasWindowManager.getWalkCanvas().setFootPrintsCount(selection);
			}
		});
		
		calculateParameters = new Button(window, SWT.PUSH);
		calculateParameters.setText("Calculate Parameters");
		calculateParameters.setEnabled(false);
		calculateParameters.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				calculateParameters();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				calculateParameters();
			}
		});
		
		
		window.open();
		window.pack();
		window.setSize(600, 200);
	}
	
	public void setCanvasWindowManager(WalkCanvasWindowManager m) {
		canvasWindowManager = m;
	}
	
	public void setOutputWindowManager(TextOutputWindowManager m) {
		outputWindowManager = m;
	}
	
	private void calculateParameters() {
		if (fileParserResult == null || fileParserResult.getResultType() != FileParserResultType.ResultOk) {
			return;
		}
		for (FootPrint p : fileParserResult.getFootPrints()) {
			p.calculate();
		}
		outputWindowManager.setResult(MainCalc.calculate(fileParserResult.getFootPrints()));
		canvasWindowManager.getWalkCanvas().setData(fileParserResult.getPressurePoints(), fileParserResult.getFootPrints(), fileParserResult.getLenX(), fileParserResult.getLenY());
	}
	
	private void reset() {
		fileParserResult = null;
		calculateParameters.setEnabled(false);
		pressurePointsSlider.setEnabled(false);
		pressurePointsScrollValueLabel.setText("Pressure Point: 0000000000");
		footPrintsSlider.setEnabled(false);
		footPrintsScrollValueLabel.setText("Foot Print: 0000000000");
		outputWindowManager.setText("");
		canvasWindowManager.getWalkCanvas().reset();
	}
	
	private void convertFolder() {
		
		String selectedFolder = null;

		
		reset();

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
	    	System.out.println("Parsing: " + inputFilename);
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
			
			FileParserResult r = FileParser.parseFile(inputPath);
			if (r.getResultType() != FileParserResultType.ResultOk) {
				log.append("Skipped file [" + inputFilename + "] because of [" + r.getResultType() +
						"] foot print index [" + r.getFootPrintProblemIndex() + "].\n");
				continue;
			}
			
			for (FootPrint p : r.getFootPrints()) {
				p.calculate();
			}
			
			String result = CSVGenerator.getCSVString(patientId, walk, MainCalc.calculate(r.getFootPrints()));
			System.out.println("Result obtained for: " + inputFilename);
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

	    outputWindowManager.setText(log.toString());
		
	}

	private void loadWalk() {

		String selected = null;

		reset();
		
		if (selected == null) {
			FileDialog fd = new FileDialog(window, SWT.OPEN);
			fd.setText("Open Raw");
			selected = fd.open();
			if (selected == null) {
				return;
			}
		}
		fileParserResult = FileParser.parseFile(selected);
		if (fileParserResult.getResultType() != FileParserResultType.ResultOk) {
			MessageBox messageBox = new MessageBox(window, SWT.ICON_ERROR);
			messageBox.setMessage("Couldn't correctly parse patient because of [" + fileParserResult.getResultType() +
					"] foot print index [" + fileParserResult.getFootPrintProblemIndex() + "].\n" +
					"You can only view the walk. You can not calculate parameters.");
			messageBox.open();
		} else {
			calculateParameters.setEnabled(true);
			
		}
		pressurePointsSlider.setEnabled(true);
		pressurePointsSlider.setMaximum(fileParserResult.getPressurePoints().size() + 10);
		pressurePointsSlider.setSelection(-1);
		pressurePointsScrollValueLabel.setText("Pressure Point: -1");
		
		footPrintsSlider.setEnabled(true);
		footPrintsSlider.setMaximum(fileParserResult.getFootPrints().size() + 10);
		footPrintsSlider.setSelection(-1);
		footPrintsScrollValueLabel.setText("Foot Print: -1");
		
		canvasWindowManager.getWalkCanvas().setData(fileParserResult.getPressurePoints(), fileParserResult.getFootPrints(), fileParserResult.getLenX(), fileParserResult.getLenY());
	}
}
