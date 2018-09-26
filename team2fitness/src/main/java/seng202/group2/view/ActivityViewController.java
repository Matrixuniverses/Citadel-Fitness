package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Activity Scene
 */
public class ActivityViewController implements Initializable, UserData {


    private DataManager dataManager = DataManager.getDataManager();

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

    @FXML
    private ImageView navLogo;

    StringProperty pulser = new SimpleStringProperty("0");


    /**
     * This initalizes the ActivityView scene
     * @param location location URL
     * @param resources FXML and css resources for Activity View
     */
    public void initialize(URL location, ResourceBundle resources) {
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activityTable.setPlaceholder(new Label("No activity data uploaded currently."));

        activityDateCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("formattedDate"));
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        activityTypeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        activityDistanceCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("formattedTotalDistance"));
        activityTimeCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("formattedTotalTime"));


        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                activityTable.setItems(DataManager.getDataManager().getActivityList());
            }
        });


        activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Double heartRate = newSelection.getAverageHR();
                pulser.setValue(Double.toString(heartRate));
            }
        });

    }

    public StringProperty getPulser() {
        return pulser;
    }



    public void delete(){
        Activity activity = activityTable.getSelectionModel().getSelectedItem();
        if (activity != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm delete");
            alert.setContentText("Do you want to delete the selected activity?");

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

    public void updateUser() {


    }

    public TableView<Activity> getTable() {
        return activityTable;
    }

    public Button getDetailButton() {
        return detailButton;
    }
}

