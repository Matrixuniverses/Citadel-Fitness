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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.io.IOException;
import java.net.URL;

import java.time.ZoneId;
import java.util.*;

/**
 * Controller for Activity Scene
 */
public class ActivityViewController implements Initializable  {

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
    private Button viewDataPointsButton;

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

    @FXML
    private AnchorPane activityView;

    private AnchorPane editActivity;
    private EditActivityController editActivityController;

    private BorderPane viewDataPointsScene;
    private ViewDataPointsController viewDataPointsController;

    private FilteredList<Activity> filteredList;

    /**
     * This initalizes the ActivityView scene
     * @param location location URL
     * @param resources FXML and css resources for Activity View
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Load edit activity view and data view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditActivity.fxml"));
            editActivity = loader.load();
            editActivityController = loader.getController();
            activityView.getChildren().add(editActivity);
            editActivity.toBack();


            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLViewDataPoints.fxml"));
            viewDataPointsScene = loader.load();
            viewDataPointsController = loader.getController();
            activityView.getChildren().add(viewDataPointsScene);
            viewDataPointsScene.toBack();

        } catch (IOException e) {
            e.printStackTrace();
        }
        editActivity.setLayoutX(250);
        editActivity.setLayoutY(50);
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        activityTable.setPlaceholder(new Label("No activity data uploaded currently."));

        //Setup activity table
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

                viewDataPointsScene.toBack();
            }
        });

        // Sets the logo to pulse. Disables buttons if the selected activity is null.
        activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewDataPointsButton.setDisable(false);
                editActivityButton.setDisable(false);
                detailButton.setDisable(false);
                activityDeleteButton.setDisable(false);

                double heartRate = newSelection.getAverageHR();
                pulser.setValue(Double.toString(heartRate));
            } else {
                detailButton.setDisable(true);
                viewDataPointsButton.setDisable(true);
                editActivityButton.setDisable(true);
                activityDeleteButton.setDisable(true);
            }
        });

        // setup filters
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

        // Sends the edit activity scene to back on 'close' button click.
        editActivityController.getCancelButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editActivity.toBack();
            }
        });


        // Brings the edit activity popup to front.
        editActivityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Activity selected = getActivityTable().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    editActivityController.editActivity(selected);
                    editActivity.toFront();
                }

            }
        });

        // Brings the view data point scene forward on button click.
        viewDataPointsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Activity selected = getActivityTable().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    viewDataPointsController.updateActivity(selected);
                    viewDataPointsScene.toFront();
                    viewDataPointsScene.setVisible(true);
                }
            }
        });

        // Closes the data point scene.
        viewDataPointsController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewDataPointsScene.setVisible(false);
                viewDataPointsScene.toBack();
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

    /**
     * checks if filters have been applied to activity list and if so shows the filtered activity list using predicates
     */
    public void runFilters(){
        filteredList.setPredicate(obj -> {
            if ((dateToPicker.getValue() != null) && (dateFromPicker.getValue() != null)) {
                Date end = Date.from(dateToPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(86400));
                Date start = Date.from(dateFromPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());


                if (obj.getDate().before(start) || obj.getDate().after(end)) {
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

    /**
     * Removes an activity from activity list if conformation is successful.
     */
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

    public void closePopup(){
        editActivityController.getCancelButton().fire();
    }


    public javafx.scene.control.TableView<Activity> getActivityTable() {
        return activityTable;
    }

    public Button getDetailButton() {
        return detailButton;
    }
}

