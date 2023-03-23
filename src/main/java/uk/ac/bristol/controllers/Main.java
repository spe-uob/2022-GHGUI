package uk.ac.bristol.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        primaryStage.setTitle("FXML Example");
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();

        controller.setPrimaryStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}