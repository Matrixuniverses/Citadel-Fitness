package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.model.DataManager;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewGraphController implements UserData, Initializable{

    private DataManager dataManager;

    @FXML
    private LineChart lineChart;

    @FXML
    private Button showButton;

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
        activityTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activityNameCol.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
    }

    /**
     * Called on button click when a new activity is selected and displays the selected activity
     * in a graphical form.
     */
    public void updateGraph() {
        Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        lineChart.getData().add(selectedActivity.createTimeSeries());
    }


    @Override
    public void setDataManager(DataManager newDataManager) {
        dataManager = newDataManager;
    }

    @Override
    public void updateUser() {

    }
}
