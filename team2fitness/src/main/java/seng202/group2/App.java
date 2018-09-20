package seng202.group2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import seng202.group2.view.AppController;

import static javafx.application.Application.launch;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static Stage mainStage;

    public void start(Stage mainStage) throws Exception{
        this.mainStage = mainStage;
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("/fxml/FXMLApp.fxml"));
        Parent root = fxml.load();
        AppController controller = fxml.getController();
//        mainStage.initStyle(StageStyle.UTILITY);



        mainStage.setTitle("Citadel Fitness");
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    public static void main( String[] args )
    {
        launch(args);
    }
}
