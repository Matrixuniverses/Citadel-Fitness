package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for view data point scene. Handles scene and events.
 */


public class ViewDataPointsController implements Initializable
{

    @FXML
    private TableColumn dateCol;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<DataPoint> dataTable;

    @FXML
    private TableColumn latCol;

    @FXML
    private TableColumn altCol;

    @FXML
    private TableColumn heartCol;

    @FXML
    private TableColumn longCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataTable.setPlaceholder(new Label("No Data Point Data"));

        dateCol.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("formattedDate"));
        latCol.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("latitude"));
        longCol.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("longitude"));
        altCol.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("altitude"));
        heartCol.setCellValueFactory(new PropertyValueFactory<DataPoint, String>("heartRate"));
    }

    public void updateActivity(Activity activity) {
        dataTable.setItems(activity.getActivityData());
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
