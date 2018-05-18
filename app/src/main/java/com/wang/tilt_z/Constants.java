package com.wang.tilt_z;

/**
 * Created by zyw on 2017-12-08.
 * Enum saving the frequently used factors
 */

public enum Constants {

    THRESHOLD(8.0F), FILTER_FACTOR_ACCELERATION(0.8F), FILTER_FACTOR_NOICE(0.4F),
    THRESHOLD_SHAKE_DURATION(1000L), GRAPH_SAMPLE_NUMBER(Integer.MAX_VALUE);

    private float value;

    Constants(float value) {
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }
}
