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
    private Button targetLink;

    @FXML
    private Button AddDataLink;

    @FXML
    private Button manageDataLink;

    @FXML
    private Button viewGraphLink;

    @FXML
    private Button mapViewLink;

    @FXML
    private Button myProfileLink;

    @FXML
    private Button exitLink;

    public StringProperty getCurrentView() {
        return currentView;
    }

    public void showTarget(){
        currentView.set("target");
    }

    public void showAddDate(){
        currentView.set("addData");
    }

    public void showManageData(){
        currentView.set("manageData");
    }

    public void showViewGraph(){
        currentView.set("viewGraph");
    }

    public void showMapView(){
        currentView.set("mapView");
    }

    public void showMyProfile(){
        currentView.set("myProfile");
    }

    public void exit(){
        currentView.set("exit");
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
