package seng202.group2.model;

import java.util.Date;

public class DataPoint {
    private int id;
    private Date date;
    private int heartRate;
    private double latitude;
    private double longitude;
    private double altitude;
    private double distanceDelta;   // Geodetic distance from last point
    private double timeDelta;
    private double speedDelta;

    /** Constructor for a new Datapoint, each of the basic measurements are required
     *
     * @param date Date/ Time of the measurements
     * @param heartRate Heart rate measured at ate
     * @param latitude WSG84 latitude measured at date
     * @param longitude WSG84 longitude measured at date
     * @param altitude Vertical distance from sea level at date
     */
    public DataPoint(Date date, int heartRate, double latitude, double longitude, double altitude) {
        this.date = date;
        this.heartRate = heartRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public DataPoint(Date date, int heartRate, double latitude, double longitude, double altitude, double distanceDelta, double timeDelta) {
        this.date = date;
        this.heartRate = heartRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.distanceDelta = distanceDelta;
        this.timeDelta = timeDelta;
        this.speedDelta = this.distanceDelta / this.timeDelta;
    }




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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
