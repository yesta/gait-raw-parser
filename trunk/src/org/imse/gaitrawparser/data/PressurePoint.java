package org.imse.gaitrawparser.data;

public class PressurePoint {
	public enum Foot {
		Right, Left
	}

	private double time;
	private int x;
	private int y;
	private int pressure;
	private int objectNumber;
	private Foot foot;
	private boolean partOfFoot;

	public void setPartOfFoot(boolean partOfFoot) {
		this.partOfFoot = partOfFoot;
	}

	public boolean isPartOfFoot() {
		return partOfFoot;
	}

	public void setFoot(Foot foot) {
		this.foot = foot;
	}

	public Foot getFoot() {
		return foot;
	}

	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}

	public int getObjectNumber() {
		return objectNumber;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getPressure() {
		return pressure;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getTime() {
		return time;
	}

}
