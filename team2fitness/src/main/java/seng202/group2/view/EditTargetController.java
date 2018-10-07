package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.net.URL;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class EditTargetController implements Initializable, UserData {

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label valueErrorLabel;

    @FXML
    private Label dateErrorLabel;

    @FXML
    private Label confirmationLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private Button updateButton;

    @FXML
    public Button closeButton;

    private DataManager dataManager = DataManager.getDataManager();
    private Target currentTarget;

    public void initialize(URL location, ResourceBundle resources) {
        dataManager.currentUserProperty().addListener((ObservableValue<? extends User> observable,
                                                       User oldValue, User newValue) -> {
            nameErrorLabel.setText("");
            valueErrorLabel.setText("");
            dateErrorLabel.setText("");
            confirmationLabel.setText("");
        });
    }

    public void updateTargetFields(Target target) {
        String type = target.getType();
        if(type.equals("Total Distance (m)") || type.equals("Average Speed (m/s)")) {
            valueTextField.setText(Double.toString(target.getFinalValue() - target.getInitialValue()));
        } else {
            valueTextField.setText(Double.toString(target.getFinalValue()));
        }
        nameTextField.setText(target.getName());
        dateDatePicker.setValue(target.getCompletionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        currentTarget = target;
    }

    public void updateTarget() {

        nameErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");

        String type = currentTarget.getType();
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
            value = Double.valueOf(valueTextField.getText());
            if (type.equals("Target Weight (kg)") && (value < 0 || value > 600)) {
                valueErrorLabel.setText("Weight must be between 0 and 600 kg.");
                validTarget = false;
            } else if (type.equals("Average Speed (m/s)") && (value <= 0 || value >= 10)) {
                valueErrorLabel.setText("Speed must be between 0.1 and 10 m/s.");
                validTarget = false;
            } else if (type.equals("Total Distance (m)") && (value <= 0)) {
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
            dataManager.deleteTarget(currentTarget);
            currentTarget.setName(name);
            currentTarget.setFinalValue(value);
            currentTarget.setCompletionDate(date);
            dataManager.addTarget(currentTarget);
            confirmationLabel.setText("Target updated successfully.");
        }
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void updateUser() {}
}
