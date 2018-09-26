package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.analysis.GraphGenerator;
import seng202.group2.model.Activity;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the ViewGraph Scene and handles events within this scene
 */
public class ViewGraphController implements UserData, Initializable{

    private DataManager dataManager = DataManager.getDataManager();
    private ObservableList<XYChart.Series> seriesList = FXCollections.observableArrayList();

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private ChoiceBox graphTypeChoice;

    @FXML
    private TableView<Activity> activityTable;

    @FXML
    private TableColumn activityNameCol;

    /**
     *  Initializes the activity tables to show a list of activities.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphTypeChoice.setItems(FXCollections.observableArrayList("Distance / Time", "Heart Rate / Time", "Speed Graph",  "Calories Burnt"));
        graphTypeChoice.getSelectionModel().select(0);
        //creating the chart
        lineChart.setTitle("Distance/Time");
        lineChart.getXAxis().setLabel("Time(s)");
        lineChart.getYAxis().setLabel("Distance(m)");

        activityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));

        activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGraph();
            }
        });

        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                activityTable.setItems(dataManager.getCurrentUser().getActivityList());
            }
        });
    }

    /**
     * Called on button click when a new activity is selected and displays the selected activity
     * in a graphical form.
     */
    public void updateGraph() {
        lineChart.getData().removeAll(lineChart.getData());
        ObservableList<Activity> activityList = activityTable.getSelectionModel().getSelectedItems();

        if (activityList.size() > 1) {
            lineChart.setCreateSymbols(false);
        } else {
            lineChart.setCreateSymbols(true);
        }
        ObservableList<XYChart.Series> seriesList = FXCollections.observableArrayList();
        for (Activity activity : activityList) {
            if (graphTypeChoice.getSelectionModel().getSelectedIndex() == 0){
                lineChart.setTitle("Distance/Time");
                lineChart.getXAxis().setLabel("Time(s)");
                lineChart.getYAxis().setLabel("Distance(m)");

                XYChart.Series series = GraphGenerator.createTimeSeries(activity);
                seriesList.add(series);
                lineChart.getData().add(series);
            } else if (graphTypeChoice.getSelectionModel().getSelectedIndex() == 1) {
                lineChart.setTitle("Heart Rate/Time");
                lineChart.getXAxis().setLabel("Time(s)");
                lineChart.getYAxis().setLabel("Hart Rate");

                XYChart.Series series = GraphGenerator.createHeartRateTimeSeries(activity);
                seriesList.add(series);
                lineChart.getData().add(series);
            } else if (graphTypeChoice.getSelectionModel().getSelectedIndex() == 2) {
                lineChart.setTitle("Speed/Time");
                lineChart.getXAxis().setLabel("Time(s)");
                lineChart.getYAxis().setLabel("Speed(m/s)");
                XYChart.Series series = GraphGenerator.createSpeedTimeSeries(activity);
                seriesList.add(series);
                lineChart.getData().add(series);
            }else if (graphTypeChoice.getSelectionModel().getSelectedIndex() == 3) {
                lineChart.setTitle("Calories Burnt");
                lineChart.getXAxis().setLabel("Time(s)");
                lineChart.getYAxis().setLabel("Calories");
                XYChart.Series series = GraphGenerator.createCaloriesTimeSeries(activity);
                seriesList.add(series);
                lineChart.getData().add(series);
            }


        }

    }


    @Override
    public void updateUser() {

    }
}
