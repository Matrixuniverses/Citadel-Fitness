package seng202.group2.analysis;

import javafx.scene.chart.XYChart;
import org.junit.Before;
import org.junit.Test;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

public class GraphGeneratorTest {

    private Instant dateNow = Instant.now();
    private Activity exampleActivity = new Activity("Activity1", Date.from(dateNow), "Walk", 70.0, 11.0);

    @Before
    public void setUpTestDatabase() {

        DataPoint dp1 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(50))), 168, 9.72, 10.0, 99.0);
        DataPoint dp2 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(40))), 167, 9.76, 10.0, 98.0);
        DataPoint dp3 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(30))), 165, 9.8, 10.0, 99.0);
        DataPoint dp4 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(20))), 160, 9.9, 10.0, 100.0);
        DataPoint dp5 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 170, 10.0, 10.0, 100.0);

        exampleActivity.addDataPoint(dp1);
        exampleActivity.addDataPoint(dp2);
        exampleActivity.addDataPoint(dp3);
        exampleActivity.addDataPoint(dp4);
        exampleActivity.addDataPoint(dp5);



    }

    @Test
    public void createDistTimeSeriesGraph_OutputNotEmptyTest() {
        XYChart.Series series = GraphGenerator.createTimeSeries(exampleActivity);
        assertFalse(series.getData().isEmpty());
    }

    @Test
    public void createDistTimeSeriesGraph_OutputLengthTest() {
        XYChart.Series series = GraphGenerator.createTimeSeries(exampleActivity);
        assertEquals(5, series.getData().size());
    }

    @Test
    public void createHeartRateTimeSeriesGraph_OutputNotEmptyTest() {
        XYChart.Series series = GraphGenerator.createHeartRateTimeSeries(exampleActivity);
        assertFalse(series.getData().isEmpty());
    }

    @Test
    public void createHeartRateTimeSeriesGraph_OutputLengthTest() {
        XYChart.Series series = GraphGenerator.createHeartRateTimeSeries(exampleActivity);
        assertEquals(5, series.getData().size());
    }

    @Test
    public void createSpeedTimeSeriesGraph_OutputNotEmptyTest() {
        XYChart.Series series = GraphGenerator.createSpeedTimeSeries(exampleActivity);
        assertFalse(series.getData().isEmpty());
    }

    @Test
    public void createSpeedTimeSeriesGraph_OutputLengthTest() {
        XYChart.Series series = GraphGenerator.createSpeedTimeSeries(exampleActivity);
        assertEquals(5, series.getData().size());
    }

    @Test
    public void createCaloriesTimeSeriesGraph_OutputNotEmptyTest() {
        XYChart.Series series = GraphGenerator.createCaloriesTimeSeries(exampleActivity);
        assertFalse(series.getData().isEmpty());
    }

    @Test
    public void createCaloriesTimeSeriesGraph_OutputLengthTest() {
        XYChart.Series series = GraphGenerator.createCaloriesTimeSeries(exampleActivity);
        assertEquals(5, series.getData().size());
    }

    @Test
    public void createAltitudeTimeSeriesGraph_OutputNotEmptyTest() {
        XYChart.Series series = GraphGenerator.createAltitudeTimeSeries(exampleActivity);
        assertFalse(series.getData().isEmpty());
    }

    @Test
    public void createAltitudeTimeSeriesGraph_OutputLengthTest() {
        XYChart.Series series = GraphGenerator.createAltitudeTimeSeries(exampleActivity);
        assertEquals(5, series.getData().size());
    }


}
