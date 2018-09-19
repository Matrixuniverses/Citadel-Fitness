package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.model.DataManager;
import seng202.group2.model.User;

import javax.swing.text.TableView;
import javax.xml.crypto.Data;
import java.net.URL;
import java.text.SimpleDateFormat;
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


    public void initialize(URL location, ResourceBundle resources) {
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        System.out.println("Activ" + dataManager);
//        System.out.println(dataManager.getCurrentUser());
//        activityTable.setItems(dataManager.getActivityList());
    }
}

