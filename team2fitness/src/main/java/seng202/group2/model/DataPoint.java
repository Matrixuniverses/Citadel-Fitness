package seng202.group2.model;

import javax.xml.bind.annotation.XmlValue;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model class datapoint stores information about a recoded point in time
 */
public class DataPoint {
    private int id;
    private Date date;
    private int heartRate;
    private double latitude;
    private double longitude;
    private double altitude;
    private double distanceDelta;   // Geodetic distance from last point
    private double timeDelta;

    /** Constructor for a new Datapoint, each of the basic measurements are required
     *
     * @param date Date/ Time of the measurements
     * @param heartRate Heart rate measured at date
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

    /**
     * This returns the Geodetic distance from last data point
     * @return returns double distanceDelta
     */
    public double getDistanceDelta() {
        return distanceDelta;
    }

    /**
     * This sets the Geodetic distance between last and the current data points
     * @param distanceDelta Geometric distance between points
     */
    public void setDistanceDelta(double distanceDelta) {
        this.distanceDelta = distanceDelta;
    }

    /**
     * This returns the time between the last and current data points as a double
     * @return double timeDelta
     */
    public double getTimeDelta() {
        return timeDelta;
    }

    /**
     * This sets the time between the last and current data points
     * @param timeDelta time between data points
     */
    public void setTimeDelta(double timeDelta) {
        this.timeDelta = timeDelta;
    }

    /**
     * This returns the date of the data pointas a
     * @return Date date
     */
    public Date getDate() {
        return date;
    }

    /**
     * This sets the date/time for the measurements of the data point
     * @param date date/time for the data measurements
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * returns the heart rate measured at point date as an integer
     * @return int heartRate
     */
    public int getHeartRate() {
        return heartRate;
    }

    /**
     * This sets the Heart rate measured at the data point as an integer
     * @param heartRate heart rate measured at point as an integer value
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * This returns the WSG84 latitude measured at date with type double
     * @return returns double latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * This returns the WSG84 longitude measured at date with type double
     * @return returns double longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * This returns the Vertical distance from sea level at date with type double
     * @return returns double altitude
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * This returns the id of the DataPoint as an Integer
     * @return returns int id
     */
    public int getId() {
        return id;
    }

    /**
     * This sets the id of the data point as an Integer
     * @param id id of the DataPoint
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This function is called by a CellValueFactory in runtime and does not appear in code.
     * @return Date containing a neicely formatted date
     */
    public String getFormattedDate() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this.date);
    }
}
