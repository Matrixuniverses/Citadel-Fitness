package seng202.group2.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.data.DataManager;
import seng202.group2.model.Target;
import seng202.group2.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class TargetViewController implements Initializable, UserData {

    @FXML
    private TableView<Target> targetTable;

    // Table Columns
    @FXML
    private TableColumn targetNameCol;

    @FXML
    private TableColumn targetTypeCol;

    @FXML
    private TableColumn targetInitValueCol;

    @FXML
    private TableColumn targetCurrValueCol;

    @FXML
    private TableColumn targetGoalValueCol;

    @FXML
    private TableColumn targetDateEndCol;

    @FXML
    private TableColumn targetStatusCol;

    // Buttons
    @FXML
    private Button addTargetButton;

    @FXML
    Button modifyTargetButton;

    @FXML
    Button deleteTargetButton;

    private User currentUser;
    private DataManager dataManager = DataManager.getDataManager();

    public void initialize(URL location, ResourceBundle resources) {
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        targetNameCol.setCellValueFactory(new PropertyValueFactory<Target, String>("name"));
        targetTypeCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedType"));
        targetInitValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedInitialValue"));
        targetCurrValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedCurrentValue"));
        targetGoalValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedFinalValue"));
        targetDateEndCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedCompletionDate"));
        targetStatusCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedStatus"));

        dataManager.currentUserProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                targetTable.setItems(DataManager.getDataManager().getTargetList());
            }
        });
    }

    public void updateUser() {

    }

    public Button getAddTargetButton() {
        return addTargetButton;
    }
}