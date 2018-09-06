package seng202.group2.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import seng202.group2.model.User;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable, UserData {

    @FXML
    Chart activityGraph;

    @FXML
    Label bmiLabel;

    @FXML
    Label weightLabel;

//    @FXML
//    Text distanceText;

    @FXML
    Label nameLabel;

    @FXML
    Label targetsLabel;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateUserData(User user) {
        bmiLabel.textProperty().bind(Bindings.convert(user.bmiProperty()));
        nameLabel.textProperty().bind(user.nameProperty());
        weightLabel.textProperty().bind(Bindings.convert(user.weightProperty()));
    }
}
