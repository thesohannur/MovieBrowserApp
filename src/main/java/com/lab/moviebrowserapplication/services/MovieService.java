package com.lab.moviebrowserapplication.services;

import com.lab.moviebrowserapplication.models.Movie;
import com.lab.moviebrowserapplication.models.DatabaseManager;
import java.util.List;

public class MovieService {
    public List<Movie> getAllMovies() {
        return DatabaseManager.getAllMovies();
    }

    public List<Movie> searchMovies(String query, String genre) {
        return DatabaseManager.getAllMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                .filter(m -> genre.equals("All Genres") || m.getGenre().equals(genre))
                .toList();
    }
}