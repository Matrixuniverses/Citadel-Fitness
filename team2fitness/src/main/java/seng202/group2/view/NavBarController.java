package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {

    private Controller mainController;

    public StringProperty getValue() {
        return value;

    }

    public StringProperty valueProperty() {
        return value;
    }

    private StringProperty value = new SimpleStringProperty();

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

    public void loadtargetScene(){
        value.set("target");

//        mainController.getMainContainer().getChildren().clear();
//        mainController.getMainContainer().getChildren().add(mainController.getTargetScene());


    }

    public void setMainController(Controller controller){
        this.mainController = controller;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
