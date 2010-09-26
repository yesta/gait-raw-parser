package org.imse.gaitrawparser.data.calculation;

public class PerWalkResult extends MetricResult {

    private double value;
    
    public PerWalkResult(String name, String description) {
        super(name, description);
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
    
    public String toString() {
        return "Caculator: " + getName() + ", Description: " + getDescription() + "\n" + "\tValue per Walk: " + value + "\n";
    }
}
