package seng202.group2.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HeaderController {


    @FXML
    private Label nameLabel;

    @FXML
    private Label viewLabel;

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getViewLabel() {
        return viewLabel;
    }
}
