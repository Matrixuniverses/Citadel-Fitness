package seng202.group2.view;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import seng202.group2.data.DataManager;
import seng202.group2.model.User;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Initialises the login Scene and handles events for the Login Scene
 */
public class LoginController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();

    private StringProperty status = new SimpleStringProperty("logged out");


    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn userTableCol;

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

    private boolean[] fields = new boolean[4];

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
        errorLabel.setText("");
        nameField.setText("");
        nameField.getStyleClass().setAll("text-input", "text-field");
        ageField.setText("");
        ageField.getStyleClass().setAll("text-input", "text-field");
        heightField.setText("");
        heightField.getStyleClass().setAll("text-input", "text-field");
        weightField.setText("");
        weightField.getStyleClass().setAll("text-input", "text-field");
    }

    /**
     * Creates a user with the data in the fields. Checks fields and throws error messages if incorrect type or range.
     * Attempts to create a new user. If all text fields contain valid entry data, the user will be added.
     * Otherwise an appropriate error message will be displayed telling the user what to modify.
     */
    public void create() {
        errorLabel.setVisible(true);
        String name = nameField.getText();

        if (!fields[0]) {
            errorLabel.setText("Name length must be in range 1 - 25");
            fieldUpdate(nameField, true, 0);
        } else if (!fields[1]) {
            errorLabel.setText("Age must be in range 0 - 140");
            fieldUpdate(ageField, true, 1);
        } else if (!fields[2]) {
            errorLabel.setText("Invalid height");
            fieldUpdate(heightField, true, 2);
        } else if (!fields[3]) {
            errorLabel.setText("Invalid weight");
            fieldUpdate(weightField, true, 3);
        } else if (genderComboBox.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText("Please select a gender!");
        } else {
            // Don't need to worry about exceptions because control flow prevents any bad numbers from getting to
            // this point
            int age = Integer.valueOf(ageField.getText());
            double height = Double.valueOf(heightField.getText());
            double weight = Double.valueOf(weightField.getText());

            dataManager.addUser(name, age, height, weight, genderComboBox.getSelectionModel().getSelectedItem());
            errorLabel.setTextFill(Color.BLACK);
            errorLabel.setText("User '" + name + "' successfully created.");
            clearFields();
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
     * @param field Field to set the style on
     * @param invalidField If the field is invalid or not
     * @param index Index in the validation array of correct fields
     */
    private void fieldUpdate(TextField field, Boolean invalidField, int index) {
        if (invalidField) {
            field.getStyleClass().clear();
            field.getStyleClass().add("invalidField");
            fields[index] = false;
        } else {
            field.getStyleClass().clear();
            field.getStyleClass().add("validField");
            fields[index] = true;
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

        // Calls field update to change the appearance of the name field to green if valid, red if not.
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int length = nameField.getText().length();
                if (length == 0 || length > 25) {
                    fieldUpdate(nameField, true, 0);
                } else {
                    fieldUpdate(nameField, false, 0);
                }
            }
        });

        // Calls field update to change the appearance of the age field to green if valid, red if not.
        ageField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (Integer.valueOf(ageField.getText()) < 0 || Integer.valueOf(ageField.getText()) > 140) {
                        fieldUpdate(ageField, true,1 );
                    } else {
                        fieldUpdate(ageField, false, 1);
                    }
                } catch (NumberFormatException e) {
                    fieldUpdate(ageField, true, 1);
                }
            }
        });

        // Calls field update to change the appearance of the height field to green if valid, red if not.
        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    double height = Double.valueOf(heightField.getText());
                    if (height < 50 || height > 270) {
                        fieldUpdate(heightField, true, 2);
                    } else {
                        fieldUpdate(heightField, false, 2);
                    }

                } catch (NumberFormatException ex) {
                    fieldUpdate(heightField, true, 2);
                }
            }
        });

        // Calls field update to change the appearance of the weight field to green if valid, red if not.
        weightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    double weight = Double.valueOf(weightField.getText());
                    if (weight <= 0 || weight >= 600) {
                        fieldUpdate(weightField, true, 3);
                    } else {
                        fieldUpdate(weightField, false, 3);
                    }
                } catch (NumberFormatException ex) {
                    fieldUpdate(weightField, true, 3);
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
