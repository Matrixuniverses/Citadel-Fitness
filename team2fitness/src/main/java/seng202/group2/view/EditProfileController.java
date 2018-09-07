package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import seng202.group2.model.User;

import java.io.File;
import java.lang.reflect.Field;

public class EditProfileController {


    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField dobField;

    @FXML
    private TextField weightField;

    @FXML
    private Button newPic;

    @FXML
    private Button cancel;

    @FXML
    private Button saveChanges;

    public TextField getfNameField() {
        return fNameField;
    }

    public void setfNameField(TextField fNameField) {
        this.fNameField = fNameField;
    }

    public TextField getlNameField() {
        return lNameField;
    }

    public void setlNameField(TextField lNameField) {
        this.lNameField = lNameField;
    }

    public TextField getHeightField() {
        return heightField;
    }

    public void setHeightField(TextField heightField) {
        this.heightField = heightField;
    }

    public TextField getDobField() {
        return dobField;
    }

    public void setDobField(TextField dobField) {
        this.dobField = dobField;
    }

    public TextField getWeightField() {
        return weightField;
    }

    public void setWeightField(TextField weightField) {
        this.weightField = weightField;
    }



    public Button getSaveChangesButton() {
        return saveChanges;
    }

    public void createProfileAction(ActionEvent event){
        //TODO: check if next userid is empty and selected
        //TODO: Check all fields are populated
        //TODO: save all fields to DB
        //TODO: Launch main application with user data



    }

    public void saveProfileAction(ActionEvent event) {
        //TODO: check if next userid is empty and selected
        //TODO: Check all fields are populated
        //TODO: save all fields to DB
        //TODO: Launch main application with user data
    }

    public void updateUserData(User user) {

    }

}
