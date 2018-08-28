package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {


    public AnchorPane getMainContainer() {
        return mainContainer;
    }

    public void setMainContainer(AnchorPane mainContainer) {
        this.mainContainer = mainContainer;
    }

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
    private AnchorPane mainContainer;
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


    public void insertInfoPane() {

    }


}
