package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Boolean.FALSE;

/**
 * This is the Controller for the Map My Run scene. Initialises and handles scene events.
 */

public class MapMyRunController implements Initializable {

    @FXML
    private Label distanceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private WebView mapWebView;

    @FXML
    private Label errorLabel;

    private WebEngine webEngine;

    private DoubleProperty distance;
    private DoubleProperty calories;
    private DoubleProperty time;

    private List<Double> distanceArray;

    private DataManager dataManager = DataManager.getDataManager();
    private User user = dataManager.getCurrentUser();
    private DataAnalyzer dataAnalyzer;

    public void initialize(URL location, ResourceBundle resources) {
        distance = new SimpleDoubleProperty(0.0);
        time = new SimpleDoubleProperty(0.0);
        calories = new SimpleDoubleProperty(0.0);
        distanceLabel.textProperty().bind(Bindings.format("%.0f", distance));
        caloriesLabel.textProperty().bind(Bindings.format("%.0f", calories));
        timeLabel.textProperty().bind(Bindings.format("%.1f", time));
        errorLabel.setVisible(false);
        dataAnalyzer = new DataAnalyzer();
        distanceArray = new ArrayList<>();
        distanceArray.add(0.0);
        initMap();

        // Listener. Calls the javascript distance calculation when the user adds a new marker to their path. The distance property is then updated.
        mapWebView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.isStillSincePress()) {
                    errorLabel.setVisible(false);
                    String distanceString = webEngine.executeScript("calculateDistance();").toString();
                    distanceArray.add(Double.parseDouble(distanceString));
                    distance.set(Double.parseDouble(distanceString));
                    time.set(calcTime());
                    calories.set(calcCalories());
                }
            }
        });
    }


    /**
     * Initialises Google Maps as a new web view, however as no API keys exist this method is subject to bandwidth
     * restrictions or network congestion
     */
    private void initMap() {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("mapMyRun.html").toExternalForm());

        // Clear the map and show an error message if there is no internet connection
        try {
            resetMap();
        } catch (netscape.javascript.JSException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Internet connection required for map view.");
        }
    }

    /**
     * Calculates an estimate of how many calories burned for the path chosen by user.
     * @return
     */
    public Double calcCalories() {
        double calories = dataAnalyzer.calcCaloriesEstimate(user, time.getValue());
        return calories;
    }

    /**
     * Calculates an estimate for how long it would take to run the route selected by user.
     * Uses the distance property, which is updated eahc time the user adds a marker.
     * @return The time in minutes
     */
    public Double calcTime() {
        Double timePerM = 0.36;
        Double time = timePerM * distance.getValue();
        return time / 60;
    }

    /**
     * Calls a javascript function to clear all markers and polylines from the maps.
     * Empties the arrays for storing the user's run path so they can draw a new one.
     */
    public void resetMap() {
        webEngine.executeScript("clearMap();");
        distance.set(0);
        time.set(0);
        calories.set(0);
        distanceArray = new ArrayList<>();
        distanceArray.add(0.0);
    }

    /**
     * Calls a JavaScript function to remove the last marker the user placed on the map.
     * Removes the last calculated distance, and reverts to the previous one.
     * Re-calculates time and calories based on the updated distance value.
     */
    public void undoMarker() {
        webEngine.executeScript("undoMarker();");
        if (distanceArray.size() > 1) {
            distance.set(distanceArray.get(distanceArray.size() - 2));
            distanceArray.remove(distanceArray.size() - 1);
            time.set(calcTime());
            calories.set(calcCalories());
        }
    }
}
