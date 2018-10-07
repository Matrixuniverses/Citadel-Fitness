package seng202.group2.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;
import seng202.group2.model.User;


import javax.lang.model.util.SimpleElementVisitor6;
import java.awt.font.NumericShaper;
import java.net.URL;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AddTargetController implements Initializable, UserData {

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private ComboBox typeComboBox;

    @FXML
    private Label typeErrorLabel;

    @FXML
    private TextField valueTextField;

    @FXML
    private Label valueErrorLabel;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private Label dateErrorLabel;

    @FXML
    private Label confirmationLabel;

    @FXML
    private Button addTargetButton;

    @FXML
    private Button closeButton;

    private User currentUser;
    private DataManager dataManager = DataManager.getDataManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.addAll("Average Speed (m/s)", "Target Weight (kg)", "Total Distance (m)");
        typeComboBox.setItems(typeOptions);

        nameErrorLabel.setText("");
        typeErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");
        currentUser = dataManager.getCurrentUser();
    }

    public void updateUser() {

    }

    /**
     * Performs field validity checks and submits a new target to the user's targets and the database
     */
    public void addTarget() {

        nameErrorLabel.setText("");
        typeErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");

        Double value = 0.0;
        Date date = null;
        boolean validTarget = true;
        Double currVal = 0.0;

        String name = nameTextField.getText();
        if (name.length() == 0) {
            nameErrorLabel.setText("Target must have a name.");
            validTarget = false;
        } else if (name.length() > 30) {
            nameErrorLabel.setText("Name cannot exceed 30 characters.");
            validTarget = false;
        }

        String type = "";
        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
            validTarget = false;
            typeErrorLabel.setText("Please select a target type");
        } else {
            type = typeComboBox.getValue().toString();
        }

        try {
            value = Double.valueOf(valueTextField.getText());

            switch(type){
                case "Target Weight (kg)":
                    if (value <= 0 || value > 600) {
                        valueErrorLabel.setText("Weight targets must be in range 1 - 600");
                        validTarget = false;
                    }
                    currVal = currentUser.weightProperty().get();
                    break;

                case "Average Speed (m/s)":
                    if (value < 0) {
                        valueErrorLabel.setText("Speed target cannot be negative!");
                        validTarget = false;
                    }
                    currVal = currentUser.avgSpeedProperty().get();
                    break;

                case "Total Distance (m)":
                    if (value < 0) {
                        valueErrorLabel.setText("Total distance cannot be negative!");
                        validTarget = false;
                    }
                    currVal = currentUser.totalDistanceProperty().get();
                    break;
            }

        } catch (NumberFormatException ex) {
            valueErrorLabel.setText("Target value must be a number!");
        }


        if (dateDatePicker.getValue() == null) {
            dateErrorLabel.setText("Target must have a completion date.");
            validTarget = false;
        } else {
            // TODO Fix bug: After entering valid target, select add target, then try submit a new target with date before today's date
            date = Date.from(dateDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -1);
            Date today = cal.getTime();
            if (date.before(today)) {
                dateErrorLabel.setText("Completion date cannot be before today.");
                validTarget = false;
            }
        }

        if (validTarget) {
            Target userTarget = new Target(name, date, type, currVal, currVal, value);
            dataManager.addTarget(userTarget, false);
            confirmationLabel.setText("Target added successfully.");
            clearFields();
        }
    }

    private void clearFields() {
        nameTextField.setText(null);
        valueTextField.setText(null);
        dateDatePicker.setValue(null);
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
