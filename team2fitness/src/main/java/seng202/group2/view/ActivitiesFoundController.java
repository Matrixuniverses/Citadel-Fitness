package seng202.group2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Date;
import java.util.ResourceBundle;

public class ActivitiesFoundController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();
    private ObservableList<Activity> activityList = FXCollections.observableArrayList();

    @FXML
    private TableView activityTable;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn statusColumn;

    @FXML
    private TableColumn importColumn;

    @FXML
    private Button cancelButton;

    @FXML
    private Button importButton;



    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Activity, String>("activityType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Activity, Image>("statusImage"));
        statusColumn.setCellFactory(new Callback<TableColumn<Activity, Image>, TableCell<Activity, Image>>() {
            @Override
            public TableCell<Activity, Image> call(TableColumn<Activity, Image> param) {
                final ImageView imageView = new ImageView();
                imageView.setFitHeight(24);
                imageView.setFitWidth(24);

                TableCell<Activity, Image> cell = new TableCell<Activity, Image>() {
                    @Override
                    public void updateItem(Image item, boolean empty) {
                        imageView.setImage(item);
                    }
                };

                cell.setGraphic(imageView);
                return cell;
            }
        });

        importColumn.setCellValueFactory(new PropertyValueFactory<Activity, Boolean>("checked"));
        importColumn.setCellFactory(CheckBoxTableCell.forTableColumn(importColumn));
        importColumn.setEditable(true);
        activityTable.setEditable(true);
    }

    public void update(DataParser parser) {
        activityList.removeAll();
        activityList.addAll(parser.getActivitiesRead());
        for (Activity activity : activityList) {
            activity.setStatusImage(new Image(getClass().getResource("/images/greenCheck.png").toExternalForm()));
            activity.setChecked(true);
        }

        for (MalformedLine malformed : parser.getMalformedLines()) {
            malformed.getActivity().setStatusImage(new Image(getClass().getResource("/images/redWarning.png").toExternalForm()));
            malformed.getActivity().setChecked(false);
        }

        activityTable.setItems(activityList);
    }

    public ObservableList<Activity> getActivityList() {
        return activityList;
    }


    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getImportButton() {
        return importButton;
    }
}
