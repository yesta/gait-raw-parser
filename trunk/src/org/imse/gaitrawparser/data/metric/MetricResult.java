package org.imse.gaitrawparser.data.metric;

public class MetricResult {
	
	private String description;
	private String name;
	
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
	
}
