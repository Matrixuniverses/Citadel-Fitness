package seng202.group2.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity {
    private SimpleStringProperty activityName;
    private SimpleStringProperty activityType;
    private SimpleDoubleProperty totalTime;
    private SimpleDoubleProperty totalDistance;
    private SimpleDateFormat startDate = new SimpleDateFormat("01/01/1998");
    private ArrayList<DataPoint> activityData = new ArrayList<DataPoint>();

    public Activity(String activityName){
        this.activityName = new SimpleStringProperty(activityName);
        this.activityType = new SimpleStringProperty("Default");
        this.totalTime = new SimpleDoubleProperty(10);
        this.totalDistance = new SimpleDoubleProperty(3.5);
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

    public ArrayList<DataPoint> getActivityData() {
        return activityData;
    }

    public void setActivityList(ArrayList<DataPoint> activityData) {
        this.activityData = activityData;
    }

    public Date getDate() {
        Date date = activityData.get(0).getDate();
        return date;
    }

}
