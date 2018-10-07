package seng202.group2.view;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import seng202.group2.data.DataManager;
import seng202.group2.data.DataParser;
import seng202.group2.data.MalformedLine;
import seng202.group2.model.Activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ActivitiesFoundController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();
    private ArrayList<Activity> activities;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn statusColumn;

    @FXML
    private TableColumn importColumn;



    public void initialize(URL location, ResourceBundle resources) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<Activity, Date>("formattedDate"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<Activity, Image>("statusImage"));
        statusColumn.setCellFactory(new Callback<TableColumn<Activity, Image>, TableCell<Activity, Image>>() {
            @Override
            public TableCell<Activity, Image> call(TableColumn<Activity, Image> param) {
                final ImageView imageView = new ImageView();


                TableCell<Activity, Image> cell = new TableCell<Activity, Image>() {
                    public void updateStatus(Activity activity) {
                        imageView.setImage(activity.getStatusImage());
                    }
                };

                cell.setGraphic(imageView);
                return cell;
            }
        });


        importColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new CheckBoxTableCell();
            }
        });

    }

    public void update(DataParser parser) {
        for (Activity activity : parser.getActivitiesRead()) {
            activity.setStatusImage(new Image(getClass().getResource("images/greenCheck.png").toExternalForm()));
        }

        for (MalformedLine malformed : parser.getMalformedLines()) {
            malformed.getActivity().setStatusImage(new Image(getClass().getResource("images/redWarning.png").toExternalForm()));
        }

    }
}
