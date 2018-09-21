package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.analysis.GraphGenerator;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.Route;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class ActivityInfoController implements Initializable, UserData {

    private DataManager dataManager;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

    @FXML
    private LineChart<?, ?> activityChart;

    @FXML
    private Label distanceLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label activityNameLabel;

    @FXML
    private Button closeButton;

    @FXML
    private Button editButton;

    @FXML
    private Label bpmLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label vmaxLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("fitnessMap.html").toExternalForm());
        activityChart.setTitle("Distance/Time");
        activityChart.getXAxis().setLabel("Time(s)");
        activityChart.getYAxis().setLabel("Distance(m)");
    }

    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
    }

    @Override
    public void updateUser() {

    }

    public void updateActivity(Activity activity) {
        activityNameLabel.textProperty().bind(activity.activityNameProperty());
        distanceLabel.textProperty().bind(Bindings.format("%.0f", activity.totalDistanceProperty()));
        timeLabel.textProperty().bind(Bindings.format("%.0f", activity.totalTimeProperty()));
        speedLabel.textProperty().bind(Bindings.format("%.2f", activity.totalDistanceProperty().divide(activity.totalTimeProperty())));
        //caloriesLabel.textProperty().bind(Bindings.format("%.0f", activity.caloriesBurnedProperty()));
        caloriesLabel.textProperty().setValue("0.0");
        bpmLabel.textProperty().bind(Bindings.format("%.0f", activity.averageHRProperty()));


        System.out.println(activity.getCaloriesBurned());

        Route path = new Route(activity.getActivityData());
        String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
        webEngine.executeScript(scriptToExecute);

        activityChart.getData().removeAll(activityChart.getData());
        XYChart.Series series = GraphGenerator.createTimeSeries(activity);
        activityChart.getData().add(series);
    }


    public Button getCloseButton(){
        return closeButton;
    }

}
