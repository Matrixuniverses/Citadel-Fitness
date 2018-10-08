package seng202.group2.analysis;

import javafx.scene.chart.XYChart;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;
import java.util.ArrayList;

public class GraphGenerator {


    /**
     * Creates an XYChart.series object containing distance over time data for an inputted activity providing that the
     * activity has not be been manually entered in and therefore has existing datapoints.
     * @param activity The activity object that the XYChart.Series data will derived from
     * @return an XYChart.Series containing distance over time data for the given activity.
     */
    public static XYChart.Series createTimeSeries(Activity activity) {
        double time = 0;
        double distance = 0;

        // Adding each Data Point into the chart for the Time axis
        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            distance += dataPoint.getDistanceDelta();
            series.getData().add(new XYChart.Data(time, distance));
            series.setName(activity.getActivityName());
        }

        return series;
    }

    /**
     * Creates an XYChart.series object containing heart rate over time data for an inputted activity providing that the
     * activity has not be been manually entered in and therefore has existing datapoints.
     * @param activity The activity object that the XYChart.Series data will derived from
     * @return an XYChart.Series containing heart rate over time data for the given activity.
     */
    public static XYChart.Series createHeartRateTimeSeries(Activity activity){
        double time = 0;
        double heartRate;

        // Adding each Data Point into the chart for the HeartRate axis
        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta() / 60;
            heartRate = dataPoint.getHeartRate();
            series.getData().add(new XYChart.Data(time, heartRate));
            series.setName(activity.getActivityName());
        }

        return series;
    }

    /**
     * Creates an XYChart.series object containing speed over time data for an inputted activity providing that the
     * activity has not be been manually entered in and therefore has existing datapoints.
     * @param activity The activity object that the XYChart.Series data will derived from
     * @return an XYChart.Series containing speed over time data for the given activity.
     */
    public static XYChart.Series createSpeedTimeSeries(Activity activity){
        double time = 0;
        double speed;

        // Adding each Data Point into the chart for the Speed axis
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

    /**
     * Creates an XYChart.series object containing Calories over time data for an inputted activity providing that the
     * activity has not be been manually entered in and therefore has existing datapoints.
     * @param activity The activity object that the XYChart.Series data will derived from
     * @return an XYChart.Series containing Calories over time data for the given activity.
     */
    public static XYChart.Series createCaloriesTimeSeries(Activity activity){
        double time = 0;
        double calories = 0;

        // Adding each Data Point into the chart for the Calories burnt axis
        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()){
            time += dataPoint.getTimeDelta() / 60;
            calories += 10;
            series.getData().add(new XYChart.Data(time, calories));
            series.setName(activity.getActivityName());
        }
        return series;
    }

    /**
     * Creates an XYChart.series object containing Altitude over time data for an inputted activity providing that the
     * activity has not be been manually entered in and therefore has existing datapoints.
     * @param activity The activity object that the XYChart.Series data will derived from
     * @return an XYChart.Series containing Altitude over time data for the given activity.
     */
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

    private static XYChart.Data createDataPoint(Activity activity) {
        String name;
        double distance;

        name = activity.getActivityName().substring(0, Math.min(activity.getActivityName().length(), 11));
        distance = activity.getTotalDistance();
        return new XYChart.Data(name, distance);
    }

    /**
     * Creates an XYChart.Series for a bar chart displaying up to 10 of the most recent activities completed by the user
     * as a total distance per activity bar graph.
     * @param activities the list of activities to be displayed on the graph.
     * @return an XYChart.Series object containing the appropriate data for a recent activities combined total distance
     * per activity graph.
     */
    public static XYChart.Series createRecentActivitySeries(ArrayList<Activity> activities) {
        XYChart.Series series = new XYChart.Series();
        int count = 0;
        for (Activity activity : activities) {
            series.getData().add(createDataPoint(activity));
            count++;
        }
        String holder = " ";
        while (count < 10) {
            series.getData().add(new XYChart.Data(holder, 0));
            count++;
            holder += " ";
        }
        return series;
    }
}
