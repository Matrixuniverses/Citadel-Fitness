package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateProfileController implements Initializable{

    @FXML
    Button createButton;

    @FXML
    Button cancel;

    @FXML
    TextField fNameField;

    @FXML
    TextField lNameField;

    @FXML
    TextField heightField;

    @FXML
    TextField dobField;

    @FXML
    TextField weightField;


    public Button getCreateButton() {
        return createButton;
    }

    public Button getCancel() {
        return cancel;
    }

    public TextField getfNameField() {
        return fNameField;
    }

    public TextField getlNameField() {
        return lNameField;
    }

    public TextField getHeightField() {
        return heightField;
    }

    public TextField getDobField() {
        return dobField;
    }

    public TextField getWeightField() {
        return weightField;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
