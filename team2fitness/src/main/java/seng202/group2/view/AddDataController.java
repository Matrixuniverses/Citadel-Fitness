package seng202.group2.view;

//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import seng202.group2.data.DataParser;
import seng202.group2.data.FileFormatException;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;

import java.io.File;
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
public class AddDataController implements Initializable, UserData {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        choiceBoxType.setItems(typeOptions);
        choiceBoxType.setValue("Run");

        // Multithreading
        executionThreads = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
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
            dataManager.addActivities(addDataTask.getValue().getActivitiesRead());
            importInfoLabel.setVisible(true);
            importInfoLabel.setTextFill(Color.GREEN);
            importInfoLabel.setText("Activities added successfully!");

        });

        addDataTask.setOnFailed(failedEvent -> {
            importInfoLabel.setTextFill(Color.RED);
            importInfoLabel.setText("Error reading data from file.");
        });

        importInfoLabel.setText("Reading file");
        executionThreads.execute(addDataTask);
    }

    @Override
    public void updateUser() {

    }

    /**
     * Button for manual data entry.
     * Creates an activity object with the data the user entered. Adds it to the user's activities list.
     * If the user enters invalid information, an alert is opened with error message.
     */
    public void addManualData() {
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
                DataManager.getDataManager().addActivity(userActivity);
                dataManager.addActivity(userActivity);
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Activity added successfully.");

                //Clear fields
                textFieldName.setText(null);
                textFieldDistance.setText(null);
                textFieldTime.setText(null);
                dateInput.setValue(null);
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Time and Distance must be numbers.");
        } catch (InputMismatchException e) {
            errorLabel.setText("Must select a date.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
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
}
