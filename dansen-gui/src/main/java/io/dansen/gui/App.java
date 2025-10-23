package io.dansen.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        var root = new MainView();
        var scene = new Scene(root, 800, 480);
        stage.setTitle("Dansen – Mixer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
