package com.wang.tilt_z;

/**
 * Created by Zhengyu Wang on 2016-12-05.
 * Email: zhengyuw@kth.se
 * Class to judge if phone is shaking
 */

public class ShakeValidator {

    private boolean isShaking;

    private float prevX, prevY, prevZ;
    private float sensorValueX, sensorValueY, sensorValueZ;
    private SimpleFilter filterX, filterY, filterZ;

    public ShakeValidator() {
        this.isShaking = false;
        this.prevX = 0f; this.prevY = 0f; this.prevZ = 0f;
        this.filterX = new SimpleFilter(Constants.FILTER_FACTOR_ACCELERATION.getValue(), getPrevX());
        this.filterY = new SimpleFilter(Constants.FILTER_FACTOR_ACCELERATION.getValue(), getPrevY());
        this.filterZ = new SimpleFilter(Constants.FILTER_FACTOR_ACCELERATION.getValue(), getPrevZ());
    }

    public boolean isShaking() {
        return isShaking;
    }

    public void setShaking(boolean shaking) {
        isShaking = shaking;
    }

    public float getPrevX() {
        return prevX;
    }

    public void setPrevX(float prevX) {
        this.prevX = prevX;
    }

    public float getPrevY() {
        return prevY;
    }

    public void setPrevY(float prevY) {
        this.prevY = prevY;
    }

    public float getPrevZ() {
        return prevZ;
    }

    public void setPrevZ(float prevZ) {
        this.prevZ = prevZ;
    }

    public float getSensorValueX() {
        return sensorValueX;
    }

    public void setSensorValueX(float sensorValueX) {
        this.sensorValueX = sensorValueX;
    }

    public float getSensorValueY() {
        return sensorValueY;
    }

    public void setSensorValueY(float sensorValueY) {
        this.sensorValueY = sensorValueY;
    }

    public float getSensorValueZ() {
        return sensorValueZ;
    }

    public void setSensorValueZ(float sensorValueZ) {
        this.sensorValueZ = sensorValueZ;
    }

    /** Use simple filter to judge if a shake reaches the threshold,
     *  if so, set the isShaking to true;
     *  otherwise set the isShaking to false
     */
    public void validate() {

        float x = filterX.filter(this.getSensorValueX());
        float y = filterY.filter(this.getSensorValueY());
        float z = filterZ.filter(this.getSensorValueZ());

        float[] accValuesDifference = new float[3];
        accValuesDifference[0] = this.getSensorValueX() - x;
        accValuesDifference[1] = this.getSensorValueY() - y;
        accValuesDifference[2] = this.getSensorValueZ() - z;

        setPrevX(x);
        setPrevY(y);
        setPrevZ(z);

        // If one of the x, y, z directions, certain acceleration difference is larger than threshold
        if (Math.abs(accValuesDifference[0]) > Constants.THRESHOLD.getValue()
                || Math.abs(accValuesDifference[1]) > Constants.THRESHOLD.getValue()
                || Math.abs(accValuesDifference[2]) > Constants.THRESHOLD.getValue()) {
            setShaking(true);
        } else {
            setShaking(false);
        }
    }
}