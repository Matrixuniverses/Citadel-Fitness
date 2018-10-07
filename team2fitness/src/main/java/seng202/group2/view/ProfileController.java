package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controls the buttons and events for the ProfileView Scene
 */
public class ProfileController implements Initializable, UserData {

    private StringProperty currentView = new SimpleStringProperty();

    public String getCurrentView() {
        return currentView.get();
    }

    public StringProperty currentViewProperty() {
        return currentView;
    }

    private DataManager dataManager =  DataManager.getDataManager();

    public void setCurrentView(String currentView) {
        this.currentView.set(currentView);
    }

    @FXML
    private Chart activityGraph;

    @FXML
    private Label bmiLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label totalDistanceLabel;

    @FXML
    private Label targetsLabel;

    // Table
    @FXML
    private TableView<Target> targetTable;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn progressColumn;

    @FXML
    private TableColumn statusColumn;

    public void initialize(URL location, ResourceBundle resources) {
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                bmiLabel.textProperty().bind(Bindings.format("%.0f", dataManager.getCurrentUser().bmiProperty()));
                weightLabel.textProperty().bind(Bindings.format("%.0f",dataManager.getCurrentUser().weightProperty()));
                totalDistanceLabel.textProperty().bind(Bindings.format("%.1f",dataManager.getCurrentUser().totalDistanceProperty().divide(1000)));

            }
        });

        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        nameColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedType"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<Target, Double>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<Target> forTableColumn());
        statusColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedStatus"));

        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                targetTable.setItems(DataManager.getDataManager().getTargetList());
            }
        });

        setupListeners();
    }

    private void setupListeners() {
        for (Target target : dataManager.getCurrentUser().getTargetList()) {
            System.out.println(target.getName());
            switch(target.getType()) {
                case "Total Distance (m)":
                    dataManager.getCurrentUser().totalDistanceProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                        }
                    });
                case "Target Weight (kg)":
                    dataManager.getCurrentUser().weightProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                            System.out.println(newValue);
                        }
                    });
                case "Average Speed (m/s)":
                    dataManager.getCurrentUser().avgSpeedProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            target.updateProgress((double) newValue);
                        }
                    });
            }
        }
    }

        @Override
    public void updateUser() {

    }
}
