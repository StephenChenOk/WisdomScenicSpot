package com.chen.fy.wisdomscenicspot.beans;

public class ViewPointInfo {

    private String name;
    private String address;
    private int number;
    private double distance;

    private double temperature;  //温度
    private double humidity;     //湿度
    private double visibility;   //能见度
    private boolean rainfall;     //降雨


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public boolean isRainfall() {
        return rainfall;
    }

    public void setRainfall(boolean rainfall) {
        this.rainfall = rainfall;
    }
}
