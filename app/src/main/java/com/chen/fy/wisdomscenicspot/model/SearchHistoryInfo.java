package com.chen.fy.wisdomscenicspot.model;

import org.litepal.crud.LitePalSupport;

public class SearchHistoryInfo extends LitePalSupport{

    /**
     * id表示
     */
    private int id;
    /**
     * 搜索内容
     */
    private String title;
    /**
     * 搜索时间
     */
    private long date;

    /**
     * 经度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}