package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import seng202.group2.data.DataManager;
import seng202.group2.model.Activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActivitiesFoundController implements Initializable, UserData {

    private DataManager dataManager = DataManager.getDataManager();
    private ArrayList<Activity> activitiesToReview;
    private ArrayList<Activity> activitiesToAdd;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn nameColumn;


    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateUser() {

    }
}
