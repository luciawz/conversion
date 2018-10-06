package com.example.demo;

import java.util.Date;

/**
 * Created by lucia on 2018/10/6.
 */
public class Data {
    private Date datetime;

    private String latitude;

    private String longtitude; //经度

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
