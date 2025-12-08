package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/** The entry point for the JavaFx application, sets up the stage, scene and controllers.*/

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 550, 540);
        scene.setFill(Color.web("#2a2a2a"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        new GameController(c);
    }


    public static void main(String[] args) {
        launch(args);
    }
}