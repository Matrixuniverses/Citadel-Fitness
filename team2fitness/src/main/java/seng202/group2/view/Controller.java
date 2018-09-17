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
import seng202.group2.model.Activity;
import seng202.group2.model.Route;
import seng202.group2.model.User;
import seng202.group2.model.UserDBOperations;

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

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser;




    //Initialize all Panes and Listeners
    public void initialize(URL location, ResourceBundle resources) {

        User testUser = new User(1, "Adam Conway", 20, 170, 70);

        initializeViews();
        initializeLoginScene();
        initializeCreateProfileScene();
        initializeNavBar();
        initializeSelectFile();
        initializeActivityView();
        initializeEditProfileView();

        initializeMapView();

        try {
            userList = UserDBOperations.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userList.add(testUser);

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

        loginSceneController.getNewUserButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createProfileScene.toFront();

            }
        });


        ObservableList<Button> buttonList = loginSceneController.getButtonList();


        userList.addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                // Sets actions for select user buttons.
                for (int i = 0; i < buttonList.size(); i++) {
                    int number = i;
                    buttonList.get(number).setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            currentUser = userList.get(number);
                            activityViewController.updateUserData(currentUser);
                            profileController.updateUserData(currentUser);
                            loginScene.toBack();
                            createProfileScene.toBack();
                            mainContainer.toFront();
                        }
                    });
                    buttonList.get(i).setVisible(false);
                }

                for (int i = 0 ; i < userList.size() ; i++) {
                    buttonList.get(i).setVisible(true);
                    buttonList.get(i).textProperty().bind(userList.get(i).nameProperty());
                }
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

                System.out.println(currentUser.getBmi());
            }
        });
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
                    User user = new User(-1, name, age, height, weight);
                    user.setId(UserDBOperations.insertNewUser(user));
                    userList.add(user);
                    createProfileScene.toBack();

                } catch (NumberFormatException e) {
                    raiseError("Error dialog", "Time and distance must be numbers");
                } catch (InputMismatchException e) {
                    raiseError("Error dialog", "Must select a date");
                } catch (IllegalArgumentException e) {
                    raiseError("Error dialog", "Activity must be named");
                } catch (SQLException e) {
                    raiseError("Database Error", "Could not read from database");
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
                editProfileController.getfNameField().setText(currentUser.getName());
                editProfileController.getlNameField().setText(currentUser.getName());
                //TODO: seperate first and last name
                editProfileController.getHeightField().setText(String.valueOf(currentUser.getHeight()));
                editProfileController.getDobField().setText(String.valueOf(currentUser.getAge()));
                editProfileController.getWeightField().setText(String.valueOf(currentUser.getWeight()));
                editProfileController.getSaveChangesButton().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            currentUser.setName(editProfileController.getfNameField().getText());
                            currentUser.setHeight(Double.valueOf(editProfileController.getHeightField().getText()));
                            currentUser.setAge(Integer.valueOf(editProfileController.getDobField().getText()));
                            currentUser.setWeight(Double.valueOf(editProfileController.getWeightField().getText()));
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
        activityViewController.getActivityDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ObservableList<Activity> activityList = activityViewController.getActivityTable().getSelectionModel().getSelectedItems();
                ArrayList<Activity> rows = new ArrayList<>(activityList);
                rows.forEach(row -> currentUser.getActivityList().remove(row));
            }
        });
    }

    /**
     * Initialises the select file view.
     */
    private void initializeSelectFile() {

        ChoiceBox choiceBoxtype = addDataController.getChoiceBoxType();

        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        choiceBoxtype.setItems(typeOptions);
        choiceBoxtype.setValue("Run");

        addDataController.newFileProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (addDataController.getNewFile() != 0) {
                    File importedFile = addDataController.getSelectedFile();
                    addDataController.setNewFile(0);
                    Parser parser = null;
                    try {
                        parser = new Parser(importedFile);
                    } catch (FileFormatException f) {
                        f.printStackTrace();
                    }

                    currentUser.getActivityList().addAll(parser.getActivitiesRead());

                    // Check if this is correct
                    mapViewController.updateUserData(currentUser);

                }
            }
        });

        /**
         * Button for manual data entry.
         * Creates an activity object with the data the user entered. Adds it to the user's activities list.
         * If the user enters invalid information, an alert is opened with error message.
         */
        addDataController.getButtonSubmitData().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    String name = addDataController.getTextFieldName().getText();
                    String type = addDataController.getChoiceBoxType().getValue().toString();
                    Double distance = Double.parseDouble(addDataController.getTextFieldDistance().getText());
                    Double time = Double.parseDouble(addDataController.getTextFieldTime().getText());

                    if (addDataController.getDateInput().getValue() == null) {
                        throw new InputMismatchException();
                    } else if (name.length() == 0) {
                        throw new IllegalArgumentException();
                    } else {
                        Date date = Date.from(addDataController.getDateInput().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Activity userActivity = new Activity(name, date, type, time, distance);
                        currentUser.getActivityList().add(userActivity);

                        // Check if this is correct
                        mapViewController.updateUserData(currentUser);

                        //Clear fields
                        addDataController.getTextFieldName().setText(null);
                        addDataController.getTextFieldDistance().setText(null);
                        addDataController.getTextFieldTime().setText(null);
                        addDataController.getDateInput().setValue(null);
                    }
                } catch (NumberFormatException e) {
                    raiseError("Error dialog", "Time and distance must be numbers");
                } catch (InputMismatchException e) {
                    raiseError("Error dialog", "Must select a date");
                } catch (IllegalArgumentException e) {
                    raiseError("Error dialog", "Activity must be named");
                }


            }
        });
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
    