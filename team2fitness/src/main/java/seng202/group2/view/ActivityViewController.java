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
import seng202.group2.data.ActivityDBOperations;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Activity Scene
 */
public class ActivityViewController implements Initializable, UserData {

    User currentUser;
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
    Button viewDataPoints;

    @FXML
    Button editActivityButton;

    @FXML
    Button searchButton;

    @FXML
    Button clearButton;

    @FXML
    DatePicker dateFromPicker;

    @FXML
    DatePicker dateToPicker;

    @FXML
    Label errorLabel;

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

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchPushed();
            }
        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearPushed();
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

    public void clearPushed(){
        activityTable.setItems(DataManager.getDataManager().getActivityList());
    }

    public void searchPushed(){
        try{
            String dateFromString;
            String dateToString;
            currentUser = DataManager.getDataManager().getCurrentUser();

            if (dateToPicker.getValue() != null){
                dateToString = dateToPicker.getValue().toString();
            } else {
                throw new IllegalArgumentException("Must pick a 'To' date to perform a search.");
            }
            if (dateFromPicker.getValue() != null){
                dateFromString = dateFromPicker.getValue().toString();
            } else {
                throw new IllegalArgumentException("Must pick a 'From' date to perform a search.");
            }
            Date start = Date.valueOf(dateFromString);
            Date end = Date.valueOf(dateToString);
            if (start.toString() == end.toString()){
                System.out.println(end); //TODO: sort out what happens when the dates are the same.
            }
            int id = currentUser.getId();
            System.out.println(currentUser.getId());
            activityTable.setItems(ActivityDBOperations.getActivitiesBetweenDates(start, end, id));

        } catch (IllegalArgumentException e){
            errorLabel.setText(e.getMessage());
        } catch (SQLException e){
            e.printStackTrace();
        }
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

    public DatePicker getDateFromPicker() {
        return dateFromPicker;
    }

    public DatePicker getDateToPicker() {
        return dateToPicker;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public Button getSearchButton() {
        return searchButton;
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

