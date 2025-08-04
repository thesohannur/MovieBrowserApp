package com.lab.moviebrowserapplication;

import com.lab.moviebrowserapplication.models.DatabaseManager;
import com.lab.moviebrowserapplication.utils.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // DatabaseManager is initialized automatically by its static block

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/lab/moviebrowserapplication/views/main-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        ThemeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);

        primaryStage.setTitle("Movie Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}