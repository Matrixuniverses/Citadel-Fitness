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
import seng202.group2.data.Route;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Activity Information Scene
 */
public class ActivityInfoController implements Initializable, UserData {

    private DataManager dataManager;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

    @FXML
    private LineChart<?, ?> activityChart;

    //Labels
    @FXML
    private Label distanceLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label activityNameLabel;

    @FXML
    private Label bpmLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label vmaxLabel;

    //Buttons
    @FXML
    private Button closeButton;

    @FXML
    private Button editButton;

    @FXML
    private Label errorLabel;



    /**
     * This initialises the webEngine and activityChart for the ActivityInfo Scene
     * @param location URL location
     * @param resources FXML and css resources for Activity info scene
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("fitnessMap.html").toExternalForm());

        activityChart.setTitle("Distance/Time");
        activityChart.getXAxis().setLabel("Time(s)");
        activityChart.getYAxis().setLabel("Distance(m)");

        activityChart.setCreateSymbols(false);

    }

    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
    }

    @Override
    public void updateUser() {

    }

    /**
     * This binds the activity information to its corresponding label
     * @param activity activity object
     */
    public void updateActivity(Activity activity) {
        activityNameLabel.textProperty().bind(activity.activityNameProperty());
        distanceLabel.textProperty().bind(Bindings.format("%.0f", activity.totalDistanceProperty()));
        timeLabel.textProperty().bind(Bindings.format("%.0f", activity.totalTimeProperty()));
        speedLabel.textProperty().bind(Bindings.format("%.2f", activity.totalDistanceProperty().divide(activity.totalTimeProperty())));
        //caloriesLabel.textProperty().bind(Bindings.format("%.0f", activity.caloriesBurnedProperty()));
        caloriesLabel.textProperty().setValue("0.0");
        if (activity.averageHRProperty().get() != -1) {
            bpmLabel.textProperty().bind(Bindings.format("%.0f", activity.averageHRProperty()));
        } else {
            bpmLabel.textProperty().set("Error");
        }



        //System.out.println(activity.getCaloriesBurned());

        try {
            Route path = new Route(activity.getActivityData());
            String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
            webEngine.executeScript(scriptToExecute);
            errorLabel.setVisible(false);
        }
        catch (netscape.javascript.JSException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Internet must be connected for map view.");
        }

        activityChart.getData().removeAll(activityChart.getData());
        XYChart.Series series = GraphGenerator.createTimeSeries(activity);
        activityChart.getData().add(series);
    }

    public Button getCloseButton(){
        return closeButton;
    }

}
