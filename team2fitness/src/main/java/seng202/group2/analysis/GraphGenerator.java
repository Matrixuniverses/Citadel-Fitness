package seng202.group2.analysis;

import javafx.scene.chart.XYChart;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

public class GraphGenerator {

    public static XYChart.Series createTimeSeries(Activity activity) {
        double time = 0;
        double distance = 0;

        XYChart.Series series = new XYChart.Series();
        for (DataPoint dataPoint : activity.getActivityData()) {
            time += dataPoint.getTimeDelta();
            distance += dataPoint.getDistanceDelta();
            series.getData().add(new XYChart.Data(time, distance));
            series.setName(activity.getActivityName());
        }

        return series;
    }
}
