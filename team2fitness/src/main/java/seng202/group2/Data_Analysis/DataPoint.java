package seng202.group2.Data_Analysis;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataPoint {
    private Date date;
    private int heartRate;
    private double latitude;
    private double longitude;
    private double altitude;
    private double distanceDelta;
    private double timeDelta;

    public DataPoint(String timeStamp, int heartRate, double latitude, double longitude, double altitude){
        DateFormat newDf = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.ENGLISH);
        try {
            this.date = newDf.parse(timeStamp);
            this.heartRate = heartRate;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        } catch (ParseException parserException){
            System.err.println(parserException);
        }
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
