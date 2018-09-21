package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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
    private ActivityInfoController activityInfoController;
    private EditProfileController editProfileController;

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
    private AnchorPane activityInfo;
    private AnchorPane editProfile;

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
        activityInfoController.setDataManager(dataManager);
        editProfileController.setDataManager(dataManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeNavBar();
        initializeActivityInfo();
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

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditProfile.fxml"));
            editProfile = loader.load();
            editProfileController = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityInfo.fxml"));
            activityInfo = loader.load();
            activityInfoController = loader.getController();


            activityInfo.toFront();

            mainStack.getChildren().addAll(activityInfo, profileView, addDataView, activityView, viewGraphScene, mapView, editProfile);
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

        navBarController.getEditProfileButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editProfile.toFront();
            }
        });

        editProfileController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editProfile.toBack();
            }
        });

    }

    public void initializeActivityInfo(){
        activityViewController.getDetailButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Activity selected = activityViewController.getActivityTable().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    activityInfoController.updateActivity(selected);
                    activityInfo.toFront();
                }
            }
        });

        activityViewController.getActivityDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Activity selected = activityViewController.getActivityTable().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    dataManager.deleteActivity(selected);
                }
            }
        });

        activityInfoController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activityInfo.toBack();
            }
        });
    }

    public void updateUser(){
        profileView.toFront();
        headerController.getNameLabel().textProperty().bind(dataManager.getCurrentUser().nameProperty());
        activityViewController.updateUser();
        addDataController.updateUser();
        viewGraphController.updateUser();
        profileViewController.updateUser();
        mapViewController.updateUser();
        editProfileController.updateUser();

    }

    public Button getLogoutButton() {
        return navBarController.getLogoutButton();
    }

}
