package com.enigmamachine.enigmawithgui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnigmaApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TypeWriter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1320, 840);
        scene.getStylesheets().add(getClass().getResource("typewriter.css").toExternalForm());
        stage.setTitle("Enigma Machine");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
