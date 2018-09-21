package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.net.URL;
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
/*        nameField.textProperty().bind(currentUser.nameProperty());
        ageField.textProperty().bind(Bindings.convert(currentUser.ageProperty()));
        weightField.textProperty().bind(Bindings.convert(currentUser.ageProperty()));
        heightField.textProperty().bind(Bindings.convert(currentUser.ageProperty()));*/
    }

    /**
     * Updates the user's data with the fields provided. User information is changed and written to database.
     */
    public void update() {
        //System.out.println("test");
        currentUser.setName(nameField.getText());
        currentUser.setAge(Integer.valueOf(ageField.getText()));
        currentUser.setHeight(Double.valueOf(heightField.getText()));
        currentUser.setWeight(Double.valueOf(heightField.getText()));
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
