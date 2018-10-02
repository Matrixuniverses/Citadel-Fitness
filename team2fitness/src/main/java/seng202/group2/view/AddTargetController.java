package seng202.group2.view;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;

import java.awt.*;
import java.net.URL;
import java.time.ZoneId;
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

    private DataManager dataManager = DataManager.getDataManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("Travel Distance");
        typeOptions.add("Weight Loss");
        typeComboBox.setItems(typeOptions);
        typeComboBox.setValue("Travel Distance");

        nameErrorLabel.setText("");
        typeErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");
    }

    public void updateUser() {

    }

    private void addTarget() {

        nameErrorLabel.setText("");
        typeErrorLabel.setText("");
        valueErrorLabel.setText("");
        dateErrorLabel.setText("");
        confirmationLabel.setText("");

        Double value = 0.0;
        Date date = null;
        boolean validTarget = true;

        String name = nameTextField.getText();
        if (name.length() == 0) {
            nameErrorLabel.setText("Target must have a name.");
            validTarget = false;
        } else if (name.length() > 15) {
            nameErrorLabel.setText("Target name cannot exceed 15 characters.");
            validTarget = false;
        }

        String type = typeComboBox.getValue().toString();

        try {
            value = Double.valueOf(valueTextField.getText());
            if (value < 0) {
                valueErrorLabel.setText("Value cannot be negative.");
                validTarget = false;
            }
            // TODO Implement error checking based off type of target.
        } catch (NumberFormatException e) {
            valueErrorLabel.setText("Value must be a number.");
            validTarget = false;
        }

        if (dateDatePicker.getValue() == null) {
            dateErrorLabel.setText("Target must have a completion date.");
            validTarget = false;
        } else {
            date = Date.from(dateDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            // TODO Ensure date selected is at least current day?
        }

        if (validTarget) {
            Target userTarget = new Target(name, type, value, date);
            /*
            Don't want to push broken code, in comment lines below.
            DataManager.getDataManager().addTarget(userTarget);
            dataManager.addTarget(userTarget);
             */
            confirmationLabel.setText("Target added successfully.");
        }
    }
}
