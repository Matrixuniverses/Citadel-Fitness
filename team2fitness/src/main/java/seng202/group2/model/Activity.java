package seng202.group2.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Date;

public class Activity {
    private int id;
    private SimpleStringProperty activityName;
    private SimpleStringProperty activityType;
    private SimpleDoubleProperty totalTime;
    private SimpleDoubleProperty totalDistance;
    private SimpleDoubleProperty averageHR;
    private Date activityDate;
    private ArrayList<DataPoint> activityData = new ArrayList<>();
    //Will add code functionality later
    private boolean manualEntry = false;

    public Activity(String activityName) {
        this.activityName = new SimpleStringProperty(activityName);
        this.activityType = new SimpleStringProperty("Exercise");
        this.totalTime = new SimpleDoubleProperty(10);
        this.totalDistance = new SimpleDoubleProperty(0);
        this.averageHR = new SimpleDoubleProperty(0);
    }

    public Activity(String activityName, Date activityDate, String activityType, Double totalTime, Double totalDistance) {
        this.activityName = new SimpleStringProperty(activityName);
        this.activityType = new SimpleStringProperty(activityType);
        this.activityDate = activityDate;
        this.totalTime = new SimpleDoubleProperty(totalTime);
        this.totalDistance = new SimpleDoubleProperty(totalDistance);
        this.averageHR = new SimpleDoubleProperty(0);
        manualEntry = true;
    }


    public void addDataPoint(DataPoint toAdd) {
        activityData.add(toAdd);
    }

    public String getActivityName() {
        return activityName.get();
    }

    public SimpleStringProperty activityNameProperty() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName.set(activityName);
    }

    public String getActivityType() {
        return activityType.get();
    }

    public SimpleStringProperty activityTypeProperty() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType.set(activityType);
    }

    public double getTotalTime() {
        return totalTime.get();
    }

    public SimpleDoubleProperty totalTimeProperty() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime.set(totalTime);
    }

    public double getTotalDistance() {
        return totalDistance.get();
    }

    public SimpleDoubleProperty totalDistanceProperty() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance.set(totalDistance);
    }

    public double getAverageHR() {
        return averageHR.get();
    }

    public SimpleDoubleProperty averageHRProperty() {
        return averageHR;
    }

    public void setAverageHR(double averageHR) {
        this.averageHR.set(averageHR);
    }

    public ArrayList<DataPoint> getActivityData() {
        return activityData;
    }

    public void setActivityList(ArrayList<DataPoint> activityData) {
        this.activityData = activityData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        if (!manualEntry) {
            Date date = activityData.get(0).getDate();
            return date;
        } else {
            return activityDate;
        }
    }

}
