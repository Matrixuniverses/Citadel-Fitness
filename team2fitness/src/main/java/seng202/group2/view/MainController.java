package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import seng202.group2.model.DataManager;

import java.util.Stack;

public class MainController implements UserData {

    private DataManager dataManager;

    @FXML
    AnchorPane navBar;

    @FXML
    StackPane mainStack;


    @Override
    public void updateUserData(DataManager newDataManager) {
        dataManager = newDataManager;
    }
}
