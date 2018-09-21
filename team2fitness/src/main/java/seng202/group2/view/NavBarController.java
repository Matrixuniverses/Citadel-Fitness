package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the Scene navigation bar
 */
public class NavBarController implements Initializable {


    private StringProperty currentView = new SimpleStringProperty();

    @FXML
    private Button logoutButton;

    @FXML
    private Button editProfileButton;


    public StringProperty getCurrentView() {
        return currentView;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void showTarget(){
        currentView.set("Targets");
    }

    public void showAddData(){
        currentView.set("Import Data");
    }

    public void showAddActivity(){
        currentView.set("Activities");
    }

    public void showGraph(){
        currentView.set("Graphs");
    }

    public void showMap(){
        currentView.set("Maps");
    }

    public void showSummary(){
        currentView.set("Summary");
    }

    public Button getEditProfileButton() {
        return editProfileButton;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
