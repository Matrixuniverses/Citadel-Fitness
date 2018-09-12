package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
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
import java.time.ZoneId;
import java.util.*;

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
    private AnchorPane editScene;

    //Create Map of Panes for easy swapping
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();

    //Initialize Controllers
    @FXML
    private NavBarController navBarController;              //Used due to FXML Injection

    private ActivityViewController activityViewController;
    private ProfileController profileController;
    private AddDataController addDataController;
    private EditProfileController editProfileController;

    private User user;

    //Initialize all Panes and Listeners
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeNavBar();
        initializeSelectFile();
        initializeActivityView();
        initializeEditProfileView();


        //user = TestDataGenerator.createUser1();
        //activityViewController.updateUserData(user);
        //profileController.updateUserData(user);
    }

    /**
     * Initialises all parts of the GUI and controllers.
     */
    private void initializeViews() {
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

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditProfile.fxml"));
            editScene = loader.load();
            editProfileController = loader.getController();
            paneMap.put("editScene", editScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLProfileView.fxml"));
            profileView = loader.load();
            profileController = loader.getController();
            paneMap.put("summaryView", profileView);

            mainContainer.getChildren().addAll(activityView, mapViewScene, addDataScene, targetScene, viewGraphScene,
                    editScene, profileView);

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }
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

    /**
     * Initialises the edit profile view.
     */
    private void initializeEditProfileView(){
        profileController.getEditProfileButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paneMap.get("editScene").toFront();
                editProfileController.getfNameField().setText(user.getName());
                editProfileController.getlNameField().setText(user.getName());
                //TODO: seperate first and last name
                editProfileController.getHeightField().setText(String.valueOf(user.getHeight()));
                editProfileController.getDobField().setText(String.valueOf(user.getAge()));
                editProfileController.getWeightField().setText(String.valueOf(user.getWeight()));
                editProfileController.getSaveChangesButton().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            user.setName(editProfileController.getfNameField().getText());
                            user.setHeight(Double.valueOf(editProfileController.getHeightField().getText()));
                            user.setAge(Integer.valueOf(editProfileController.getDobField().getText()));
                            user.setWeight(Double.valueOf(editProfileController.getWeightField().getText()));
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
                rows.forEach(row -> user.getActivityList().remove(row));
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

                    user.getActivityList().addAll(parser.getActivitiesRead());

                }
            }
        });

        /**
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
                        user.getActivityList().add(userActivity);

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
    