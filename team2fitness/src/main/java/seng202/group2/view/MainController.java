package seng202.group2.view;

import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main container, responsible for initialising all Views
 */
public class MainController implements Initializable {

    private SimpleStringProperty status = new SimpleStringProperty("logout");
    private DataManager dataManager = DataManager.getDataManager();

    @FXML
    AnchorPane navBar;

    @FXML
    StackPane mainStack;

    @FXML
    AnchorPane mainAnchorPane;


    // Controllers
    private ActivityViewController activityViewController;
    private AddDataController addDataController;
    private TargetViewController targetViewController;
    private AddTargetController addTargetController;
    private EditTargetController editTargetController;
    private ViewGraphController viewGraphController;
    private ProfileController profileViewController;
    private MapViewController mapViewController;
    private ActivityInfoController activityInfoController;
    private EditProfileController editProfileController;
    private MapMyRunController mapMyRunController;
    private CalendarController calendarController;
    private ActivitiesFoundController activitiesFoundController;
    private RouteSelectController routeSelectController;
    private WarningPanelController warningPanelController;

    @FXML
    private HeaderController headerController;

    @FXML
    private NavBarController navBarController;


    // Views
    private AnchorPane activityView;
    private AnchorPane addDataView;
    private AnchorPane targetView;
    private AnchorPane viewGraphScene;
    private AnchorPane profileView;
    private AnchorPane mapView;
    private AnchorPane activityInfo;
    private AnchorPane addTargetView;
    private AnchorPane editTargetView;
    private AnchorPane editProfile;
    private AnchorPane mapMyRun;
    private AnchorPane calendarScene;
    private AnchorPane activitiesFoundScene;
    private AnchorPane routeSelect;
    private VBox warningPanel;

    // Allows nav bar to work easily
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();

    // Pulsy boi
    private AnimationTimer timer;
    private double pulseRate;
    private ImageView navBarLogo;


    /**
     * Initalizes the navbar and startup scenes
     * @param location URL
     * @param resources ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeNavBar();
        initializeActivityInfo();
        initializeWarningPanel();
    }

    /**
     * This adds all scenes to the mainStack so they can be called to front when required
     */
    private void initializeViews() {
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

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLTargetView.fxml"));
            targetView = loader.load();
            targetViewController = loader.getController();
            paneMap.put("Targets", targetView);

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

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLAddTarget.fxml"));
            addTargetView = loader.load();
            addTargetController = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditTarget.fxml"));
            editTargetView = loader.load();
            editTargetController = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLEditProfile.fxml"));
            editProfile = loader.load();
            editProfileController = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLCalendar.fxml"));
            calendarScene = loader.load();
            calendarController = loader.getController();
            paneMap.put("Calendar", calendarScene);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityInfo.fxml"));
            activityInfo = loader.load();
            activityInfoController = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMapMyRun.fxml"));
            mapMyRun = loader.load();
            mapMyRunController = loader.getController();
            paneMap.put("Map My Run", mapMyRun);


            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLWarningPanel.fxml"));
            warningPanel = loader.load();
            warningPanelController = loader.getController();
            warningPanel.setLayoutX(890);
            warningPanel.setLayoutY(55);
            mainAnchorPane.getChildren().add(warningPanel);
            warningPanel.setVisible(false);




            activityInfo.toFront();

            mainStack.getChildren().addAll(activityInfo, profileView, addDataView, activityView, targetView,
                    addTargetView, editTargetView, mapView, viewGraphScene, editProfile, mapMyRun, calendarScene);

            profileView.toFront();

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }

    }

    /**
     * This initializes the nav bar and adds a listener for navigation to new scenes
     */
    private void initializeNavBar() {
        navBarController.getCurrentView().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    paneMap.get(newValue).toFront();
                    headerController.getViewLabel().setText(newValue);
                    navBarController.getCurrentView().setValue("");
                }
            }
        });

        activityViewController.getPulser().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                navBarLogo = navBarController.getNavLogo();
                pulseRate = Double.parseDouble(newValue);

                ScaleTransition scaleTransition = new ScaleTransition();
                scaleTransition.setNode(navBarLogo);
                scaleTransition.setFromX(1);
                scaleTransition.setFromY(1);
                scaleTransition.setByX(0.2);
                scaleTransition.setByY(0.2);
                scaleTransition.setCycleCount(10);
                scaleTransition.setDuration(Duration.seconds(60/pulseRate));
                scaleTransition.setAutoReverse(true);
                scaleTransition.play();
            }
        });

        activityViewController.getActivityAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                navBarController.getCurrentView().set("Import Data");
            }
        });

        navBarController.getLogoutButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                status.set("logout");
                profileView.toFront();
            }
        });

        navBarController.getEditProfileButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editProfile.toFront();
            }
        });

        targetViewController.getAddTargetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addTargetView.toFront();
            }
        });

        targetViewController.getModifyTargetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Target selected = targetViewController.getTargetTable().getSelectionModel().getSelectedItem();
                if (selected != null) {
                    editTargetController.updateTargetFields(selected);
                    editTargetView.toFront();
                }
            }
        });

        addTargetController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addTargetView.toBack();
            }
        });

        editTargetController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editTargetView.toBack();
            }
        });

        editProfileController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editProfile.toBack();
            }
        });

        editProfileController.getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete User");
                alert.setHeaderText("Delete User");
                alert.setContentText("Are you sure? Deleting will remove all user data. This can't be undone!");
//                DialogPane dialogPane = alert.getDialogPane();
//                dialogPane.getStylesheets().add(getClass().getResource("resources/style/style.css").toExternalForm());
//                dialogPane.getStyleClass().add("dialog-pane");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    User currentUser =  DataManager.getDataManager().getCurrentUser();
                    DataManager.getDataManager().deleteUser(currentUser);
                    paneMap.get("Summary").toFront();
                    navBarController.getLogoutButton().fire();

                } else {
                    // ... user chose CANCEL or closed the dialog
                }

            }
        });

    }

    /**
     * Initializes Activity info as the home scene after login
     */
    private void initializeActivityInfo() {
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

        activityInfoController.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activityInfo.toBack();
            }
        });
    }

    /**
     * Initializes the Warning Panel Button/Icon
     */
    private void initializeWarningPanel() {
        headerController.getNotificationButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dataManager.newHealthWarningProperty().setValue(false);
                if (warningPanel.isVisible()) {
                    warningPanel.setVisible(false);
                } else {
                    warningPanel.setVisible(true);
                }
            }
        });
    }

    /**
     * resets summary scene to the first scene on login
     */
    public void summaryToFront(){
        profileView.toFront();
    }

    public Button getLogoutButton() {
        return navBarController.getLogoutButton();
    }

    public void closePopup(){
        activityViewController.closePopup();
        editTargetController.getCloseButton().fire();

    }
}
