package seng202.group2.view;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This Initialises the login Sceneand handles events for the Login Scene
 */
public class LoginController implements Initializable {


    private StringProperty status = new SimpleStringProperty("logged out");
    private ObservableList<Button> buttonList = FXCollections.observableArrayList();

    @FXML
    private Button loginButton;

    @FXML
    private TableView<User> userTable;

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

    @FXML
    private ImageView imageViewLogo;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    private int rotate;

    private double size;

    boolean moveBack = false;


    public void login() {
        if (userTable.getSelectionModel().getSelectedItem() != null) {
            DataManager.getDataManager().setCurrentUser(userTable.getSelectionModel().getSelectedItem());
            status.setValue("logged in");
        } else {
            userTable.setPlaceholder(new Label("Please create a user before logging in"));
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        heightField.setText("");
        weightField.setText("");
        genderChoiceBox.setValue("");
    }

    public void create() {
        try {
            errorLabel.setTextFill(Color.RED);

            String name = nameField.getText();
            Integer age = Integer.valueOf(ageField.getText());
            Double height = Double.valueOf(heightField.getText());
            Float weight = Float.valueOf(weightField.getText());
            String gender = genderChoiceBox.getValue();

            if (name.length() == 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Please enter a Name.");
            } else if (name.length() > 25) {
                errorLabel.setVisible(true);
                errorLabel.setText("Name cannot exceed 25 characters.");
            } else if (gender != "Male" && gender != "Female") {
                errorLabel.setVisible(true);
                errorLabel.setText("Please select a Gender.");
            } else if (age < 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Age value cannot be negative.");
            } else if (height <= 0 || weight <= 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Height/Weight values must be positive numbers.");
            } else {
                DataManager.getDataManager().addUser(name, age, height, weight, gender);
                errorLabel.setVisible(true);
                errorLabel.setTextFill(Color.BLACK);
                errorLabel.setText("User '" + name + "' successfully created.");
                clearFields();
            }
        } catch (Exception e){
            errorLabel.setVisible(true);
            errorLabel.setText("Age/Height/Weight values must be numbers.");
        }
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        ArrayList<String> genders = new ArrayList<String>();
        genders.add("Male");
        genders.add("Female");
        ObservableList<String> gendersList = FXCollections.observableArrayList(genders);
        genderChoiceBox.setItems(gendersList);

        userTable.setPlaceholder(new Label("No users created yet."));
        userTableCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));


        userTable.setItems( DataManager.getDataManager().getUserList());
        userTable.getSelectionModel().selectFirst();


        Image logo = new Image("/images/citadelLogo.png");
        imageViewLogo.setImage(logo);

        rotate = 0;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                rotate++;
                imageViewLogo.setRotate(rotate);
            }
        };
        timer.start();

    }
}
