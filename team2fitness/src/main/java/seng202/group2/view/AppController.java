package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import seng202.group2.model.*;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {

    //Inject FXML
    @FXML
    private StackPane appStack;

    //Initialize Panes to be added
    private AnchorPane loginScene;
    private BorderPane mainScene;

    //Initializes Controllers
    private LoginController loginSceneController;
    private MainController mainSceneController;

    //Setup Data Manager

    private DataManager dataManager;

    //Initialize all Panes and Listeners
    public void initialize(URL location, ResourceBundle resources) {
        dataManager = new DataManager();
        initializeViews();
        setupViews();
    }


    /**
     * Initialises all parts of the GUI and controllers.
     */
    private void initializeViews() {
        try {

            FXMLLoader loader;

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLLogin.fxml"));
            loginScene = loader.load();
            loginSceneController = loader.getController();
            loginSceneController.setDataManager(dataManager);
            appStack.getChildren().add(loginScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMain.fxml"));
            mainScene = loader.load();
            mainSceneController = loader.getController();
            appStack.getChildren().add(mainScene);
            mainSceneController.setDataManager(dataManager);



            loginScene.toFront();

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }
    }

    private void setupViews(){
        loginSceneController.statusProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("logged in")) {

                    mainSceneController.updateViews();
                    loginScene.toBack();
                    mainScene.toFront();
                }
            }
        });
    }

}
    