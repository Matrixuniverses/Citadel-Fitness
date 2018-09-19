package seng202.group2.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.data_functions.GraphGenerator;
import seng202.group2.model.Activity;
import seng202.group2.model.DataManager;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewGraphController implements UserData, Initializable{

    private DataManager dataManager;

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
        graphTypeChoice.setItems(FXCollections.observableArrayList("Distance / Time", "Heart Rate / Time",
                new Separator(), "Calories Burned", "Stress / Time"));

        //creating the chart
        lineChart.setTitle("Distance over Time");

        activityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));

        activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateGraph();
            }
        });
    }

    /**
     * Called on button click when a new activity is selected and displays the selected activity
     * in a graphical form.
     */
    public void updateGraph() {
        Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        lineChart.getData().add(GraphGenerator.createTimeSeries(selectedActivity));
    }


    @Override
    public void setDataManager(DataManager newDataManager) {
        dataManager = newDataManager;
    }

    @Override
    public void updateUser() {
        activityTable.setItems(dataManager.getCurrentUser().getActivityList());
    }
}
