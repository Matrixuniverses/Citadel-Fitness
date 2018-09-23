package seng202.group2.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import seng202.group2.data.DataParser;
import seng202.group2.data.FileFormatException;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;

import java.io.File;
import java.time.ZoneId;
import java.util.Date;
import java.util.InputMismatchException;

/**
 *Controller for AddData Scene
 */
public class AddDataController implements UserData {

    private File selectedFile;
    private IntegerProperty newFile = new SimpleIntegerProperty(0);
    private DataManager dataManager;

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
    private Button buttonSubmitData;

    @FXML
    private ChoiceBox choiceBoxType;

    public DatePicker getDateInput() {
        return dateInput;
    }

    public void setDateInput(DatePicker dateInput) {
        this.dateInput = dateInput;
    }

    public ChoiceBox getChoiceBoxType() {
        return choiceBoxType;
    }

    public void setChoiceBoxType(ChoiceBox choiceBoxType) {
        this.choiceBoxType = choiceBoxType;
    }

    public Button getButtonSubmitData() {
        return buttonSubmitData;
    }

    public void setButtonSubmitData(Button buttonSubmitData) {
        this.buttonSubmitData = buttonSubmitData;
    }

    public TextField getTextFieldName() {
        return textFieldName;
    }

    public void setTextFieldName(TextField textFieldName) {
        this.textFieldName = textFieldName;
    }

    /*public TextField getTextFieldDate() {
        return textFieldDate;
    }

    public void setTextFieldDate(TextField textFieldDate) {
        this.textFieldDate = textFieldDate;
    }*/

    public TextField getTextFieldDistance() {
        return textFieldDistance;
    }

    public void setTextFieldDistance(TextField textFieldDistance) {
        this.textFieldDistance = textFieldDistance;
    }

    public TextField getTextFieldTime() {
        return textFieldTime;
    }

    public void setTextFieldTime(TextField textFieldTime) {
        this.textFieldTime = textFieldTime;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public int getNewFile() {
        return newFile.get();
    }

    public IntegerProperty newFileProperty() {
        return newFile;
    }

    public void setNewFile(int newFile) {
        this.newFile.set(newFile);
    }

    public void selectFileAction(ActionEvent event){
        FileChooser fc = new FileChooser();

        selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            DataParser parser;
            try {
                parser = new DataParser(selectedFile);
                dataManager.addActivities(parser.getActivitiesRead());
                raiseSuccess("Activities added.");
            } catch (FileFormatException f) {
                raiseError("Data Parsing Error", "File could not be uploaded.");
            }
        }
    }
    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;

        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Run");
        typeOptions.add("Walk");
        typeOptions.add("Cycle");
        typeOptions.add("Swim");
        choiceBoxType.setItems(typeOptions);
        choiceBoxType.setValue("Run");
    }

    @Override
    public void updateUser() {

    }

    /**
     * Button for manual data entry.
     * Creates an activity object with the data the user entered. Adds it to the user's activities list.
     * If the user enters invalid information, an alert is opened with error message.
     */
    public void addManualData(){
        try {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            String name = textFieldName.getText();
            String type = choiceBoxType.getValue().toString();
            if (name.length() == 0) {
                throw new IllegalArgumentException("Activity must be named.");
            }
            Double distance = Double.parseDouble(textFieldDistance.getText());
            if (distance < 0) {
                throw new IllegalArgumentException("The distance value cannot be negative.");
            }
            Double time = Double.parseDouble(textFieldTime.getText());
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


    private void raiseError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void raiseSuccess(String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
