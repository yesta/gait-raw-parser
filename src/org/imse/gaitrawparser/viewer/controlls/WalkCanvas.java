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
import org.imse.gaitrawparser.data.FootPrint;
import org.imse.gaitrawparser.data.PressurePoint;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class WalkCanvas extends Canvas implements PaintListener {

	private List<PressurePoint> points;
	private int width;
	private int height;
	private int scale = 5;
	private Color[] colors;
	private int stepsCount = 0;
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
			if (i > stepsCount) {
				break;
			}
			i++;
			gc.setBackground(getColorForPoint(p, e.display));
			gc.fillRectangle(p.getX() * (scale), p.getY() * scale, scale, scale);
		}

		gc.setLineStyle(SWT.LINE_SOLID);
		gc.setForeground(new Color(e.display, 0, 0, 0));
		double scaleD = (double) scale;
		for (FootPrint fp : footPrints) {
			gc.drawLine((int) (fp.getA().x * scaleD), (int) (fp.getA().y * scaleD), (int) (fp.getG().x * scaleD), (int) (fp.getG().y * scaleD));
		}
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
	
	public void setStepsCount(int count) {
		this.stepsCount  = count;
		
		redraw();
	}

}
