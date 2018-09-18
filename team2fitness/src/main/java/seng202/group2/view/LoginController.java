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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.model.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable, UserData {

    private DataManager dataManager;
    private StringProperty status = new SimpleStringProperty("logged out");
    private ObservableList<Button> buttonList = FXCollections.observableArrayList();

    @FXML
    private Button loginButton;

    @FXML
    private TableView userTable;

    @FXML
    private TableColumn userTableCol;

    @FXML
    private Button createButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private Label errorLabel;


    public void login(){


    }

    public void create() {
        String name = nameField.getText();
        Integer age = Integer.valueOf(ageField.getText());
        Double height = Double.valueOf(heightField.getText());
        Float weight = Float.valueOf(weightField.getText());
        User user = new User(name, age, height, weight);
        dataManager.getUserList().add(user);

    }


    /**
     * Sets the DataManager to the instance passed from the main controller.
     * Adds listeners to the login buttons and the new user buttons.
     * Disables/Enables the buttons when users exist
     * @param newDataManager
     */
    public void updateUserData(DataManager newDataManager) {
        this.dataManager = newDataManager;
        userTable.setItems(dataManager.getUserList());


        dataManager.getUserList().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(Change<? extends User> c) {

            }
        });
    }


    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTableCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    }
}
