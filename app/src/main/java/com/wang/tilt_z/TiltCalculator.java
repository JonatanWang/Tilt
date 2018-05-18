package com.wang.tilt_z;

/**
 * Created by zyw on 2017-12-08.
 * Class to calculate the tilt grade
 */

public class TiltCalculator {

    private int grade;
    private float prevX, prevY, prevZ;
    private float sensorValueX, sensorValueY, sensorValueZ;
    private SimpleFilter filterX, filterY, filterZ;

    public TiltCalculator() {
        this.prevX = 0f; this.prevY = 0f; this.prevZ = 0f;
        filterX = new SimpleFilter(Constants.FILTER_FACTOR_NOICE.getValue(), getPrevX());
        filterY = new SimpleFilter(Constants.FILTER_FACTOR_NOICE.getValue(), getPrevY());
        filterZ = new SimpleFilter(Constants.FILTER_FACTOR_NOICE.getValue(), getPrevZ());
    }

    // Use simple filter to calculate the tilt grade
    public int getGrade() {

        float x = filterX.filter(this.getSensorValueX());
        float y = filterY.filter(this.getSensorValueY());
        float z = filterZ.filter(this.getSensorValueZ());

        this.setGrade((int) Math.toDegrees(Math.atan2(z, y)));

        this.setPrevX(x);
        this.setPrevY(y);
        this.setPrevZ(z);

        return grade;
    }

    private void setGrade(int grade) {
        this.grade = grade;
    }

    private float getPrevX() {
        return prevX;
    }

    private void setPrevX(float prevX) {
        this.prevX = prevX;
    }

    private float getPrevY() {
        return prevY;
    }

    private void setPrevY(float prevY) {
        this.prevY = prevY;
    }

    private float getPrevZ() {
        return prevZ;
    }

    private void setPrevZ(float prevZ) {
        this.prevZ = prevZ;
    }

    private float getSensorValueX() {
        return sensorValueX;
    }

    public void setSensorValueX(float sensorValueX) {
        this.sensorValueX = sensorValueX;
    }

    private float getSensorValueY() {
        return sensorValueY;
    }

    public void setSensorValueY(float sensorValueY) {
        this.sensorValueY = sensorValueY;
    }

    private float getSensorValueZ() {
        return sensorValueZ;
    }

    public void setSensorValueZ(float sensorValueZ) {
        this.sensorValueZ = sensorValueZ;
    }
}
