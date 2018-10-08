package seng202.group2.view;

//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import seng202.group2.data.DataParser;
import seng202.group2.data.FileFormatException;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
//import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Controller for AddData Scene
 */
public class AddDataController implements Initializable  {

    private Executor executionThreads;
    //private IntegerProperty newFile = new SimpleIntegerProperty(0);
    private DataManager dataManager = DataManager.getDataManager();

    @FXML
    private Button selectFileButton;

    @FXML
    private TextField textFieldName;

    @FXML
    private DatePicker dateInput;

    @FXML
    private TextField textFieldDistance;

    @FXML
    private TextField textFieldTime;

    @FXML
    private Label errorLabel;

    @FXML
    private Label importInfoLabel;

    @FXML
    private ChoiceBox choiceBoxType;

    @FXML
    private AnchorPane addDataPane;

    private AnchorPane routeSelectPane;

    private RouteSelectController routeSelectController;

    private Double routeDistance;

    private Double routeTime;

    private AnchorPane activitiesFoundScene;
    private ActivitiesFoundController activitiesFoundController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        choiceBoxType.setItems(typeOptions);
        choiceBoxType.setValue("Run");


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLRouteSelect.fxml"));
            routeSelectPane  = loader.load();
            routeSelectController = loader.getController();
            addDataPane.getChildren().add(routeSelectPane);
            routeSelectPane.toBack();
            routeSelectPane.setVisible(false);

            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivitiesFound.fxml"));
            activitiesFoundScene = loader.load();
            activitiesFoundController = loader.getController();
            addDataPane.getChildren().add(activitiesFoundScene);
            activitiesFoundScene.toBack();
            activitiesFoundScene.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }



        routeSelectController.getConfirmButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                routeDistance = routeSelectController.getRouteDistance();
                routeTime = routeSelectController.getRouteTime() * 60;
                routeSelectController.resetMap();
                routeSelectPane.toBack();
                routeSelectPane.setVisible(false);
                textFieldDistance.setText(routeDistance.toString());
                textFieldTime.setText(routeTime.toString());
            }
        });

        routeSelectController.getCancelButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                routeSelectController.resetMap();
                routeSelectPane.toBack();
                routeSelectPane.setVisible(false);
            }
        });

        // Multithreading
        executionThreads = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                reset();
            }
        });


        activitiesFoundController.getImportButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activitiesFoundScene.toBack();
                activitiesFoundScene.setVisible(false);
                for (Activity activity : activitiesFoundController.getActivityList()) {
                    if (activity.getChecked()) {
                        dataManager.addActivity(activity);
                    }
                }

                importInfoLabel.setVisible(true);
                importInfoLabel.setTextFill(Color.GREEN);
                importInfoLabel.setText("Activities added successfully!");

            }
        });

        activitiesFoundController.getCancelButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                importInfoLabel.setText("");
                activitiesFoundScene.toBack();
                activitiesFoundScene.setVisible(false);
            }
        });


    }

    @FXML
    protected void selectFileAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        final File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            addData(selectedFile);
        }
        event.consume();
    }

    private void addData(File fileToRead) {
        Task<DataParser> addDataTask = new Task<DataParser>() {
            @Override
            protected DataParser call() throws Exception {
                return new DataParser(fileToRead);
            }
        };

        addDataTask.setOnSucceeded(succeededEvent -> {
            activitiesFoundController.update(addDataTask.getValue());
            activitiesFoundScene.setVisible(true);
            activitiesFoundScene.toFront();

        });

        addDataTask.setOnFailed(failedEvent -> {
            importInfoLabel.setVisible(true);
            importInfoLabel.setTextFill(Color.RED);
            importInfoLabel.setText(addDataTask.getException().getMessage());
        });

        importInfoLabel.setText("Reading file");
        executionThreads.execute(addDataTask);
    }

    /**
     * Button for manual data entry.
     * Creates an activity object with the data the user entered. Adds it to the user's activities list.
     * If the user enters invalid information, an alert is opened with error message.
     */
