package seng202.group2.model;

import java.util.Date;

public class DataPoint {
    private Date date;
    private int heartRate;
    private double latitude;
    private double longitude;
    private double altitude;
    private double distanceDelta;
    private double timeDelta;

    public double getDistanceDelta() {
        return distanceDelta;
    }

    public void setDistanceDelta(double distanceDelta) {
        this.distanceDelta = distanceDelta;
    }

    public double getTimeDelta() {
        return timeDelta;
    }

    public void setTimeDelta(double timeDelta) {
        this.timeDelta = timeDelta;
    }

    public DataPoint(Date date, int heartRate, double latitude, double longitude, double altitude){
        this.date = date;
        this.heartRate = heartRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;


    }

    public DataPoint(Boolean malformed) {

    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public int getHeartRate() { return heartRate; }

    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getAltitude() { return altitude; }

    public void setAltitude(double altitude) { this.altitude = altitude; }
}
