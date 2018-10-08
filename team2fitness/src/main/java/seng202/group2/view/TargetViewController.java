package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for target view scene. Handles scene and events.
 */

public class TargetViewController implements Initializable {

    @FXML
    private TableView<Target> targetTable;

    // Table Columns
    @FXML
    private TableColumn targetNameCol;

    @FXML
    private TableColumn targetTypeCol;

    @FXML
    private TableColumn targetProgressColumn;

    @FXML
    private TableColumn targetDateEndCol;

    // Buttons
    @FXML
    private Button addTargetButton;

    @FXML
    private Button modifyTargetButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label currentValueLabel;

    @FXML
    private Label targetValueLabel;

    private User currentUser;
    private DataManager dataManager = DataManager.getDataManager();

    public void initialize(URL location, ResourceBundle resources) {
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        targetNameCol.setCellValueFactory(new PropertyValueFactory<Target, String>("name"));
        targetTypeCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedType"));
        targetDateEndCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedCompletionDate"));
        targetProgressColumn.setCellValueFactory(new PropertyValueFactory<Target, Double>("progress"));
        targetProgressColumn.setCellFactory(ProgressBarTableCell.<Target> forTableColumn());

        currentUser = dataManager.getCurrentUser();
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                currentUser = newValue;
                targetTable.setItems(DataManager.getDataManager().getTargetList());
            }
        });

        // Adds an event listener to the selected item to change the target information boxes in the activity view
        targetTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Target>() {
            @Override
            public void changed(ObservableValue<? extends Target> observable, Target oldValue, Target newValue) {
                if (newValue != null) {
                    statusLabel.setText(newValue.getStatus());
                    String type = newValue.getType();
                    if (type.equals("Total Distance (m)")) {
                        double current = newValue.getCurrentValue() - newValue.getInitialValue();
                        double target = newValue.getFinalValue() - newValue.getInitialValue();
                        current = Math.max(current, 0);
                        currentValueLabel.setText(Integer.toString((int)Math.round(current)) + " m");
                        targetValueLabel.setText(Integer.toString((int)Math.round(target)) + " m");
                    } else if (type.equals("Average Speed (m/s)")) {
                        currentValueLabel.setText(Double.toString(Math.round(newValue.getCurrentValue() * 10.0) / 10.0) + " m/s");
                        targetValueLabel.setText(Double.toString(Math.round(newValue.getFinalValue() * 10.0) / 10.0) + " m/s");
                    } else {
                        currentValueLabel.setText(Double.toString(Math.round(newValue.getCurrentValue() * 10.0) / 10.0) + " kg");
                        targetValueLabel.setText(Double.toString(Math.round(newValue.getFinalValue() * 10.0) / 10.0) + " kg");
                    }
                }

            }
        });

        setupListeners();
    }

    /**
     * Helper function that will add change listeners to user fields that will update a targets current value when the
     * user fields are changed
     */
    private void setupListeners() {
        for (Target target : currentUser.getTargetList()) {
            switch(target.getType()) {
                case "Total Distance (m)":
                    currentUser.totalDistanceProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                        }
                    });
                case "Target Weight (kg)":
                    currentUser.weightProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                        }
                    });
                case "Average Speed (m/s)":
                    currentUser.avgSpeedProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                        }
                    });
            }
        }
    }

    /**
     * Deletes the selected target from the database and the target list for the current user
     */
    @FXML
    private void deleteTarget() {
        Target target = targetTable.getSelectionModel().getSelectedItem();
        if (target != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm delete");
            alert.setContentText("Do you want to delete the selected target?");

            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                dataManager.deleteTarget(target);
            }
        }
    }

    public TableView<Target> getTargetTable() {
        return targetTable;
    }

    public Button getAddTargetButton() {
        return addTargetButton;
    }

    public Button getModifyTargetButton() {
        return modifyTargetButton;
    }

}