/*    public void addManualData() {
        try {
            importInfoLabel.setVisible(false);
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            String name = textFieldName.getText();
            String type = choiceBoxType.getValue().toString();
            if (name.length() == 0) {
                throw new IllegalArgumentException("Activity must be named.");
            }
            double distance = Double.parseDouble(textFieldDistance.getText());
            if (distance < 0) {
                throw new IllegalArgumentException("The distance value cannot be negative.");
            }
            double time = Double.parseDouble(textFieldTime.getText());
            if (time < 0) {
                throw new IllegalArgumentException("The time value cannot be negative.");
            }

            if (dateInput.getValue() == null) {
                throw new InputMismatchException();
            } else {
                Date date = Date.from(dateInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                Activity userActivity = new Activity(name, date, type, time, distance);
                dataManager.addActivity(userActivity);
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Activity added successfully.");

                //Clear fields
                reset();
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Time and Distance must be numbers.");
        } catch (InputMismatchException e) {
            errorLabel.setText("Must select a date.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }*/

    /**
     * Button for manual data entry.
     * Creates an activity object with the data the user entered. Adds it to the user's activities list.
     * If the user enters invalid information, an alert is opened with error message.
     */
    public void addManualData() {
        errorLabel.setTextFill(Color.RED);

        boolean valid = true;
        String name;
        Date date;
        double distance = 0;
        double time = 0;

        String type = choiceBoxType.getValue().toString();

        // Check name
        name = textFieldName.getText();
        if (name == null || name.length() == 0) {
            errorLabel.setVisible(true);
            errorLabel.setText("Activity must have a name.");
            valid = false;
        }

        // Check date
        if (dateInput.getValue() == null) {
            errorLabel.setVisible(true);
            errorLabel.setText("Activity must have a date.");
            valid = false;
        }

        // Check distance
        try {
            distance = Double.parseDouble(textFieldDistance.getText());
            if (distance < 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Distance cannot be negative.");
                valid = false;
            }

        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Distance must be numeric.");
            valid = false;
        } catch (NullPointerException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Activity must have a distance.");
            valid = false;
        }

        // Check time
        try {
            time = Double.parseDouble(textFieldTime.getText());
            if (time < 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Time cannot be negative.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Time must be numeric.");
            valid = false;
        } catch (NullPointerException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Activity must have a time.");
            valid = false;
        }

        // Add activity, if input clears all checks.
        if (valid) {
            date = Date.from(dateInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Activity userActivity = new Activity(name, date, type, time, distance);
            dataManager.addActivity(userActivity);
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Activity added successfully.");

            //Clear fields
            reset();
        }
    }



    /**
     * Resets the manual entry fields to empty
     */
    public void reset(){
        //Clear fields
        textFieldName.setText(null);
        textFieldDistance.setText(null);
        textFieldTime.setText(null);
        dateInput.setValue(null);
    }

    @FXML
    public void dragEntered(final DragEvent event) {
        selectFileButton.setText("Drop File");
        selectFileButton.getStyleClass().setAll("button", "main-panel", "dragStyle");
        event.consume();
    }

    @FXML
    public void dragOver(final DragEvent event) {
        Dragboard board = event.getDragboard();

        if (board.hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        }
    }

    @FXML
    public void dragDropped(final DragEvent event) {
        Dragboard board = event.getDragboard();

        if (board.hasFiles()) {
            addData(board.getFiles().get(0));
        }
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    public void dragExited(final DragEvent event) {
        selectFileButton.setText("Select File");
        selectFileButton.getStyleClass().setAll("button", "main-panel");
        event.consume();
    }

    /**
     * Brings the Route select pane up. Allows the user to select distance/time through clicking a route on map.
     */
    public void openSelectRoute() {
        routeSelectPane.toFront();
        routeSelectPane.setVisible(true);
    }


}
