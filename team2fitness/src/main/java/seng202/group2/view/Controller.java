package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import seng202.group2.data_functions.FileFormatException;
import seng202.group2.data_functions.Parser;
import seng202.group2.development_code.TestDataGenerator;
import seng202.group2.model.*;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

public class Controller implements Initializable {

    //Inject FXML
    @FXML
    private StackPane mainContainer;
    @FXML
    private AnchorPane navBar;
    @FXML
    private AnchorPane appContainer;

    //Initialize Panes to be added
    private AnchorPane mapViewScene;
    private AnchorPane addDataScene;
    private AnchorPane targetScene;
    private AnchorPane manageDataScene;
    private AnchorPane viewGraphScene;
    private AnchorPane profileView;
    private AnchorPane exitScene;
    private AnchorPane activityView;
    private AnchorPane editScene;
    private AnchorPane loginScene;
    private AnchorPane createProfileScene;
    //Create Map of Panes for easy swapping
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();

    //Initialize Controllers
    @FXML
    private NavBarController navBarController;              //Used due to FXML Injection

    private ActivityViewController activityViewController;
    private ProfileController profileController;
    private AddDataController addDataController;
    private EditProfileController editProfileController;
    private LoginController loginSceneController;
    private CreateProfileController createProfileController;
    private MapViewController mapViewController;


    private DataManager dataManager = new DataManager();

    private ObservableList<User> userList = dataManager.getUserList();
   // private User currentUser;




    //Initialize all Panes and Listeners
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeLoginScene();
        initializeCreateProfileScene();
        initializeNavBar();
        initializeSelectFile();
        initializeActivityView();
        initializeEditProfileView();
        initializeProfileScene();

        initializeMapView();

//        dataManager.addUser("Bob Johnson", 30, 190, 100);
//        dataManager.addUser("John Smith", 20, 165, 70);

        //currentUser = TestDataGenerator.createUser1();
        //activityViewController.updateUserData(currentUser);
        //profileController.updateUserData(currentUser);
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
            appContainer.getChildren().add(loginScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLCreateProfile.fxml"));
            createProfileScene = loader.load();
            createProfileController = loader.getController();
            appContainer.getChildren().add(createProfileScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityView.fxml"));
            activityView = loader.load();
            activityViewController = loader.getController();
            paneMap.put("data", activityView);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMapView.fxml"));
            mapViewScene = loader.load();
            mapViewController = loader.getController();
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

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditProfile.fxml"));
            editScene = loader.load();
            editProfileController = loader.getController();
            paneMap.put("editScene", editScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLProfileView.fxml"));
            profileView = loader.load();
            profileController = loader.getController();
            paneMap.put("summaryView", profileView);

            paneMap.put("exit", loginScene);

            mainContainer.getChildren().addAll(activityView, mapViewScene, addDataScene, targetScene, viewGraphScene,
                    editScene, profileView);

            loginScene.toFront();

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }
    }

    /**
     * Initialises LoginScene.
     */
    private void initializeLoginScene(){

        loginSceneController.updateUserData(dataManager);

        loginSceneController.statusProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("logged in")){
                    mainContainer.toFront();
                    loginScene.toBack();
                    createProfileScene.toBack();
                    loginSceneController.statusProperty().setValue("none");
                }
            }
        });

        loginSceneController.getNewUserButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createProfileScene.toFront();

            }
        });


    }

    /**
     * Initialises the nav bar.
     */
    private void initializeNavBar() {
        navBarController.getCurrentView().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                paneMap.get(newValue).toFront();
            }
        });
    }

    private void initializeProfileScene() {
        profileController.updateUserData(dataManager);
    }


    private void initializeGraphView()  {

    }
    private void initializeCreateProfileScene(){
        createProfileController.getCreateButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    String name = createProfileController.getfNameField().getText();
                    Integer age = Integer.valueOf(createProfileController.getDobField().getText());
                    Double height = Double.valueOf(createProfileController.getHeightField().getText());
                    Float weight = Float.valueOf(createProfileController.getWeightField().getText());
                    dataManager.addUser(name, age, height, weight);
                    createProfileScene.toBack();

                } catch (NumberFormatException e) {
                    raiseError("Error dialog", "Time and distance must be numbers");
                } catch (InputMismatchException e) {
                    raiseError("Error dialog", "Must select a date");
                } catch (IllegalArgumentException e) {
                    raiseError("Error dialog", "Activity must be named");
                }
            }
        });

        createProfileController.getCancel().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createProfileScene.toBack();
            }
        });
    }

    private void initializeMapView() {


        mapViewController.getShowRouteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Activity selectedActivity = mapViewController.getActivityTable().getSelectionModel().getSelectedItem();
                WebEngine webEngine = mapViewController.getWebEngine();

                Route path = new Route(selectedActivity.getActivityData());
                String scriptToExecute = "displayRoute(" + path.toJSONArray() + ");";
                webEngine.executeScript(scriptToExecute);
            }
        });
    }

    /**
     * Initialises the edit profile view.
     */
    private void initializeEditProfileView(){
        profileController.getEditProfileButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paneMap.get("editScene").toFront();
                editProfileController.getfNameField().setText(dataManager.getCurrentUser().getName());
                editProfileController.getlNameField().setText(dataManager.getCurrentUser().getName());
                //TODO: seperate first and last name
                editProfileController.getHeightField().setText(String.valueOf(dataManager.getCurrentUser().getHeight()));
                editProfileController.getDobField().setText(String.valueOf(dataManager.getCurrentUser().getAge()));
                editProfileController.getWeightField().setText(String.valueOf(dataManager.getCurrentUser().getWeight()));
                editProfileController.getSaveChangesButton().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            dataManager.getCurrentUser().setName(editProfileController.getfNameField().getText());
                            dataManager.getCurrentUser().setHeight(Double.valueOf(editProfileController.getHeightField().getText()));
                            dataManager.getCurrentUser().setAge(Integer.valueOf(editProfileController.getDobField().getText()));
                            dataManager.getCurrentUser().setWeight(Double.valueOf(editProfileController.getWeightField().getText()));
                            paneMap.get("editScene").toBack();
                        }catch (Exception f) {
                            System.out.println(f);
                        }
                    }
                });
            }


        });

    }


    /**
     * Initialises the activity view.
     */
    private void initializeActivityView() {
        activityViewController.updateUserData(dataManager);

//        activityViewController.getActivityDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                ObservableList<Activity> activityList = activityViewController.getActivityTable().getSelectionModel().getSelectedItems();
//                ArrayList<Activity> rows = new ArrayList<>(activityList);
//                rows.forEach(row -> dataManager.getCurrentUser().getActivityList().remove(row));
//            }
//        });
    }

    /**
     * Initialises the select file view.
     */
    private void initializeSelectFile() {
        addDataController.updateUserData(dataManager);
    }

    /**
     * Raises an alert panel when the user enters data incorrectly.
     * @param header The message to be displayed in the alert's header
     * @param content The message to be displayed as the alert's content
     */
    private void raiseError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
    