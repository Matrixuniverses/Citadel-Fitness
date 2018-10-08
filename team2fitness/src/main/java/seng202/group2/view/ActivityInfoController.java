package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.analysis.GraphGenerator;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.Route;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Activity Information Scene
 */
public class ActivityInfoController implements Initializable {

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

    @FXML
    private ImageView disconnectedIcon;


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
        activityChart.getXAxis().setLabel("Time (minutes)");
        activityChart.getYAxis().setLabel("Distance (m)");

        activityChart.setCreateSymbols(false);

    }

    /**
     * This binds the activity information to its corresponding label
     * @param activity activity object
     */
    public void updateActivity(Activity activity) {
        activityNameLabel.textProperty().bind(activity.activityNameProperty());
        distanceLabel.textProperty().bind(Bindings.format("%.0f", activity.totalDistanceProperty()));
        timeLabel.textProperty().bind(Bindings.format("%.0fh %.0fm %.0fs",
                activity.totalTimeProperty().get() / 3600,
                Math.floor((activity.totalTimeProperty().get() % 3600) / 60),
                activity.totalTimeProperty().get() % 60));
        speedLabel.textProperty().bind(Bindings.format("%.2f", activity.totalDistanceProperty().divide(activity.totalTimeProperty())));
        caloriesLabel.textProperty().bind(Bindings.format("%.0f", activity.caloriesBurnedProperty()));
        bpmLabel.textProperty().bind(Bindings.format("%.0f", activity.averageHRProperty()));
        vmaxLabel.textProperty().bind(Bindings.format("%.0f", activity.vo2MaxProperty()));
        activityChart.getData().removeAll(activityChart.getData());

        // Displays a message indicating manual data can't be visually represented.
        if (activity.isManualEntry()) {
            mapWebView.setVisible(false);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.BLACK);
            errorLabel.setText("Map view is unavailable for this activity.");
            disconnectedIcon.setVisible(false);
            activityChart.setVisible(true);
            activityChart.setTitle("No graph data available.");

            // Displays the route on the mini map.
        } else {
            try {
                Route path = new Route(activity.getActivityData());
                String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
                webEngine.executeScript(scriptToExecute);
                errorLabel.setVisible(false);
                mapWebView.setVisible(true);
                disconnectedIcon.setVisible(false);
            }
            catch (netscape.javascript.JSException e) {
                mapWebView.setVisible(false);
                disconnectedIcon.setVisible(true);
                errorLabel.setVisible(true);
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Internet connection required for map view");
            }
            // Populates the graph with the activity data.
            activityChart.setTitle("Distance/Time");
            XYChart.Series series = GraphGenerator.createTimeSeries(activity);
            activityChart.getData().add(series);
        }


    }

    public Button getCloseButton(){
        return closeButton;
    }


}
