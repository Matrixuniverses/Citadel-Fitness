package seng202.group2.analysis;

import javafx.scene.chart.XYChart;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

import java.util.ArrayList;
import java.util.Date;

public class GraphGenerator {

    public static XYChart.Series createTimeSeries(Activity activity) {
        double time = 0;
        double distance = 0;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            distance += dataPoint.getDistanceDelta();
            series.getData().add(new XYChart.Data(time, distance));
            series.setName(activity.getActivityName());
        }

        return series;
    }

    public static XYChart.Series createHeartRateTimeSeries(Activity activity){
        double time = 0;
        double heartRate;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            heartRate = dataPoint.getHeartRate();
            series.getData().add(new XYChart.Data(time, heartRate));
            series.setName(activity.getActivityName());
        }

        return series;
    }


    public static XYChart.Series createSpeedTimeSeries(Activity activity){
        double time = 0;
        double speed;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            if (dataPoint.getTimeDelta() != 0) {
                speed = dataPoint.getDistanceDelta() / dataPoint.getTimeDelta();
            } else {
                speed = 0;
            }
            series.getData().add(new XYChart.Data(time, speed));
            series.setName(activity.getActivityName());
        }

        return series;
    }

    public static XYChart.Series createCaloriesTimeSeries(Activity activity){
        double time = 0;
        double calories = 0;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()){
            time += dataPoint.getTimeDelta() / 60;
            calories += 10;
            series.getData().add(new XYChart.Data(time, calories));
            series.setName(activity.getActivityName());
        }
        return series;
    }

    public static XYChart.Series createAltitudeTimeSeries(Activity activity) {
        double time = 0;
        double altitude = 0;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            altitude = dataPoint.getAltitude();
            series.getData().add(new XYChart.Data(time, altitude));
            series.setName(activity.getActivityName());
        }
        return series;
    }

    public static XYChart.Data createDataPoint(Activity activity) {
        String date;
        double distance;

        date = activity.getShortFormattedDate();
        distance = activity.getTotalDistance();

        return new XYChart.Data(date, distance);
    }

    public static XYChart.Series createRecentActivitySeries(ArrayList<Activity> activities) {
        XYChart.Series series = new XYChart.Series();
        for (Activity activity : activities) {
            series.getData().add(createDataPoint(activity));
        }
        return series;
    }
}
