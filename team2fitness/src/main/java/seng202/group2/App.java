package seng202.group2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import seng202.group2.view.AppController;



/**
 * This class is the main class for Citadel Fitness and launches the application
 * Start up sound Sound effect sourced from <a href="https://www.zapsplat.com"> https://www.zapsplat.com </a>
 * @author Adam Conway, Christopher Worrall, Luke Walsh, Ollie Sharplin, Sam Shankland, Vikas Shenoy
 */
public class App extends Application
{
    public static Stage mainStage;
    private static MediaPlayer startUpSoundPlayer;

    /**
     * This Creates the mainStage of the app, builds the app window and loads fxml and css resources
     * @param mainStage
     * @throws Exception
     */
    @Override
    public void start(Stage mainStage) throws Exception{
        this.mainStage = mainStage;

        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("/fxml/FXMLApp.fxml"));
        Parent root = fxml.load();
        root.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        AppController controller = fxml.getController();
        //mainStage.initStyle(StageStyle.UTILITY);


        mainStage.setTitle("Citadel Fitness");
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        mainStage.setScene(new Scene(root));

//        Media startUpMedia = new Media(getClass().getResource("/Sounds/StartUpSound.mp3").toExternalForm());
//        startUpSoundPlayer = new MediaPlayer(startUpMedia);
//        startUpSoundPlayer.play();
        mainStage.show();
    }

    /**
     * Main Function, Launches App
     * @param args
     */
    public static void main( String[] args )
    {
        launch(args);
    }
}
