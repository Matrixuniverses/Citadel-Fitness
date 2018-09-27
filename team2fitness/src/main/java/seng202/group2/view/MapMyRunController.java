package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MapMyRunController implements Initializable, UserData {

    @FXML
    private Label distanceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

    private DoubleProperty distance;
    private DoubleProperty calories;
    private DoubleProperty time;


    public void initialize(URL location, ResourceBundle resources) {
        distance = new SimpleDoubleProperty(0.0);
        time = new SimpleDoubleProperty(0.0);
        calories = new SimpleDoubleProperty(0.0);
        distanceLabel.textProperty().bind(Bindings.format("%.0f", distance));
        caloriesLabel.textProperty().bind(Bindings.format("%.0f", calories));
        timeLabel.textProperty().bind(Bindings.format("%.1f", time));
        initMap();

        // Listener. Calls the javascript distance calculation when the user adds a new marker to their path. The distance property is then updated.
        mapWebView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.isStillSincePress()) {
                    //System.out.println("test");
                    String distanceString = webEngine.executeScript("calculateDistance();").toString();
                    distance.set(Double.parseDouble(distanceString));
                    time.set(calcTime());
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
    }

    /**
     * Test function to calculate distance for map my run feature.
     */
    public void clickTest() {
        String distanceString= webEngine.executeScript("distanceTest();").toString();
        distance.set(Double.parseDouble(distanceString));
    }

    public void updateUser() {
    }

    /**
     * Calculates an estimate for how long it would take to run the route selected by user.
     * Uses the distance property, which is updated eahc time the user adds a marker.
     * @return The time in minutes
     */
    public Double calcTime() {
        Double timePerM = 0.24;
        Double time = timePerM * distance.getValue();
        return time / 60;
    }


}
