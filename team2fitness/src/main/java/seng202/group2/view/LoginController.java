package seng202.group2.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import seng202.group2.model.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable, UserData {

    private DataManager dataManager;
    private StringProperty status = new SimpleStringProperty("logged out");

    @FXML
    Button newUserButton;

    @FXML
    Button user1Button;

    @FXML

    Button user2Button;

    @FXML
    Button user3Button;

    @FXML
    Button user4Button;

    @FXML
    Button user5Button;

    @FXML
    Button user6Button;

    private ObservableList<Button> buttonList = FXCollections.observableArrayList();


    public Button getNewUserButton() {
        return newUserButton;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonList.addAll(user1Button, user2Button, user3Button, user4Button, user5Button, user6Button);

    }

    /**
     * Sets the DataManager to the instance passed from the main controller.
     * Adds listeners to the login buttons and the new user buttons.
     * Disables/Enables the buttons when users exist
     * @param newDataManager
     */
    public void updateUserData(DataManager newDataManager) {
        this.dataManager = newDataManager;

        dataManager.getUserList().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                // Sets actions for select user buttons.
                for (int i = 0; i < buttonList.size(); i++) {
                    int number = i;
                    buttonList.get(number).setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            dataManager.setCurrentUser(number);
                            status.setValue("logged in");
                        }
                    });
                    buttonList.get(i).setVisible(false);
                }

                for (int i = 0 ; i < dataManager.getUserList().size() ; i++) {
                    buttonList.get(i).setVisible(true);
                    buttonList.get(i).textProperty().bind(dataManager.getUserList().get(i).nameProperty());
                }
            }
        });
    }

    public StringProperty statusProperty() {
        return status;
    }
}
