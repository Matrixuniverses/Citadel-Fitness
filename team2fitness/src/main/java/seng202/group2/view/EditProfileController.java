package seng202.group2.view;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for EditProfile Scene, initializes and handles Scene events
 */
public class EditProfileController implements Initializable  {

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private Button closeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label heightErrorLabel;

    @FXML
    private Label ageErrorLabel;

    @FXML
    private Label weightErrorLabel;

    @FXML
    private Label confirmLabel;

    private DataManager dataManager = DataManager.getDataManager();

    private User currentUser;

    public void initialize(URL location, ResourceBundle resources) {
        nameErrorLabel.setTextFill(Color.RED);
        heightErrorLabel.setTextFill(Color.RED);
        weightErrorLabel.setTextFill(Color.RED);
        ageErrorLabel.setTextFill(Color.RED);
        confirmLabel.setTextFill(Color.RED);

        dataManager.currentUserProperty().addListener((ObservableValue<? extends User> observable,
                                                       User oldValue, User newValue) -> {
            currentUser = DataManager.getDataManager().getCurrentUser();
            setFields();
            nameErrorLabel.setText("");
            ageErrorLabel.setText("");
            heightErrorLabel.setText("");
            weightErrorLabel.setText("");
            confirmLabel.setText("");
        });
    }

    /**
     * Fills the provided fields with the user's current information.
     */
    private void setFields() {
        nameField.setText(currentUser.getName());
        ageField.setText(Integer.toString(currentUser.getAge()));
        heightField.setText(Double.toString(currentUser.getHeight()));
        weightField.setText(Double.toString(currentUser.getWeight()));
    }

    /**
     * Updates the user's data with the fields provided. User information is changed and written to database.
     */
    @FXML
    public void update() {

        nameErrorLabel.setText("");
        ageErrorLabel.setText("");
        heightErrorLabel.setText("");
        weightErrorLabel.setText("");

        boolean update = true;
        int age = 0;
        double height = 0.0;
        double weight = 0.0;

        // Checks name
        String name = nameField.getText();
        if (name.length() > 25) {
            nameErrorLabel.setText("Name cannot exceed 25 characters");
            update = false;
        } else if (name.length() == 0) {
            nameErrorLabel.setText("Name can't be empty.");
            update = false;
        }

        // Checks age
        try {
            age = Integer.valueOf(ageField.getText());
            if (age > 140 || age < 0) {
                ageErrorLabel.setText("Age must be between 0 and 140");
                update = false;
            }
        } catch (NumberFormatException e) {
            ageErrorLabel.setText("Age must be an integer");
            update = false;
        }

        // Checks height
        try {
            height = Double.valueOf(heightField.getText());
            if (height > 270 || height <= 50) {
                heightErrorLabel.setText("Height must be between 0 and 270");
                update = false;
            }
        } catch (NumberFormatException e) {
            heightErrorLabel.setVisible(true);
            heightErrorLabel.setText("Height must be a number");
            update = false;
        }

        // Checks weight
        try {
            weight = Double.valueOf(weightField.getText());
            if (weight > 600 || weight < 0) {
                weightErrorLabel.setText("Weight must be between 0 and 600");
                update = false;
            }
        } catch (NumberFormatException e) {
            weightErrorLabel.setVisible(true);
            weightErrorLabel.setText("Weight must be an integer");
            update = false;
        }

        // If all checks verfiy the input is valid, the current user's profile is updated.
        if (update) {
            currentUser.setName(name);
            currentUser.setAge(age);
            currentUser.setWeight(weight);
            currentUser.setHeight(height);
            confirmLabel.setTextFill(Color.GREEN);
            confirmLabel.setText("User data updated.");
        } else {
            confirmLabel.setTextFill(Color.RED);
            confirmLabel.setText("User data invalid. Profile update failed.");
        }
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
