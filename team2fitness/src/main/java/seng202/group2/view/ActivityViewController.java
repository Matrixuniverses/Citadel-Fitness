package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.model.User;

import javax.swing.text.TableView;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ActivityViewController implements Initializable, UserData {

    private User user;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        activityDateCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("startDate"));
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        activityTypeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        activityDistanceCol.setCellValueFactory(new PropertyValueFactory<Activity, Double>("totalDistance"));


    }

    @Override
    public void updateUserData(User user) {
        this.user = user;
        activityTable.setItems(user.getActivityList());
    }
}

