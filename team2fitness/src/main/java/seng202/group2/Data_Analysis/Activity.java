package seng202.group2.Data_Analysis;

import java.util.ArrayList;

public class Activity {
    private String activityTitle;
    private String activityType;
    private int totalTime;
    private int totalDistance;
    private ArrayList<DataPoint> activityData = new ArrayList<DataPoint>();

    public Activity(String activityName){
        this.activityTitle = activityName;
    }

    public void addDataPoint(DataPoint toAdd) {
        activityData.add(toAdd);
    }
    public void setActivityTitle(String title) {
        this.activityTitle = title;
    }
    public String getName(){
        return this.activityTitle;
    }
}
