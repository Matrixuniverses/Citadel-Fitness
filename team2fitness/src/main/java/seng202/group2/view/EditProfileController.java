package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label errorLabel;

    private DataManager dataManager;


    User currentUser;

    public void initialize(URL location, ResourceBundle resources) {
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
    }

    /**
     * Updates the user's data with the fields provided. User information is changed and written to database.
     */
    public void update() {



        try {

            int age = Integer.valueOf(ageField.getText());
            double height = Double.valueOf(heightField.getText());
            double weight = Double.valueOf(weightField.getText());

            if (age >= 0 && height >= 0 && weight >= 0) {
                currentUser.setAge(age);
                currentUser.setHeight(height);
                currentUser.setWeight(weight);
            } else {
                throw new NumberFormatException();
            }

            if (nameField.getText().length() == 0) {
                throw new InputMismatchException();
            } else {
                errorLabel.setVisible(false);
                currentUser.setName(nameField.getText());
            }

        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Age/height/weight must be positive numbers");
        } catch (InputMismatchException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Name field must be non-empty");
        }
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
