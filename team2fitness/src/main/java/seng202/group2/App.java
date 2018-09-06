package seng202.group2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.group2.view.Controller;

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
        fxml.setLocation(getClass().getResource("/fxml/FXMLMain.fxml"));
        Parent root = fxml.load();
        Controller controller = fxml.getController();
        mainStage.setTitle("Citadel Fitness");
        mainStage.setResizable(false);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    public static void main( String[] args )
    {
        launch(args);
    }
}
