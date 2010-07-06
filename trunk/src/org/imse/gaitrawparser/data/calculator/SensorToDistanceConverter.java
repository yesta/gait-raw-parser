package org.imse.gaitrawparser.data.calculator;

public class SensorToDistanceConverter {

	public static Double convertSensorUnitToCentimeters(Double value) {
		if (value == null) {
			return null;
		}
		// TODO Reicht das, wenn man das einfach mit 1.27 multipliziert????
		return value * 1.27;
	}
	
}
