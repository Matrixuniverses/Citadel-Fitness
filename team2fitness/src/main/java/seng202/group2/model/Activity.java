package seng202.group2.model;

import java.util.ArrayList;

public class Activity {
    private String activityName;
    private String activityType;
    private int totalTime;
    private double totalDistance;
    private ArrayList<DataPoint> activityData = new ArrayList<DataPoint>();

    public Activity(String activityName){
        this.activityName = activityName;
    }

    public void addDataPoint(DataPoint toAdd) {
        activityData.add(toAdd);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setActivityName(String title) {
        this.activityName = title;
    }
    public String getName(){
        return this.activityName;
    }
    public ArrayList<DataPoint> getActivityData(){
        return this.activityData;
    }
}
