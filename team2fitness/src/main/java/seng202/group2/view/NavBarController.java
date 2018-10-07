package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @FXML
    private ImageView navLogo;


    public StringProperty getCurrentView() {
        return currentView;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void showAddData(){
        currentView.set("Import Data");
    }

    public void showAddActivity(){
        currentView.set("Activities");
    }

    public void showTarget(){
        currentView.set("Targets");
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

    public void showMapMyRun() { currentView.set("Map My Run");}

    public void showRouteSelect() {
        currentView.set("Route Select");
    }

    public Button getEditProfileButton() {
        return editProfileButton;
    }

    public ImageView getNavLogo() {return navLogo;
    }

    public void initialize(URL location, ResourceBundle resources) {
        Image logo = new Image("/images/citadelLogoB.png");
        navLogo.setImage(logo);

    }
}
