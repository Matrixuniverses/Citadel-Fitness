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
import seng202.group2.data.DataManager;
import seng202.group2.model.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This Initialises the login Sceneand handles events for the Login Scene
 */
public class LoginController implements Initializable {

    private DataManager dataManager;
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
            dataManager.setCurrentUser(userTable.getSelectionModel().getSelectedItem());
            status.setValue("logged in");
        } else {
            userTable.setPlaceholder(new Label("Please create a user before logging in"));
        }

    }

    public void create() {
        try {
            String name = nameField.getText();
            Integer age = Integer.valueOf(ageField.getText());
            Double height = Double.valueOf(heightField.getText());
            Float weight = Float.valueOf(weightField.getText());
            String gender = genderChoiceBox.getValue();

            dataManager.addUser(name, age, height, weight, gender);
            errorLabel.setText("User '" + name + "' successfully created.");
            errorLabel.setVisible(true);
        } catch (Exception e){
            errorLabel.setText("Invalid profile data");
            errorLabel.setVisible(true);
        }
    }


    /**
     * Sets the DataManager to the instance passed from the main controller.
     * Adds listeners to the login buttons and the new user buttons.
     * Disables/Enables the buttons when users exist
     * @param newDataManager
     */
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
        userTable.setItems(dataManager.getUserList());
        userTable.getSelectionModel().selectFirst();
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
