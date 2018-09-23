package seng202.group2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Activity Scene
 */
public class ActivityViewController implements Initializable, UserData {

    private DataManager dataManager;



    @FXML
    TableView<Activity> activityTable;

    //Table Columns
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

    //Buttons
    @FXML
    private Button detailButton;

    @FXML
    Button activityDeleteButton;

    /**
     * This initalizes the ActivityView scene
     * @param location location URL
     * @param resources FXML and css resources for Activity View
     */
    public void initialize(URL location, ResourceBundle resources) {
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activityTable.setPlaceholder(new Label("No activity data uploaded currently."));

        activityDateCol.setCellValueFactory(new PropertyValueFactory<Activity, Formatter>("formattedDate"));
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        activityTypeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        activityDistanceCol.setCellValueFactory(new PropertyValueFactory<Activity, Double>("totalDistance"));
        activityTimeCol.setCellValueFactory(new PropertyValueFactory<Activity, Formatter>("formattedTotalTime"));

    }

    public void delete(){
        Activity activity = activityTable.getSelectionModel().getSelectedItem();
        if (activity != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Com");
            alert.setContentText("Do you want to delete the selected ");

            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                dataManager.deleteActivity(activity);
            }
        }
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

