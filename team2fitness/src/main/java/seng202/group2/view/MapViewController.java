package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.Route;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This initialises the MapView Scene and handles events within MapView
 */
public class MapViewController implements Initializable, UserData  {

    private DataManager dataManager;

    @FXML
    private javafx.scene.control.TableView<Activity> mapActivityTable;

    @FXML
    private TableColumn mapActivityNameCol;

    @FXML
    private WebView mapWebView;

    @FXML
    private Label errorLabel;

    private WebEngine webEngine;

    public void initialize(URL location, ResourceBundle resources) {
        initMap();
        mapActivityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mapActivityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));

        mapActivityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Activity selectedActivity = mapActivityTable.getSelectionModel().getSelectedItem();
                    Route path = new Route(selectedActivity.getActivityData());
                    String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
                    webEngine.executeScript(scriptToExecute);
                    errorLabel.setVisible(false);
                } catch (netscape.javascript.JSException e) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Internet connection required for map view.");
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

    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
    }

    @Override
    /**
     * Populates the map table with the user's activities. Done so that the user can select one to view on the map as
     * a route.
     * @param user The user who's activity data is to be added to the table.
     */
    public void updateUser() {
        mapActivityTable.setItems(dataManager.getCurrentUser().getActivityList());
        webEngine.executeScript("clearRoute()");
    }
}
