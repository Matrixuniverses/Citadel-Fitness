package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the calender. Handles scene and events.
 */

public class CalendarController implements Initializable {
    private DataManager dataManager = DataManager.getDataManager();

    @FXML
    TableView<Activity> calendarTable;

    @FXML
    TableColumn mondayCol;

    @FXML
    TableColumn tuesdayCol;

    @FXML
    TableColumn wednesdayCol;

    @FXML
    TableColumn thursdayCol;

    @FXML
    TableColumn fridayCol;

    @FXML
    TableColumn saturdayCol;

    @FXML
    TableColumn sundayCol;


    @FXML
    Button currentButton;

    @FXML
    Button nextButton;

    @FXML
    Button previousButton;

    @FXML
    Button detailButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        calendarTable.setPlaceholder(new Label("No activities this week!"));

        mondayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        thursdayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        fridayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        saturdayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        sundayCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));

        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                calendarTable.setItems(DataManager.getDataManager().getActivityList());
            }
        });


    }
}
