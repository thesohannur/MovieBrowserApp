package com.lab.moviebrowserapplication.controllers;

import com.lab.moviebrowserapplication.models.Movie;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WatchLaterController {
    @FXML private ListView<Movie> watchLaterListView;

    public void setWatchLaterList(ObservableList<Movie> watchLaterList) {
        watchLaterListView.setItems(watchLaterList);
        watchLaterListView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(movie.getTitle());
                    try {
                        Image img = new Image(getClass().getResourceAsStream(
                                "/com/lab/moviebrowserapplication/images/" + movie.getPosterUrl()));
                        imageView.setImage(img);
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(120);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
    }
}