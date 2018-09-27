package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MapMyRunController implements Initializable, UserData{

    @FXML
    private Label distanceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

    public void initialize(URL location, ResourceBundle resources) {
        initMap();
    }

    /**
     * Initialises Google Maps as a new web view, however as no API keys exist this method is subject to bandwidth
     * restrictions or network congestion
     */
    private void initMap() {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("mapMyRun.html").toExternalForm());

    }

    public void clickTest() {
        String executeScript = webEngine.executeScript("distanceTest();").toString();
        System.out.println(Double.parseDouble(executeScript));
    }

    public void updateUser() {
    }


}
