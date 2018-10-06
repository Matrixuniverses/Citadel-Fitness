package seng202.group2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;
import seng202.group2.model.User;


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
        typeOptions.add("Average Speed (m/s)");
        typeOptions.add("Target Weight (kg)");
        typeOptions.add("Total Distance (m)");
        typeComboBox.setItems(typeOptions);

        nameErrorLabel.setText("");
        typeErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");
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

        String type = "";
        Double value = 0.0;
        Date date = null;
        boolean validTarget = true;

        String name = nameTextField.getText();
        if (name.length() == 0) {
            nameErrorLabel.setText("Target must have a name.");
            validTarget = false;
        } else if (name.length() > 15) {
            nameErrorLabel.setText("Name cannot exceed 15 characters.");
            validTarget = false;
        }

        try {
            type = typeComboBox.getValue().toString();
        } catch (NullPointerException e) {
            typeErrorLabel.setText("Target must have a type.");
            validTarget = false;
        }

        try {
            value = Double.valueOf(valueTextField.getText());
            if (type == "Target Weight (kg)" && (value < 0 || value > 600)) {
                valueErrorLabel.setText("Weight must be between 0 and 600 kg.");
                validTarget = false;
            } else if (type == "Average Speed (m/s)" && (value <= 0 || value >= 10)) {
                valueErrorLabel.setText("Speed must be between 0.1 and 10 m/s.");
                validTarget = false;
            } else if (type == "Total Distance (m)" && (value <= 0)) {
                valueErrorLabel.setText("Distance must be greater than 0 m.");
                validTarget = false;
            }
        } catch (NumberFormatException e) {
            valueErrorLabel.setText("Value must be a number.");
            validTarget = false;
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
            Target userTarget = new Target(name, type, value, date);
            dataManager.addTarget(userTarget);
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
