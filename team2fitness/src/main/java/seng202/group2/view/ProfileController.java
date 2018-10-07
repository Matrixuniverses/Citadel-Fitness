package seng202.group2.view;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.analysis.GraphGenerator;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.net.URL;
import java.util.*;

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
    private Label bmiLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label totalDistanceLabel;

    @FXML
    private Label activityCountLabel;

    @FXML
    private Label maxSpeedLabel;

    // Table
    @FXML
    private TableView<Target> targetTable;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn valueColumn;

    @FXML
    private TableColumn progressColumn;

    @FXML
    private TableColumn statusColumn;

    // Bar Chart
    @FXML
    private BarChart<String, Double> activityGraph;

    public void initialize(URL location, ResourceBundle resources) {
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                bmiLabel.textProperty().bind(Bindings.format("%.0f", dataManager.getCurrentUser().bmiProperty()));
                weightLabel.textProperty().bind(Bindings.format("%.1f", dataManager.getCurrentUser().weightProperty()));
                heightLabel.textProperty().bind(Bindings.format("%.0f", dataManager.getCurrentUser().heightProperty()));
                totalDistanceLabel.textProperty().bind(Bindings.format("%.1f", dataManager.getCurrentUser().totalDistanceProperty().divide(1000)));
                activityCountLabel.textProperty().setValue(Integer.toString(dataManager.getCurrentUser().getActivityList().size()));
                setMaxSpeedLabel();
                setActivityGraph();

                dataManager.getCurrentUser().getActivityList().addListener(new ListChangeListener<Activity>() {
                    @Override
                    public void onChanged(Change<? extends Activity> c) {
                        activityCountLabel.textProperty().setValue(Integer.toString(dataManager.getCurrentUser().getActivityList().size()));
                        setMaxSpeedLabel();
                        setActivityGraph();
                    }
                });
            }
        });

        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        nameColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedType"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedFinalValue"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<Target, Double>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<Target> forTableColumn());
        statusColumn.setCellValueFactory(new PropertyValueFactory<Target, Double>("status"));

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

    private void setMaxSpeedLabel() {
        double maxSpeed = 0.0;
        for (Activity activity : dataManager.getCurrentUser().getActivityList()) {
            double speed = activity.getTotalDistance() / activity.getTotalTime();
            maxSpeed = Math.max(maxSpeed, speed);
        }
        maxSpeedLabel.textProperty().setValue(Double.toString(Math.round(maxSpeed * 10.0) / 10.0));
    }

    private void setActivityGraph() {
        int remaining = dataManager.getCurrentUser().getActivityList().size();
        ArrayList<Activity> recentActivities = new ArrayList<>();
        for (Activity currentActivity : dataManager.getCurrentUser().getActivityList()) {
            if (remaining <= 10) {
                recentActivities.add(currentActivity);
            }
            remaining -= 1;
        }
        System.out.println("Bar Graph activities size: " + Integer.toString(recentActivities.size()));
        activityGraph.getData().removeAll(activityGraph.getData());
        XYChart.Series series = GraphGenerator.createRecentActivitySeries(recentActivities);
        activityGraph.getData().add(series);
        activityGraph.getYAxis().setLabel("Total Distance (m)");
    }

        @Override
    public void updateUser() {

    }
}
