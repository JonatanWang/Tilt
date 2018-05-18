package com.wang.tilt_z;

/**
 * Author: Anders Lindström
 */
public class SimpleFilter {

    private float currentValue;
    private float previousValue;
    private float filterFactor;

    public SimpleFilter(float filterFactor, float initialValue) {
        this.filterFactor = filterFactor;
        this.previousValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * @return filteredvalue(n) = F * filteredvalue(n-1) + (1-F) * sensorvalue(n)
     */
    public float filter(float sensorValue) {
        currentValue = filterFactor * previousValue + (1.0F - filterFactor) * sensorValue;
        previousValue = currentValue;
        return currentValue;
    }

    public float getCurrentValue() {
        return currentValue;
    }
}
