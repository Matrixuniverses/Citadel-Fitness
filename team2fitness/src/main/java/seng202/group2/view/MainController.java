package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import seng202.group2.model.DataManager;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;

public class MainController implements UserData, Initializable {

    private DataManager dataManager;

    @FXML
    AnchorPane navBar;

    @FXML
    StackPane mainStack;


    // Controllers
    private ActivityViewController activityViewController;


    // Views

    private AnchorPane activityView;


    // Allows nav bar to work easily
    private HashMap<String, Pane> paneMap = new HashMap<String, Pane>();



    @Override
    public void setDataManager(DataManager newDataManager) {
        this.dataManager = newDataManager;
        activityViewController.setDataManager(dataManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
    }

    private void initializeViews(){
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/fxml/FXMLActivityView.fxml"));
            activityView = loader.load();
            activityViewController = loader.getController();
            paneMap.put("data", activityView);
            mainStack.getChildren().addAll(activityView);

        } catch (IOException ex_) {
            ex_.printStackTrace();
        }

    }

    public void updateViews(){

        activityViewController.updateUser();

    }
}
