package com.lab.moviebrowserapplication.controllers;

import com.lab.moviebrowserapplication.models.DatabaseManager;
import com.lab.moviebrowserapplication.models.Movie;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieDetailsController {
    @FXML private Label titleLabel;
    @FXML private Label genreLabel;
    @FXML private Label castLabel;
    @FXML private Label durationLabel;
    @FXML private Label ratingLabel;
    @FXML private Label summaryLabel;
    @FXML private ImageView posterImageView;
    @FXML private Button watchLaterButton;

    private Movie movie;
    private boolean isInWatchLater;
    private ObservableList<Movie> watchLaterList;

    public void setMovie(Movie movie, boolean isInWatchLater, ObservableList<Movie> watchLaterList) {
        this.movie = movie;
        this.isInWatchLater = isInWatchLater;
        this.watchLaterList = watchLaterList;

        titleLabel.setText(movie.getTitle());
        genreLabel.setText("Genre: " + movie.getGenre());
        castLabel.setText("Cast: " + movie.getCastMembers());
        durationLabel.setText("Duration: " + movie.getDuration());
        ratingLabel.setText("Rating: " + String.valueOf(movie.getRating()));
        summaryLabel.setText(movie.getSummary());

        try {
            Image image = new Image(getClass().getResourceAsStream(
                    "/com/lab/moviebrowserapplication/images/" + movie.getPosterUrl()));
            posterImageView.setImage(image);
        } catch (Exception e) {
            posterImageView.setImage(null);
        }

        updateButton();
    }

    @FXML
    private void toggleWatchLater() {
        if (isInWatchLater) {
            DatabaseManager.removeFromWatchLater(movie.getId());
            watchLaterList.remove(movie);
        } else {
            DatabaseManager.addToWatchLater(movie.getId());
            watchLaterList.add(movie);
        }
        isInWatchLater = !isInWatchLater;
        updateButton();
    }

    private void updateButton() {
        watchLaterButton.setText(isInWatchLater ? "Remove from Watch Later" : "Add to Watch Later");
    }
}