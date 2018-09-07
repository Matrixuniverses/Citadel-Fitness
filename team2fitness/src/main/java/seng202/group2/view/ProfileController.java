package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng202.group2.model.User;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable, UserData {

    private StringProperty currentView = new SimpleStringProperty();

    public String getCurrentView() {
        return currentView.get();
    }

    public StringProperty currentViewProperty() {
        return currentView;
    }

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
    Label nameLabel;

    @FXML
    Label targetsLabel;


    @FXML
    Button editProfileButton;

    public Button getEditProfileButton() {
        return editProfileButton;
    }


    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateUserData(User user) {
        bmiLabel.textProperty().bind(Bindings.convert(user.bmiProperty()));
        nameLabel.textProperty().bind(user.nameProperty());
        weightLabel.textProperty().bind(Bindings.convert(user.weightProperty()));
        totalDistanceLabel.textProperty().bind(Bindings.convert(user.totalDistanceProperty()));
    }

    public void showEditProfile() {
        setCurrentView("editProfileScene");
    }

}
