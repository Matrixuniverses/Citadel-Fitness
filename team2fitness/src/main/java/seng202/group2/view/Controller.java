package seng202.group2.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import seng202.group2.data_functions.FileFormatException;
import seng202.group2.data_functions.Parser;
import seng202.group2.development_code.TestDataGenerator;
import seng202.group2.model.Activity;
import seng202.group2.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Inject FXML
    @FXML
    private StackPane mainContainer;
    @FXML
    private AnchorPane navBar;

    //Initialize Panes to be added
    private AnchorPane mapViewScene;
    private AnchorPane addDataScene;
    private AnchorPane targetScene;
    private AnchorPane manageDataScene;
    private AnchorPane viewGraphScene;
    private AnchorPane profileView;
    private AnchorPane exitScene;
    private AnchorPane activityView;

    //Create Map of Panes for easy swapping
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();

    //Initialize Controllers
    @FXML
    private NavBarController navBarController;              //Used due to FXML Injection

    private ActivityViewController activityViewController;
    private ProfileController profileController;
    private AddDataController addDataController;
    private User user;

    //Initialize all Panes and Listeners
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeNavBar();
        initializeSelectFile();
        initializeActivityView();

        user = TestDataGenerator.createUser1();
        activityViewController.updateUserData(user);
        profileController.updateUserData(user);
    }

    //Initialize All Parts of the GUI and resulting controllers
    private void initializeViews(){
        try {

            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityView.fxml"));
            activityView = loader.load();
            activityViewController = loader.getController();
            paneMap.put("data", activityView);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMapView.fxml"));
            mapViewScene = loader.load();
            paneMap.put("mapView", mapViewScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLAddData.fxml"));
            addDataScene = loader.load();
            addDataController = loader.getController();
            paneMap.put("addData", addDataScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLTarget.fxml"));
            targetScene = loader.load();
            paneMap.put("target", targetScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLViewGraph.fxml"));
            viewGraphScene = loader.load();
            paneMap.put("viewGraph", viewGraphScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLProfileView.fxml"));
            profileView = loader.load();
            profileController = loader.getController();
            paneMap.put("summaryView", profileView);

            mainContainer.getChildren().addAll(activityView, mapViewScene, addDataScene, targetScene, viewGraphScene,
                    profileView);

        }
        catch (IOException ex_) {
            ex_.printStackTrace();
        }
    }

    //Initializes the navbar
    private void initializeNavBar(){
        navBarController.getCurrentView().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                paneMap.get(newValue).toFront();
            }
        });
    }

    //Initializes the activity view
    private void initializeActivityView(){
        activityViewController.getActivityDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ObservableList<Activity> activityList = activityViewController.getActivityTable().getSelectionModel().getSelectedItems();
                ArrayList<Activity> rows = new ArrayList<>(activityList);
                rows.forEach(row -> user.getActivityList().remove(row));
            }
        });
    }

    //Initializes the select file view
    private void initializeSelectFile(){
        addDataController.newFileProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (addDataController.getNewFile() != 0){
                    File importedFile = addDataController.getSelectedFile();
                    addDataController.setNewFile(0);
                    Parser parser = null;
                    try {
                        parser = new Parser(importedFile);
                    }
                    catch (FileFormatException f) {
                        f.printStackTrace();
                    }

                    user.getActivityList().addAll(parser.getActivitiesRead());

                }
            }
        });
        addDataController.getButtonSubmitData().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = addDataController.getTextFieldName().getText();
                String type = addDataController.getChoiceBoxType().getValue().toString();
                Double distance = Double.parseDouble(addDataController.getTextFieldDistance().getText());
                Double time = Double.parseDouble(addDataController.getTextFieldTime().getText());

                Activity userActivity = new Activity(name, type, distance, time);
                //user.

            }
        });
    }


}
