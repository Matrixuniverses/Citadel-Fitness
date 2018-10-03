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
    TableView<Target> targetTable;

    // Table Columns
    @FXML
    TableColumn targetNameCol;

    @FXML
    TableColumn targetTypeCol;

    @FXML
    TableColumn targetDateStartCol;

    @FXML
    TableColumn targetInitValueCol;

    @FXML
    TableColumn targetCurrValueCol;

    @FXML
    TableColumn targetGoalValueCol;

    @FXML
    TableColumn targetDateEndCol;

    @FXML
    TableColumn targetStatusCol;

    // Buttons
    @FXML
    Button addTargetButton;

    @FXML
    Button modifyTargetButton;

    @FXML
    Button deleteTargetButton;

    User currentUser;
    private DataManager dataManager = DataManager.getDataManager();

    public void initialize(URL location, ResourceBundle resources) {
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        targetTable.setPlaceholder(new Label("No targets set."));

        targetNameCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetName"));
        targetTypeCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetType"));
        targetInitValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetInitialValue"));
        targetCurrValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetCurrentValue"));
        targetGoalValueCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetGoalValue"));
        targetDateEndCol.setCellValueFactory(new PropertyValueFactory<Target, String>("formattedDateEnd"));
        targetStatusCol.setCellValueFactory(new PropertyValueFactory<Target, String>("targetStatus"));

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