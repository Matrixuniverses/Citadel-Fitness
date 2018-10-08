package seng202.group2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;


import java.util.Date;
import java.time.ZoneId;
import java.util.InputMismatchException;

/**
 * Controller class for edit activity scene. Handles scene and events.
 */
public class EditActivityController {

    @FXML
    private TextField activityNameField;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private ComboBox typeComboBox;

    @FXML
    private Label confirmationLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editButton;

    private DataManager dataManager = DataManager.getDataManager();

    Activity currentActivity;

    /**
     * Populates the fields of the edit activity popup, with the selected activities' current info.
     * @param activity The activity selected by the user.
     */
    public void editActivity(Activity activity){
        nameErrorLabel.setVisible(false);
        confirmationLabel.setVisible(false);
        currentActivity = activity;
        activityNameField.setText(activity.activityNameProperty().getValue().toString());
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        typeComboBox.setItems(typeOptions);
        if (activity.getActivityType().equals("Exercise")){
            typeComboBox.setValue("Run");
        } else{
            typeComboBox.setValue(activity.getActivityType());
        }

    }



    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * Checks the new activity name for validity, then updates the activity.
     * Displays an error message if name invalid, or a success message if the update is successful.
     */
    @FXML
    public void editButtonPushed() {
        boolean update = true;
        String newName = activityNameField.getText();
        String newType = typeComboBox.getValue().toString();

        if (newName.length() > 30){
            nameErrorLabel.setText("Name cannot exceed 30 characters");
            nameErrorLabel.setVisible(true);
            update = false;
        } else if (newName.length() == 0){
            nameErrorLabel.setText("Name cant be empty");
            update = false;
        } if (update){
            nameErrorLabel.setVisible(false);
            currentActivity.setActivityName(newName);
            currentActivity.setActivityType(newType);
            confirmationLabel.setText("Activity Updated");
            confirmationLabel.setVisible(true);

        }
    }
}
