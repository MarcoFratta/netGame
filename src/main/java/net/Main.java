package net;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/startMenu.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        /*GameServer gameServer = new GameServer();
        gameServer.start();
        /*GameClient client = new GameClient(InetAddress.getLocalHost(),6200);
        client.start();

         */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
