package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        controller = new Controller();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        primaryStage.setTitle("Space Battles");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.getScene().getStylesheets().add(getClass().getResource("gameCSS.css").toExternalForm());
        primaryStage.show();

    }


    @Override
    public void stop(){
        controller.clearApplication();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
