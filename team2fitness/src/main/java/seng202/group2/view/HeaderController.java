package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This edits the title bar at the top of the window
 */
public class HeaderController implements Initializable, UserData {

    private DataManager dataManager = DataManager.getDataManager();

    @FXML
    private Label nameLabel;

    @FXML
    private Label viewLabel;

    @FXML
    private Button notificationButton;

    @FXML
    private ImageView notificationImage;


    @FXML
    private ImageView titleLogo;

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getViewLabel() {
        return viewLabel;
    }

    public void initialize(URL location, ResourceBundle resources) {
        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                // Update header name
                nameLabel.textProperty().bind(dataManager.getCurrentUser().nameProperty());

                //Rest health warning icon
            }
        });

        dataManager.newHealthWarningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    notificationImage.setImage(new Image("/images/notification1.png"));
                } else {
                    notificationImage.setImage(new Image("/images/notification0.png"));
                }
            }
        });

    }

    @Override
    public void updateUser() {

    }

    public Button getNotificationButton() {
        return notificationButton;
    }


}
