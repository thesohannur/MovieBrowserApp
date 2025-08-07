package com.lab.moviebrowserapplication.controllers;

import com.lab.moviebrowserapplication.models.Movie;
import com.lab.moviebrowserapplication.models.DatabaseManager;
import com.lab.moviebrowserapplication.services.MovieService;
import com.lab.moviebrowserapplication.utils.ThemeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML private ListView<Movie> moviesListView;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> genreCombo;
    @FXML private MenuBar menuBar;

    private ObservableList<Movie> watchLaterList = FXCollections.observableArrayList();
    private List<Movie> allMovies;
    private final MovieService movieService = new MovieService();

    @FXML
    public void initialize() {
        allMovies = movieService.getAllMovies();
        setupGenreFilter();
        setupListView();
        setupThemeMenu();
        setupSearchField();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMovies());
        genreCombo.valueProperty().addListener((obs, oldVal, newVal) -> filterMovies());
    }

    private void setupGenreFilter() {
        genreCombo.getItems().add("All Genres");
        genreCombo.getItems().addAll(
                allMovies.stream()
                        .map(Movie::getGenre)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
        genreCombo.setValue("All Genres");
    }

    private void setupListView() {
        moviesListView.setItems(FXCollections.observableArrayList(allMovies));
        moviesListView.setCellFactory(param -> new ListCell<>() {
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

    private void setupThemeMenu() {
        Menu themeMenu = new Menu("Theme");
        MenuItem lightTheme = new MenuItem("Light");
        MenuItem darkTheme = new MenuItem("Dark");

        lightTheme.setOnAction(e -> ThemeManager.applyTheme(menuBar.getScene(), ThemeManager.Theme.LIGHT));
        darkTheme.setOnAction(e -> ThemeManager.applyTheme(menuBar.getScene(), ThemeManager.Theme.DARK));

        themeMenu.getItems().addAll(lightTheme, darkTheme);
        menuBar.getMenus().add(themeMenu);
    }

    private void filterMovies() {
        String searchTerm = searchField.getText().toLowerCase();
        String selectedGenre = genreCombo.getValue();
        List<Movie> filtered = allMovies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(searchTerm))
                .filter(m -> selectedGenre.equals("All Genres") || m.getGenre().equals(selectedGenre))
                .collect(Collectors.toList());

        moviesListView.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void openMovieDetails(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Movie selected = moviesListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/lab/moviebrowserapplication/views/movie-details.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                MovieDetailsController controller = loader.getController();
                controller.setMovie(selected, watchLaterList.contains(selected), watchLaterList);

                stage.setTitle(selected.getTitle());
                stage.show();
            }
        }
    }

    @FXML
    private void openWatchLater() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/lab/moviebrowserapplication/views/watch-later.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load(), 500, 500));

        WatchLaterController controller = loader.getController();
        controller.setWatchLaterList(watchLaterList);

        stage.setTitle("Watch Later List");
        stage.show();
    }
}