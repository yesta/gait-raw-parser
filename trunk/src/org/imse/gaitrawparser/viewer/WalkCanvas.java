package org.imse.gaitrawparser.viewer;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.imse.gaitrawparser.data.PressurePoint;

public class WalkCanvas extends Canvas implements PaintListener {

	private List<PressurePoint> points;
	private int width;
	private int height;
	private int scale = 3;

	public WalkCanvas(Composite parent, int style) {
		super(parent, style);
		addPaintListener(this);
	    setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setPressurePoints(List<PressurePoint> points) {
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
	
	public void setDimensionsFromPressurePoints() {
		if (points != null) {
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
	}

	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		if (points == null) {
			return;
		}
		gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK)); 
		for (PressurePoint p : points) {
			gc.fillRectangle(p.getX() * scale, p.getY() * scale, scale, scale);
		}
	}

}
