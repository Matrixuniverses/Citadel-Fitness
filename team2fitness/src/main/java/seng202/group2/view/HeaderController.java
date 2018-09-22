package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This edits the title bar at the top of the window
 */
public class HeaderController implements Initializable {


    @FXML
    private Label nameLabel;

    @FXML
    private Label viewLabel;

    @FXML
    private ImageView titleLogo;

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getViewLabel() {
        return viewLabel;
    }

    public void initialize(URL location, ResourceBundle resources) {


    }


}
