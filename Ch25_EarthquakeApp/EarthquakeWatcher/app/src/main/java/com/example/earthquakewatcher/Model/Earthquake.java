package com.example.earthquakewatcher.Model;

public class Earthquake {
    private double magnitude;
    private String place;
    private long time;
    private String detailLink;
    private String type;
    private double lat;
    private double lon;

    public Earthquake() {

    }

    public Earthquake(double magnitude, String place, long time, String detailLink, String type, double lat, double lon) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
