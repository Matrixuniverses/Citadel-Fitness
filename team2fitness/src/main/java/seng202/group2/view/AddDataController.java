package seng202.group2.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddDataController {

    private File selectedFile;
    private IntegerProperty newFile = new SimpleIntegerProperty(0);

    @FXML
    private Button selectFileButton;

    public File getSelectedFile() {
        return selectedFile;
    }

    public int getNewFile() {
        return newFile.get();
    }

    public IntegerProperty newFileProperty() {
        return newFile;
    }

    public void setNewFile(int newFile) {
        this.newFile.set(newFile);
    }

    public void selectFileAction(ActionEvent event){
        FileChooser fc = new FileChooser();

        selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            newFile.setValue(1);
        } else {
            System.out.println("File is not valid");
        }
    }
}
