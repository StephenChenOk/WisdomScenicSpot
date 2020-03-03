package com.chen.fy.wisdomscenicspot.model;

/**
 * 每个区的天气情况
 */
public class AreaWeatherInfo {

    private String name;
    private int rainfull;
    private double temperature;
    private double humidity;
    private double pressure;
    private double visibility;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRainfull() {
        return rainfull;
    }

    public void setRainfull(int rainfull) {
        this.rainfull = rainfull;
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

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }
}