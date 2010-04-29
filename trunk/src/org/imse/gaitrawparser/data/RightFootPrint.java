package org.imse.gaitrawparser.data;

import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class RightFootPrint extends FootPrint {

	public RightFootPrint(int maxX, int maxY) {
		super(maxX, maxY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Foot getFoot() {
		return Foot.Right;
	}

}
