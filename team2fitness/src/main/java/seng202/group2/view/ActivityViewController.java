package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.data.ActivityDBOperations;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;
import java.net.URL;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Controller for Activity Scene
 */
public class ActivityViewController implements Initializable, UserData {

    private DataManager dataManager = DataManager.getDataManager();
    private StringProperty pulser = new SimpleStringProperty("0");

    @FXML
    private TableView<Activity> activityTable;

    //Table Columns
    @FXML
    private TableColumn activityDateCol;

    @FXML
    private TableColumn activityNameCol;

    @FXML
    private TableColumn activityTypeCol;

    @FXML
    private TableColumn activityDistanceCol;

    @FXML
    private TableColumn activityTimeCol;

    //Buttons
    @FXML
    private Button detailButton;

    @FXML
    private Button activityDeleteButton;

    @FXML
    private Button viewDataPoints;

    @FXML
    private Button editActivityButton;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox typePicker;

    @FXML
    private Button addActivityButton;



    private FilteredList<Activity> filteredList;

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
                filteredList = new FilteredList<>(DataManager.getDataManager().getActivityList());
                activityTable.setItems(filteredList);
            }
        });


        activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewDataPoints.setDisable(false);
                editActivityButton.setDisable(false);
                detailButton.setDisable(false);
                activityDeleteButton.setDisable(false);

                double heartRate = newSelection.getAverageHR();
                pulser.setValue(Double.toString(heartRate));
            } else {
                detailButton.setDisable(true);
                viewDataPoints.setDisable(true);
                editActivityButton.setDisable(true);
                activityDeleteButton.setDisable(true);
            }
        });

        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("All");
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        typePicker.setItems(typeOptions);
        typePicker.setValue("All");
        typePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                runFilters();

            }
        });


    }

    public StringProperty getPulser() {
        return pulser;
    }

    @FXML
    public void clearPushed(){
        dateToPicker.setValue(null);
        dateFromPicker.setValue(null);
        typePicker.setValue("All");
        runFilters();
    }

    @FXML
    public void searchPushed(){
        runFilters();

    }

    public void runFilters(){
        filteredList.setPredicate(obj -> {

            if ((dateToPicker.getValue() != null) && (dateFromPicker.getValue() != null)) {
                Date start = Date.from(dateToPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(dateFromPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

                if (obj.getDate().before(end) || obj.getDate().after(start)) {
                    return false;
                }
            }

            if (typePicker.getSelectionModel().getSelectedIndex() == 0) {
                return true;
            }

            if (typePicker.getSelectionModel().getSelectedItem().equals(obj.getActivityType())) {
                return true;
            } else {
                return false;
            }
        });
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

    public Button getActivityAddButton(){
        return addActivityButton;
    }

    public javafx.scene.control.TableView<Activity> getActivityTable() {
        return activityTable;
    }

    public void updateUser() {
    }

    public Button getDetailButton() {
        return detailButton;
    }
}

