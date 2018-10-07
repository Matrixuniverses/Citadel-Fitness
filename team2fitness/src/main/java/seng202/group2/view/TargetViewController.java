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

public class TargetViewController implements Initializable, UserData {

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

    @FXML
    private TableColumn targetStatusCol;

    // Buttons
    @FXML
    private Button addTargetButton;

    @FXML
    public Button modifyTargetButton;

    @FXML
    public Button deleteTargetButton;

    private User currentUser;
    private DataManager dataManager = DataManager.getDataManager();

    public void initialize(URL location, ResourceBundle resources) {
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        targetNameCol.setCellValueFactory(new PropertyValueFactory<Target, String>("name"));
        targetTypeCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedType"));
        targetDateEndCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedCompletionDate"));
        targetStatusCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedStatus"));
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

        setupListeners();
    }

    private void setupListeners() {
        for (Target target : currentUser.getTargetList()) {
            System.out.println(target.getName());
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
                            System.out.println(newValue);
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

    public void updateUser() {}
}