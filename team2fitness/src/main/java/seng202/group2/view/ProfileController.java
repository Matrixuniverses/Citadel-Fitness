package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng202.group2.data.DataManager;
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
    Chart activityGraph;

    @FXML
    Label bmiLabel;

    @FXML
    Label weightLabel;

    @FXML
    Label totalDistanceLabel;

    @FXML
    Label targetsLabel;


    @FXML
    Button editProfileButton;

    public Button getEditProfileButton() {
        return editProfileButton;
    }


    public void initialize(URL location, ResourceBundle resources) {
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                bmiLabel.textProperty().bind(Bindings.format("%.0f", dataManager.getCurrentUser().bmiProperty()));
                weightLabel.textProperty().bind(Bindings.format("%.0f",dataManager.getCurrentUser().weightProperty()));
                totalDistanceLabel.textProperty().bind(Bindings.format("%.1f",dataManager.getCurrentUser().totalDistanceProperty().divide(1000)));
            }
        });

    }

        @Override
    public void updateUser() {

    }
}
