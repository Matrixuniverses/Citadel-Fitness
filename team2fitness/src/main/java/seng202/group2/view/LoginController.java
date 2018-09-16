package seng202.group2.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    int count = 1;

    @FXML
    Button newUserButton;

    @FXML
    Button user1Button;

    @FXML

    Button user2Button;

    @FXML
    Button user3Button;

    @FXML
    Button user4Button;

    @FXML
    Button user5Button;

    @FXML
    Button user6Button;


    public Button getNewUserButton() {
        return newUserButton;
    }


    public void showButton(String name){
        int userID = getCount();
        switch(userID) {
            case(1):
                user1Button.setVisible(true);
                user1Button.setText(name);
                break;
            case(2):
                user2Button.setVisible(true);
                user2Button.setText(name);
                break;
            case(3):
                user3Button.setVisible(true);
                user3Button.setText(name);
                break;
            case(4):
                user4Button.setVisible(true);
                user4Button.setText(name);
                break;
            case(5):
                user5Button.setVisible(true);
                user5Button.setText(name);
                break;
            case(6):
                user6Button.setVisible(true);
                user6Button.setText(name);
                break;
        }
        setCount(userID + 1);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Button getUser1Button() {
        return user1Button;
    }

    public Button getUser2Button() {
        return user2Button;
    }

    public Button getUser3Button() {
        return user3Button;
    }

    public Button getUser4Button() {
        return user4Button;
    }

    public Button getUser5Button() {
        return user5Button;
    }

    public Button getUser6Button() {
        return user6Button;
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
