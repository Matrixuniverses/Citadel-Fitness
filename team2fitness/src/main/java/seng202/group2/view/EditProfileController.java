package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.net.URL;
import java.util.IllegalFormatException;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

/**
 * This is the controller for EditProfile Scene, initializes and handles Scene events
 */
public class EditProfileController implements Initializable, UserData {

    @FXML
    private Button updateButton;

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
    private Label nameErrorLabel;

    @FXML
    private Label heightErrorLabel;

    @FXML
    private Label ageErrorLabel;

    @FXML
    private Label weightErrorLabel;

    @FXML
    private Label confirmLabel;

    private DataManager dataManager;


    User currentUser;

    public void initialize(URL location, ResourceBundle resources) {
        nameErrorLabel.setTextFill(Color.RED);
        heightErrorLabel.setTextFill(Color.RED);
        weightErrorLabel.setTextFill(Color.RED);
        ageErrorLabel.setTextFill(Color.RED);
        confirmLabel.setTextFill(Color.RED);

    }


    @Override
    public void setDataManager(DataManager newDataManager) {
        dataManager = newDataManager;
    }

    /**
     * Fills the provided fields with the user's current information.
     */
    @Override
    public void updateUser() {
        currentUser = dataManager.getCurrentUser();
        setFields();

        nameErrorLabel.setText("");
        ageErrorLabel.setText("");
        heightErrorLabel.setText("");
        weightErrorLabel.setText("");
        confirmLabel.setText("");
    }

    private void setFields() {
        nameField.setText(currentUser.getName());
        ageField.setText(Integer.toString(currentUser.getAge()));
        heightField.setText(Double.toString(currentUser.getHeight()));
        weightField.setText(Double.toString(currentUser.getWeight()));
    }



    /**
     * Updates the user's data with the fields provided. User information is changed and written to database.
     */
/*    public void update() {
        nameErrorLabel.setVisible(false);
        ageErrorLabel.setVisible(false);
        heightErrorLabel.setVisible(false);
        weightErrorLabel.setVisible(false);
        confirmLabel.setVisible(false);

        try {
            String name = nameField.getText();
            int age = Integer.valueOf(ageField.getText());
            double height = Double.valueOf(heightField.getText());
            double weight = Double.valueOf(weightField.getText());

            if (name.length() > 25) {
                nameErrorLabel.setVisible(true);
                nameErrorLabel.setText("Name cannot exceed 25 characters.");
            } else if (age < 0) {
                ageErrorLabel.setVisible(true);
                ageErrorLabel.setText("Age value cannot be negative.");
            } else if (height <= 0 || weight <= 0) {
                heightErrorLabel.setVisible(true);
                heightErrorLabel.setText("Height/Weight values must be positive numbers.");
            } else {
                currentUser.setAge(age);
                currentUser.setHeight(height);
                currentUser.setWeight(weight);
                confirmLabel.setVisible(true);
                if (nameField.getText().length() == 0) {
                    confirmLabel.setText("Age/Height/Weight values updated.");
                } else {
                    currentUser.setName(nameField.getText());
                    confirmLabel.setText("All values updated.");
                }
            }
        } catch (NumberFormatException e) {
            ageErrorLabel.setVisible(true);
            ageErrorLabel.setText("Age must be a number.");
            weightErrorLabel.setVisible(true);
            weightErrorLabel.setText("Weight must be a number.");
            heightErrorLabel.setVisible(true);
            heightErrorLabel.setText("Height must be a number.");
        }
    }*/

    /**
     * Updates the user's data with the fields provided. User information is changed and written to database.
     */
    public void update() {

        nameErrorLabel.setText("");
        ageErrorLabel.setText("");
        heightErrorLabel.setText("");
        weightErrorLabel.setText("");


        boolean update = true;
        int age = 0;
        double height = 0.0;
        double weight = 0.0;

        String name = nameField.getText();
        if (name.length() > 25) {
            nameErrorLabel.setText("Name cannot exceed 25 characters");

            update = false;
        } else if (name.length() == 0) {
            nameErrorLabel.setText("Name can't be empty.");
            update = false;
        }

        try {
            age = Integer.valueOf(ageField.getText());
            if (age > 105 || age < 0) {
                ageErrorLabel.setText("Age must be between 0 and 105");
                update = false;
            }
        } catch (NumberFormatException e) {
            ageErrorLabel.setText("Age must be an integer");
            update = false;
        }

        try {
            height = Double.valueOf(heightField.getText());
            if (height > 270 || height < 0) {
                heightErrorLabel.setText("Height must be between 0 and 270");
                update = false;
            }
        } catch (NumberFormatException e) {
            heightErrorLabel.setVisible(true);
            heightErrorLabel.setText("Height must be a number");
            update = false;
        }

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

        if (update) {
            currentUser.setName(name);
            currentUser.setAge(age);
            currentUser.setWeight(weight);
            currentUser.setHeight(height);
            confirmLabel.setTextFill(Color.GREEN);
            confirmLabel.setText("User data updated.");
        } else {
            confirmLabel.setText("User data invalid. Profile update failed.");
        }
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
