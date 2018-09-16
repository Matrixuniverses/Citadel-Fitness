package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.group2.model.Activity;
import seng202.group2.model.User;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MapViewController implements Initializable, UserData  {

    private User user;

    @FXML
    private javafx.scene.control.TableView<Activity> mapActivityTable;

    @FXML
    private TableColumn mapActivityNameCol;

    @FXML
    private WebView mapWebView;

    @FXML
    private Button showRouteButton;

    private WebEngine webEngine;

    public void initialize(URL location, ResourceBundle resources) {
        initMap();
        mapActivityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mapActivityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
    }

    /**
     * Initialises the Google Maps in a web view.
     */
    private void initMap() {
        webEngine = mapWebView.getEngine();
        webEngine.load(this.getClass().getClassLoader().getResource("fitnessMap.html").toExternalForm());
    }

    public javafx.scene.control.TableView<Activity> getActivityTable() {
        return mapActivityTable;
    }

    /**
     * Populates the map table with the user's activities. Done so that the user can select one to view on the map as
     * a route.
     * @param user The user who's activity data is to be added to the table.
     */
    public void updateUserData(User user) {
        this.user = user;
        mapActivityTable.setItems(user.getActivityList());
    }

}
