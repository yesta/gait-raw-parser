package org.imse.gaitrawparser.viewer.controlls;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.imse.gaitrawparser.data.calculation.SideswingCalculator;
import org.imse.gaitrawparser.data.input.DoubleLine;
import org.imse.gaitrawparser.data.input.DoublePoint;
import org.imse.gaitrawparser.data.input.FootPrint;
import org.imse.gaitrawparser.data.input.PressurePoint;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public class WalkCanvas extends Canvas implements PaintListener {

	private List<PressurePoint> points;
	private int width;
	private int height;
	private int scale = 4;
	private Color[] colors;
	private int printsCount = 0;
	private int lenX;
	private int lenY;
	private List<FootPrint> footPrints;

	public WalkCanvas(Composite parent, int style) {
		super(parent, style);
		addPaintListener(this);
	    setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setData(List<PressurePoint> points, List<FootPrint> footPrints, int lenX, int lenY) {
		this.footPrints = footPrints;
		this.lenX = lenX;
		this.lenY = lenY;
		this.points = points;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(new Color(e.display, 240, 240, 240));
		gc.fillRectangle(0, 0, lenX * scale, lenY * scale);
		if (points == null) {
			return;
		}
		
		int i = 1;
		for (PressurePoint p : points) {
			if (i > printsCount) {
				break;
			}
			i++;
			gc.setBackground(getColorForPoint(p, e.display));
			gc.fillRectangle(p.getX() * (scale), p.getY() * scale, scale, scale);
		}

		gc.setLineStyle(SWT.LINE_SOLID);
		gc.setForeground(new Color(e.display, 0, 0, 0));
		double scaleD = (double) scale;
		for (int j = 0; j < footPrints.size(); j++) {
			FootPrint fp = footPrints.get(j);
			gc.drawLine((int) (fp.getA().x * scaleD), (int) (fp.getA().y * scaleD), (int) (fp.getG().x * scaleD), (int) (fp.getG().y * scaleD));
		    gc.drawLine((int) (fp.getL().x * scaleD), (int) (fp.getL().y * scaleD), (int) (fp.getR().x * scaleD), (int) (fp.getR().y * scaleD));
		    gc.drawLine((int) (fp.getA().x * scaleD), (int) (fp.getA().y * scaleD), (int) (fp.getL().x * scaleD), (int) (fp.getL().y * scaleD));
		    gc.drawLine((int) (fp.getG().x * scaleD), (int) (fp.getG().y * scaleD), (int) (fp.getR().x * scaleD), (int) (fp.getR().y * scaleD));
		    gc.drawLine((int) (fp.getC().x * scaleD), (int) (fp.getC().y * scaleD), (int) (fp.getN().x * scaleD), (int) (fp.getN().y * scaleD));
		    gc.drawLine((int) (fp.getP().x * scaleD), (int) (fp.getP().y * scaleD), (int) (fp.getE().x * scaleD), (int) (fp.getE().y * scaleD));
		
		    if (j < footPrints.size() - 1) {
		    	FootPrint nextFP = footPrints.get(j + 1);
		    	gc.drawLine((int) (fp.getHeelCenter().x * scaleD), (int) (fp.getHeelCenter().y * scaleD), (int) (nextFP.getHeelCenter().x * scaleD), (int) (nextFP.getHeelCenter().y * scaleD));
		    }
		}
		DoubleLine axis = (new SideswingCalculator()).calculateMinimalAxis(footPrints);
		DoublePoint p1 = axis.getP1();
		DoublePoint p2 = new DoublePoint(axis.getP1().x + axis.getA().x * 400, axis.getP1().y + axis.getA().y * 400);
		gc.drawLine((int) (p1.x * scaleD), (int) (p1.y * scaleD), (int) (p2.x * scaleD), (int) (p2.y * scaleD));
		
	}
	
	private Color getColorForPoint(PressurePoint p, Device device) {
		if (colors == null) {
			colors = new Color[8];
			colors[0] = new Color(device, 200, 200, 200);
			colors[1] = new Color(device, 50, 50, 50);
			colors[2] = new Color(device, 65, 154, 72);
			colors[3] = new Color(device, 233, 0, 40);
			colors[4] = new Color(device, 3, 78, 145);
			colors[5] = new Color(device, 113, 60, 23);
			colors[6] = new Color(device, 0, 82, 39);
			colors[7] = new Color(device, 0, 0, 0);
		}
		return colors[p.getPressure()];
		/*if (p.getFoot() == Foot.Left) {
			return colors[0];
		} else {
			return colors[1];
		}*/
	}
	
	public void setPrintsCount(int count) {
		this.printsCount  = count;
		
		redraw();
	}

}
