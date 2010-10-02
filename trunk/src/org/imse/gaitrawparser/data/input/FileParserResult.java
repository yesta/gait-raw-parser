package org.imse.gaitrawparser.data.input;

import java.util.List;

public class FileParserResult {

	private int lenY;
	private int lenX;
	private FileParserResultType resultType;
	private List<PressurePoint> pressurePoints;
	private List<FootPrint> footPrints;
	private int footPrintProblemIndex = -1;
	
	public void setLenY(int lenY) {
		this.lenY = lenY;
	}
	
	public int getLenY() {
		return lenY;
	}

	public void setLenX(int lenX) {
		this.lenX = lenX;
	}

	public int getLenX() {
		return lenX;
	}

	public void setResultType(FileParserResultType resultType) {
		this.resultType = resultType;
	}

	public FileParserResultType getResultType() {
		return resultType;
	}

	public void setPressurePoints(List<PressurePoint> pressurePoints) {
		this.pressurePoints = pressurePoints;
	}

	public List<PressurePoint> getPressurePoints() {
		return pressurePoints;
	}

	public void setFootPrints(List<FootPrint> footPrints) {
		this.footPrints = footPrints;
	}

	public List<FootPrint> getFootPrints() {
		return footPrints;
	}

	public void setFootPrintProblemIndex(int footPrintProblemIndex) {
		this.footPrintProblemIndex = footPrintProblemIndex;
	}

	public int getFootPrintProblemIndex() {
		return footPrintProblemIndex;
	}

	
}
