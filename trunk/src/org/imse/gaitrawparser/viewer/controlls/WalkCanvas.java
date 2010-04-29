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
import org.imse.gaitrawparser.data.PressurePoint;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class WalkCanvas extends Canvas implements PaintListener {

	private List<PressurePoint> points;
	private int width;
	private int height;
	private int scale = 3;
	private Color[] colors;
	private int stepsCount = 0;
	private int maxX;
	private int maxY;

	public WalkCanvas(Composite parent, int style) {
		super(parent, style);
		addPaintListener(this);
	    setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setPressurePoints(List<PressurePoint> points, int maxX, int maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
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
	
	public Point calculateDimensionsFromPressurePoints() {
		if (points == null) {
			throw new RuntimeException("Pressurepoints have to be set first.");
		}
		height = 0;
		width = 0;
		for (PressurePoint p : points) {
			if (p.getX() > width) {
				width = p.getX();
			}
			if (p.getY() > height) {
				height = p.getY();
			}
		}
		Point result = new Point(width, height);
		return result;
	}

	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(new Color(e.display, 240, 240, 240));
		gc.fillRectangle(0, 0, (maxX + 1) * scale, (maxY + 1) * scale);
		if (points == null) {
			return;
		}
		
		int i = 1;
		for (PressurePoint p : points) {
			if (i > stepsCount) {
				return;
			}
			i++;
			gc.setBackground(getColorForPoint(p, e.display));
			gc.fillRectangle(p.getX() * (scale), p.getY() * (scale), scale, scale);
		}
	}
	
	private Color getColorForPoint(PressurePoint p, Device device) {
		if (colors == null) {
			colors = new Color[8];
			colors[0] = new Color(device, 181, 213, 254);
			colors[1] = new Color(device, 210, 157, 62);
			colors[2] = new Color(device, 65, 154, 72);
			colors[3] = new Color(device, 233, 0, 40);
			colors[4] = new Color(device, 3, 78, 145);
			colors[5] = new Color(device, 113, 60, 23);
			colors[6] = new Color(device, 0, 82, 39);
			colors[7] = new Color(device, 0, 0, 0);
		}
		//return colors[p.getPressure()];
		if (p.getFoot() == Foot.Left) {
			return colors[0];
		} else {
			return colors[1];
		}
	}
	
	public void setStepsCount(int count) {
		this.stepsCount  = count;
		
		redraw();
	}

}
