package seng202.group2.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.DataManager;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for warning panel. Handles scene and events.
 */

public class WarningPanelController implements Initializable {

    DataManager dataManager = DataManager.getDataManager();
    private BooleanProperty hasTachycardia = dataManager.hasTachycardiaProperty();
    private BooleanProperty hasCardiovascular = dataManager.hasCardiovascularProperty();
    private BooleanProperty hasBradycardia = dataManager.hasBradycardiaProperty();

    @FXML
    private VBox vboxPanel;

    @FXML
    private BorderPane noWarningsPanel;


    @FXML
    private AnchorPane cardiovascularPanel;

    @FXML
    private Button cardiovascularSearch;

    @FXML
    private AnchorPane tachycardiaPanel;

    @FXML
    private Button tachycardiaSearch;

    @FXML
    private Button bradycardiaSearch;

    @FXML
    private Button tachycardiaButton;

    @FXML
    private Button bradycardiaButton;

    @FXML
    private AnchorPane bradycardiaPanel;

    @FXML
    private Button resolveAllButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        hasBradycardia.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                bradycardiaPanel.setManaged(newValue);
                bradycardiaPanel.setVisible(newValue);
                checkWarnings();
            }
        });

        hasTachycardia.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                tachycardiaPanel.setManaged(newValue);
                tachycardiaPanel.setVisible(newValue);
                checkWarnings();
            }
        });

        hasCardiovascular.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                cardiovascularPanel.setManaged(newValue);
                cardiovascularPanel.setVisible(newValue);
                checkWarnings();

            }
        });

        hasTachycardia.setValue(false);
        hasCardiovascular.setValue(false);
        hasBradycardia.setValue(false);

    }

    /**
     * Sets the panel to display no warnings if the user has no diseases.
     */
    private void checkWarnings(){
        if (!(hasBradycardia.get() || hasTachycardia.get() || hasCardiovascular.get())) {
            noWarningsPanel.setVisible(true);
            noWarningsPanel.setManaged(true);
            resolveAllButton.setDisable(true);
        } else {
            noWarningsPanel.setVisible(false);
            noWarningsPanel.setManaged(false);
            resolveAllButton.setDisable(false);
        }
    }

    @FXML
    public void resolveAll(){
        hasCardiovascular.setValue(false);
        hasTachycardia.setValue(false);
        hasBradycardia.setValue(false);
    }

    @FXML
    public void resolveBradycardia() {
        hasBradycardia.setValue(false);
    }

    @FXML
    public void resolveTachycardia(){
        hasTachycardia.setValue(false);
    }

    @FXML
    public void resolveCardiovascular() {
        hasCardiovascular.setValue(false);
    }



    @FXML
    public void bradycardiaSearch(){
        DataAnalyzer.webSearch_Google("Bradycardia");
    }

    @FXML
    public void tachycardiaSearch(){
        DataAnalyzer.webSearch_Google("Tachycardia");
    }

    @FXML
    public void cardiovascularSearch(){
        DataAnalyzer.webSearch_Google("Cardiovascular Mortality");
    }


}
