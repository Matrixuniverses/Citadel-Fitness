package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {
    @FXML
    private Controller mainController;

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
        //Controller.insertInfoPane().load()
        //return

    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
