package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    Button newUserButton;



    public Button getNewUserButton() {
        return newUserButton;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
