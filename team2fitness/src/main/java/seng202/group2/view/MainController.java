package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import seng202.group2.model.DataManager;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;

public class MainController implements UserData, Initializable {

    private SimpleStringProperty status = new SimpleStringProperty("loggedout");
    private DataManager dataManager;

    @FXML
    AnchorPane navBar;

    @FXML
    StackPane mainStack;


    // Controllers
    private ActivityViewController activityViewController;
    private AddDataController addDataController;
    private ViewGraphController viewGraphController;
    private ProfileController profileViewController;
    private MapViewController mapViewController;

    @FXML
    private HeaderController headerController;

    @FXML
    private NavBarController navBarController;


    // Views

    private AnchorPane activityView;
    private AnchorPane addDataView;
    private AnchorPane viewGraphScene;
    private AnchorPane profileView;
    private AnchorPane mapView;

    // Allows nav bar to work easily
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();



    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
        activityViewController.setDataManager(dataManager);
        addDataController.setDataManager(dataManager);
        viewGraphController.setDataManager(dataManager);
        profileViewController.setDataManager(dataManager);
        mapViewController.setDataManager(dataManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeNavBar();
    }

    private void initializeViews(){
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityView.fxml"));
            activityView = loader.load();
            activityViewController = loader.getController();
            paneMap.put("Activities", activityView);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLAddData.fxml"));
            addDataView = loader.load();
            addDataController = loader.getController();
            paneMap.put("Import Data", addDataView);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLViewGraph.fxml"));
            viewGraphScene = loader.load();
            viewGraphController = loader.getController();
            paneMap.put("Graphs", viewGraphScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLProfileView.fxml"));
            profileView = loader.load();
            profileViewController = loader.getController();
            paneMap.put("Summary", profileView);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMapView.fxml"));
            mapView = loader.load();
            mapViewController = loader.getController();
            paneMap.put("Maps", mapView);


            mainStack.getChildren().addAll(profileView, addDataView, activityView, viewGraphScene, mapView);
            profileView.toFront();

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }

    }

    public void initializeNavBar() {
        navBarController.getCurrentView().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    paneMap.get(newValue).toFront();
                    headerController.getViewLabel().setText(newValue);
                    navBarController.getCurrentView().setValue("");
                }
            }
        });

        navBarController.getLogoutButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                status.set("loggedout");
            }
        });

    }

    public void updateUser(){
        profileView.toFront();
        headerController.getNameLabel().setText(dataManager.getCurrentUser().getName());
        activityViewController.updateUser();
        addDataController.updateUser();
        viewGraphController.updateUser();
        profileViewController.updateUser();
        mapViewController.updateUser();

    }

    public Button getLogoutButton() {
        return navBarController.getLogoutButton();
    }

}
