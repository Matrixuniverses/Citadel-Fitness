package seng202.group2.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddDataController {

    private File selectedFile;
    private IntegerProperty newFile = new SimpleIntegerProperty(0);

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
            newFile.setValue(1);
        } else {
            System.out.println("File is not valid");
        }
    }
}
