package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ActivityViewController implements Initializable, UserData {

    private DataManager dataManager;

    @FXML
    private Button detailButton;

    @FXML
    TableView<Activity> activityTable;

    @FXML
    TableColumn activityDateCol;

    @FXML
    TableColumn activityNameCol;

    @FXML
    TableColumn activityTypeCol;

    @FXML
    TableColumn activityDistanceCol;

    @FXML
    TableColumn activityTimeCol;

    @FXML
    Button activityDeleteButton;


    public void initialize(URL location, ResourceBundle resources) {
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activityTable.setPlaceholder(new Label("No activity data uploaded currently."));

        activityDateCol.setCellValueFactory(new PropertyValueFactory<Activity, Date>("date"));
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        activityTypeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        activityDistanceCol.setCellValueFactory(new PropertyValueFactory<Activity, Double>("totalDistance"));
        activityTimeCol.setCellValueFactory(new PropertyValueFactory<Activity, Double>("totalTime"));
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
        activityTable.setItems(dataManager.getActivityList());
    }

    public TableView<Activity> getTable() {
        return activityTable;
    }

    public Button getDetailButton() {
        return detailButton;
    }
}

