package seng202.group2.view;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import jdk.nashorn.internal.runtime.ParserException;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This Initialises the login Scene and handles events for the Login Scene
 */
public class LoginController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();

    private StringProperty status = new SimpleStringProperty("logged out");


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
    private ComboBox<String> genderComboBox;

    private boolean shouldSpin = true;
    private int rotation = 0;
    private AnimationTimer timer;


    /**
     * Sets the current user in the data manager, and logs the user in to the main application.
     */
    public void login() {
        if (userTable.getSelectionModel().getSelectedItem() != null) {
            dataManager.setCurrentUser(userTable.getSelectionModel().getSelectedItem());
            status.setValue("logged in");
        } else {
            userTable.setPlaceholder(new Label("Please create a user before logging in"));
        }
    }

    /**
     * Clears the fields to empty.
     * Empties the text fields when a valid user is created.
     */
    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        heightField.setText("");
        weightField.setText("");
    }

    /**
     * Creates a user with the data in the fields. Checks fields and throws error messages if incorrect type or range.
     * Attempts to create a new user. If all text fields contain valid entry data, the user will be added.
     * Otherwise an appropriate error message will be displayed telling the user what to modify.
     */
    public void create() {
        try {
            errorLabel.setVisible(true);

            String name = nameField.getText();
            int age = Integer.valueOf(ageField.getText());
            double height = Double.valueOf(heightField.getText());
            double weight = Double.valueOf(weightField.getText());
            String gender = genderComboBox.getSelectionModel().getSelectedItem();

            if (name.length() == 0) {
                errorLabel.setText("Please enter a Name.");
            } else if (name.length() > 25) {
                errorLabel.setText("Name cannot exceed 25 characters.");
            } else if (age < 0) {
                errorLabel.setText("Age value cannot be negative.");
            } else if (height <= 0 || weight <= 0) {
                errorLabel.setText("Height/Weight values must be positive numbers.");
            } else {
                dataManager.addUser(name, age, height, weight, gender);
                errorLabel.setTextFill(Color.BLACK);
                errorLabel.setText("User '" + name + "' successfully created.");
                clearFields();
            }
        } catch (Exception e) {
            errorLabel.setText("Unable to create user!");
        }
    }


    /**
     * Function used to make the logo spin
     */
    public void spin() {
        if (shouldSpin) {
            timer.start();
            shouldSpin = false;
        } else {
            timer.stop();
            shouldSpin = true;
        }
    }


    public StringProperty statusProperty() {
        return status;
    }

    /**
     * Helper function to set the visual input validity of the passed field
     *
     * @param field         Field to set the style on
     * @param disableButton If the create button should be disabled or not
     */
    private void fieldUpdate(TextField field, Boolean disableButton) {
        if (disableButton) {
            field.getStyleClass().clear();
            field.getStyleClass().add("invalidField");
            createButton.disableProperty().setValue(true);
        } else {
            field.getStyleClass().clear();
            field.getStyleClass().add("validField");
            createButton.disableProperty().setValue(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<String> gendersList = FXCollections.observableArrayList("Male", "Female");
        genderComboBox.setItems(gendersList);

        userTable.setPlaceholder(new Label("No users created yet."));
        userTableCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        imageViewLogo.setImage(new Image("/images/citadelLogo.png"));

        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int length = nameField.getText().length();
                if (length == 0 || length > 25) {
                    fieldUpdate(nameField, true);
                } else {
                    fieldUpdate(nameField, false);
                }
            }
        });

        ageField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (Integer.valueOf(ageField.getText()) <= 0) {
                        fieldUpdate(ageField, true);
                    } else {
                        fieldUpdate(ageField, false);
                    }
                } catch (NumberFormatException e) {
                    fieldUpdate(ageField, true);
                }
            }
        });

        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    double height = Double.valueOf(heightField.getText());
                    if (height <= 0 || height >= 300) {
                        fieldUpdate(heightField, true);
                    } else {
                        fieldUpdate(heightField, false);
                    }

                } catch (NumberFormatException ex) {
                    fieldUpdate(heightField, true);
                }
            }
        });

        weightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    double weight = Double.valueOf(weightField.getText());
                    if (weight <= 0 || weight >= 650) {
                        fieldUpdate(weightField, true);
                    } else {
                        fieldUpdate(weightField, false);
                    }
                } catch (NumberFormatException ex) {
                    fieldUpdate(weightField, true);
                }
            }
        });


        // Spinny boi
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                rotation += 2;
                imageViewLogo.setRotate(rotation);
            }
        };

        userTable.setItems(dataManager.getUserList());
        userTable.getSelectionModel().selectFirst();
    }
}
