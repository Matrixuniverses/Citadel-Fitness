package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;
import seng202.group2.model.Route;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This initialises the MapView Scene and handles events within MapView
 */
public class MapViewController implements Initializable  {

    private DataManager dataManager = DataManager.getDataManager();

    @FXML
    private javafx.scene.control.TableView<Activity> mapActivityTable;

    @FXML
    private TableColumn mapActivityNameCol;

    @FXML
    private WebView mapWebView;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView disconnectedIcon;

    private WebEngine webEngine;

    public void initialize(URL location, ResourceBundle resources) {
        initMap();
        mapActivityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mapActivityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));


        // Adds a listener to the activity table. When the user selects an activity, a Route is created based on
        // that activities' points. This path (list of points) is fed in to a javascript function which generates the route on the map.
        mapActivityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Activity selectedActivity = mapActivityTable.getSelectionModel().getSelectedItem();
                    Route path = new Route(selectedActivity.getActivityData());
                    String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
                    webEngine.executeScript(scriptToExecute);
                    disconnectedIcon.setVisible(false);
                    errorLabel.setVisible(false);
                } catch (netscape.javascript.JSException e) {
                    errorLabel.setVisible(true);
                    disconnectedIcon.setVisible(true);
                    errorLabel.setText("Internet connection required for map view.");
                }
            }
        });

        // Adds a listener to the data manager. When the user is changed, the new user's activities are loaded, and
        // a JavaScript function is called to clear the map of any markers.
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                mapActivityTable.setItems(dataManager.getCurrentUser().getNonManualActivityList());
                webEngine.executeScript("clearRoute()");
            }
        });

    }

    /**
     * Initialises Google Maps as a new web view, however as no API keys exist this method is subject to bandwidth
     * restrictions or network congestion
     */
    private void initMap() {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("fitnessMap.html").toExternalForm());
        errorLabel.setVisible(false);
    }

    public javafx.scene.control.TableView<Activity> getActivityTable() {
        return mapActivityTable;
    }

    public TableView<Activity> getMapActivityTable() {
        return mapActivityTable;
    }

    public TableColumn getMapActivityNameCol() {
        return mapActivityNameCol;
    }

    public WebView getMapWebView() {
        return mapWebView;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

}
