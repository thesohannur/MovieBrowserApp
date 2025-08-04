package com.lab.moviebrowserapplication.models;

public class Movie {
    private final int id;
    private final String title;
    private final String genre;
    private final String castMembers;
    private final String duration;
    private final double rating;
    private final String summary;
    private final String posterUrl;

    public Movie(int id, String title, String genre, String castMembers,
                 String duration, double rating, String summary, String posterUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.castMembers = castMembers;
        this.duration = duration;
        this.rating = rating;
        this.summary = summary;
        this.posterUrl = posterUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getCastMembers() { return castMembers; }
    public String getDuration() { return duration; }
    public double getRating() { return rating; }
    public String getSummary() { return summary; }
    public String getPosterUrl() { return posterUrl; }

    @Override
    public String toString() {
        return title;
    }
}