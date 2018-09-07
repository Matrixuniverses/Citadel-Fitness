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

    private int userId;
    private String name;
    private int age;
    private double height;
    private double weight;
    private File selectedPic;

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
    private Button createProfile;
    @FXML
    private Button saveChanges;




    public void selectPicAction(ActionEvent event) {
        FileChooser fc = new FileChooser();

        selectedPic = fc.showOpenDialog(null);
        if (selectedPic != null) {
            //TODO: check img
            //TODO: convert image to circle
        } else {
            System.out.println("File is not an image");
        }
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
