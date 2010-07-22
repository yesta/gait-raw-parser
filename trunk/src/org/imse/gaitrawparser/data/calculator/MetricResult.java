package org.imse.gaitrawparser.data.calculator;

public class MetricResult {
	
	private String description;
	private String name;
	private String unit;
	
	public MetricResult(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}
	
}
