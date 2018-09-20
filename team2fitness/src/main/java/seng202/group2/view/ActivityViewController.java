package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ActivityViewController implements Initializable, UserData {

    private DataManager dataManager;

    @FXML
    javafx.scene.control.TableView<Activity> activityTable;

    @FXML
    TableColumn activityDateCol;

    @FXML
    TableColumn activityNameCol;

    @FXML
    TableColumn activityTypeCol;

    @FXML
    TableColumn activityDistanceCol;

    @FXML
    Button activityDeleteButton;


    /**
     * Initializes the activity table. Sets the Col's to display the right fields of activity
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        activityTable.setPlaceholder(new Label("No activity data uploaded currently."));

        activityDateCol.setCellValueFactory(new PropertyValueFactory<Activity, Date>("date"));
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        activityTypeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        activityDistanceCol.setCellValueFactory(new PropertyValueFactory<Activity, Double>("totalDistance"));
    }

    public javafx.scene.control.TableView<Activity> getActivityTable() {
        return activityTable;
    }

    public Button getActivityDeleteButton() {
        return activityDeleteButton;
    }

    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
    }

    public void updateUser() {
        activityTable.getItems().clear();
        //System.out.println(dataManager.getActivityList().get(0).getTotalDistance());
        activityTable.setItems(dataManager.getActivityList());
    }
}

