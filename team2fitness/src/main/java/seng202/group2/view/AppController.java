package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This is the controller for the main app responsible for initalizing the login and main scenes and listeners
 */
public class AppController implements Initializable {

    //Inject FXML
    @FXML
    private StackPane appStack;

    //Initialize Panes to be added
    private AnchorPane loginScene;
    private AnchorPane mainScene;

    //Initializes Controllers
    private LoginController loginSceneController;
    private MainController mainSceneController;

    /**
     * Initalises all panes and Listeners
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        setupViews();
    }


    /**
     * Initialises all parts of the GUI and controllers.
     * Loads the mainScene, and loginScene. All other scenes are loaded as children of main
     * Pulls login screen to front on first load
     */
    private void initializeViews() {
        try {
            FXMLLoader loader;

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLLogin.fxml"));
            loginScene = loader.load();
            loginSceneController = loader.getController();
            appStack.getChildren().add(loginScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMain.fxml"));
            mainScene = loader.load();
            mainSceneController = loader.getController();
            appStack.getChildren().add(mainScene);



            loginScene.toFront();

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }
    }

    /**
     * Sets up the logout/login buttons.
     * login() is called when the string property is changed in the login scene.
     */
    private void setupViews(){
        loginSceneController.statusProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("logged in")) {
                    login();
                }
            }
        });

        mainSceneController.getLogoutButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainSceneController.closePopup();
                logout();
            }
        });

    }

    /**
     * Called when the user clicks the logout button.
     *
     */
    private void logout(){
        loginScene.toFront();
        mainSceneController.summaryToFront();
        loginSceneController.statusProperty().setValue("logged out");
    }

    /**
     * Called when the user clicks the login button or selected a User
     * in the login view.
     */
    private void login(){
        mainScene.toFront();
    }

}
    