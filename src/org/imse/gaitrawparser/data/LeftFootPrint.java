package org.imse.gaitrawparser.data;


import org.eclipse.swt.graphics.Point;
import org.imse.gaitrawparser.data.PressurePoint.Foot;

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
		if (p1.y > p2.y) {
			return new Line(p1.x + 1, p1.y + 1, p2.x + 1, p2.y + 1);
		} else {
			return new Line(p1.x, p1.y + 1, p2.x, p2.y + 1);
		}
	}

	@Override
	protected boolean isInnerPoint(int x, int y) {
	    //TODO y = lenY - 1
		if (y + 1 < lenY && pixel[x][y + 1] == false) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected double getTargetAngleAG() {
		return -0.1;
	}

	@Override
	protected boolean isOutsideInnerLine(double py, double yl, double yr) {
		if (yl < py + 1 || yr < py + 1) {
			return false;
		} else {
			return true;
		}
	}

    @Override
    protected boolean isOuterPoint(int x, int y){
        if(y == 0){
            return true;
        }
        else if (y - 1 >= 0 && pixel[x][y - 1] == false) {
            return true;
        }
        return false;
    }

    @Override
    protected Line getOuterLine(Point p1, Point p2) {
        if(p1.y > p2.y){
            return new Line(p1.x, p1.y, p2.x, p2.y);
        } else {
            return new Line(p1.x + 1, p1.y, p2.x + 1, p2.y);
        }
    }

    @Override
    protected double getTargetAngleLR() {
        return -0.1;
    }

    @Override
    protected boolean isOutsideOuterLine(double py, double yl, double yr){
        if(py < yl || py < yr){
           return false; 
        } else {
            return true;
        }
       
    }

	

}
