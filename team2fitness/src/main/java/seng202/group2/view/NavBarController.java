package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {


    private StringProperty currentView = new SimpleStringProperty();

    @FXML
    private Button logoutButton;

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
        currentView.set("Profile");
    }

    public void logout(){
        currentView.set("exit");
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
