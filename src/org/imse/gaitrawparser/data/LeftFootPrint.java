package org.imse.gaitrawparser.data;

import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class LeftFootPrint extends FootPrint {

	public LeftFootPrint(int maxX, int maxY) {
		super(maxX, maxY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Foot getFoot() {
		return Foot.Left;
	}

}
