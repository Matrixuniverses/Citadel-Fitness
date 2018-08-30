package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public AnchorPane getMapViewScene() {
        return mapViewScene;
    }

    public void setMapViewScene(AnchorPane mapViewScene) {
        this.mapViewScene = mapViewScene;
    }

    public AnchorPane getAddDataScene() {
        return addDataScene;
    }

    public void setAddDataScene(AnchorPane addDataScene) {
        this.addDataScene = addDataScene;
    }

    public AnchorPane getTargetScene() {
        return targetScene;
    }

    public void setTargetScene(AnchorPane targetScene) {
        this.targetScene = targetScene;
    }

    public AnchorPane getManageDataScene() {
        return manageDataScene;
    }

    public void setManageDataScene(AnchorPane manageDataScene) {
        this.manageDataScene = manageDataScene;
    }

    public AnchorPane getViewGraphScene() {
        return viewGraphScene;
    }

    public void setViewGraphScene(AnchorPane viewGraphScene) {
        this.viewGraphScene = viewGraphScene;
    }

    public AnchorPane getMyProfileScene() {
        return myProfileScene;
    }

    public void setMyProfileScene(AnchorPane myProfileScene) {
        this.myProfileScene = myProfileScene;
    }

    public AnchorPane getExitScene() {
        return exitScene;
    }

    public void setExitScene(AnchorPane exitScene) {
        this.exitScene = exitScene;
    }

    @FXML
    private StackPane mainContainer;
    @FXML
    private AnchorPane mapViewScene;
    @FXML
    private AnchorPane addDataScene;
    @FXML
    private AnchorPane targetScene;
    @FXML
    private AnchorPane manageDataScene;
    @FXML
    private AnchorPane viewGraphScene;
    @FXML
    private AnchorPane myProfileScene;
    @FXML
    private AnchorPane exitScene;
    @FXML
    private AnchorPane navBar;
    @FXML
    private Label testText;

    @FXML
    private NavBarController navBarController;


    public void insertInfoPane() {

    }


    public void initialize(URL location, ResourceBundle resources) {


//        navBarController.setMainController(this);
  //      navBarController.getValue().addListener((observable, oldValue, newValue));
        navBarController.getCurrentView().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("target")) {
                    mainContainer.getChildren().add(targetScene);
                }
            }
        });
    }
}
