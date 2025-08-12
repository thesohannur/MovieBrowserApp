package com.lab.moviebrowserapplication.controllers;

import com.lab.moviebrowserapplication.models.DatabaseManager;
import com.lab.moviebrowserapplication.models.Movie;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WatchLaterController {
    @FXML private ListView<Movie> watchLaterListView;

    public void setWatchLaterList(ObservableList<Movie> watchLaterList) {
        watchLaterListView.setItems(watchLaterList);
        watchLaterListView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            private final Label titleLabel = new Label();
            private final Button removeButton = new Button("Remove");
            private final HBox hBox = new HBox(10, imageView, titleLabel, removeButton);

            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    titleLabel.setText(movie.getTitle());
                    try {
                        Image img = new Image(getClass().getResourceAsStream(
                                "/com/lab/moviebrowserapplication/images/" + movie.getPosterUrl()));
                        imageView.setImage(img);
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(120);
                        imageView.setPreserveRatio(true);
                    } catch (Exception e) {
                        imageView.setImage(null);
                    }

                    // Style the remove button
                    removeButton.setStyle("-fx-font-size: 12px; -fx-padding: 5 10;");
                    removeButton.setOnAction(event -> {
                        DatabaseManager.removeFromWatchLater(movie.getId());
                        watchLaterList.remove(movie);
                    });

                    setGraphic(hBox);
                }
            }
        });

        // Add double-click handler to open movie details
        watchLaterListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Movie selected = watchLaterListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        openMovieDetails(selected, watchLaterList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Refresh ListView when watchLaterList changes
        watchLaterList.addListener((ListChangeListener<Movie>) change -> {
            watchLaterListView.refresh();
        });
    }

    private void openMovieDetails(Movie movie, ObservableList<Movie> watchLaterList) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/lab/moviebrowserapplication/views/movie-details.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        MovieDetailsController controller = loader.getController();
        controller.setMovie(movie, DatabaseManager.isInWatchLater(movie.getId()), watchLaterList);

        stage.setTitle(movie.getTitle());
        stage.show();
    }
}