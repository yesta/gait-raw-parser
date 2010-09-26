package org.imse.gaitrawparser.data.input;


import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.input.PressurePoint.Foot;

public class LeftFootPrint extends FootPrint {

	public LeftFootPrint(int lenX, int lenY) {
		super(lenX, lenY);
	}

	@Override
	public Foot getFoot() {
		return Foot.Left;
	}

	@Override
	protected Line getInnerLine(Point p1, Point p2) {
		if (p1.x > p2.x) {
			Point temp = p1;
			p1 = p2;
			p2 = temp;
		}
		if (p1.y > p2.y) {
			return new Line(p1.x + 1, p1.y + 1, p2.x + 1, p2.y + 1);
		} else {
			return new Line(p1.x, p1.y + 1, p2.x, p2.y + 1);
		}
	}

	@Override
	protected boolean isInnerPoint(int x, int y) {
		if (y == lenY - 1 || (y + 1 < lenY && pixel[x][y + 1] == false)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Point getTargetDirectionAG() {
		return new Point(10, -1);
	}

    @Override
    protected boolean isOuterPoint(int x, int y){
        if (y == 0 || (y - 1 >= 0 && pixel[x][y - 1] == false)) {
            return true;
        } else {
        	return false;
        }
    }

    @Override
    protected Line getOuterLine(Point p1, Point p2) {
		if (p1.x > p2.x) {
			Point temp = p1;
			p1 = p2;
			p2 = temp;
		}
        if(p1.y > p2.y){
            return new Line(p1.x, p1.y, p2.x, p2.y);
        } else {
            return new Line(p1.x + 1, p1.y, p2.x + 1, p2.y);
        }
    }

    @Override
    protected Point getTargetDirectionLR() {
		return new Point(10, -3);
    }



	

}
