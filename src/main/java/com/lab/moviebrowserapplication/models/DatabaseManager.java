package com.lab.moviebrowserapplication.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static String DB_URL_NO_DB;
    private static String DB_URL_WITH_DB;
    private static String USER;
    private static String PASS;

    static {
        try {
            // Load configuration
            Properties props = new Properties();
            try (InputStream input = DatabaseManager.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new RuntimeException("config.properties not found!");
                }
                props.load(input);
                DB_URL_NO_DB = props.getProperty("db.url.no_db");
                DB_URL_WITH_DB = props.getProperty("db.url.with_db");
                USER = props.getProperty("db.user");
                PASS = props.getProperty("db.password");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL_NO_DB, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // Create database if not exists
            stmt.execute("CREATE DATABASE IF NOT EXISTS moviebrowser");

            try (Connection connWithDb = DriverManager.getConnection(DB_URL_WITH_DB, USER, PASS);
                 Statement stmtWithDb = connWithDb.createStatement()) {

                // Create movies table
                stmtWithDb.execute("CREATE TABLE IF NOT EXISTS movies (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "title VARCHAR(255) NOT NULL," +
                        "genre VARCHAR(255)," +
                        "cast_members TEXT," +
                        "duration VARCHAR(50)," +
                        "rating DOUBLE," +
                        "summary TEXT," +
                        "poster_url VARCHAR(255)" +
                        ")");

                // Clear existing data before inserting new movies
                stmtWithDb.execute("TRUNCATE TABLE movies");

                insertSampleMovies(connWithDb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleMovies(Connection conn) throws SQLException {
        String[] inserts = {
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Shawshank Redemption', 'Drama', 'Tim Robbins, Morgan Freeman', '142 min', 9.3, " +
                        "'Two imprisoned men bond over a number of years...', '1.The Shawshank Redemption.jpg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Godfather', 'Crime', 'Marlon Brando, Al Pacino', '175 min', 9.2, " +
                        "'The aging patriarch of an organized crime dynasty...', '2.The Godfather.jpg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Dark Knight', 'Action', 'Christian Bale, Heath Ledger', '152 min', 9.0, " +
                        "'When the menace known as the Joker emerges...', '3.The Dark Knight.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Inception', 'Action, Science Fiction, Adventure', 'Leonardo DiCaprio, Joseph Gordon-Levitt, Ken Watanabe, Tom Hardy, Elliot Page, Cillian Murphy', '148 min', 8.7, " +
                        "'A skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: ''inception'', the implantation of another person''s idea into a target''s subconscious.', '4.Inception.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Matrix', 'Action, Science Fiction', 'Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving', '136 min', 9.0, " +
                        "'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', '5.The Matrix.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Lord of the Rings: The Return of the King', 'Epic High Fantasy, Adventure', 'Elijah Wood, Ian McKellen, Liv Tyler, Viggo Mortensen, Sean Astin, Cate Blanchett, Orlando Bloom, Andy Serkis', '201 min', 8.9, " +
                        "'The hobbit Frodo and his friend Sam continue their journey to destroy the One Ring, while the fellowship and their allies join forces against Sauron.', '6.The Lord of the Rings- The Return of the King.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Interstellar', 'Adventure, Drama, Sci-Fi', 'Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine', '169 min', 8.7, " +
                        "'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.', '7.Interstellar.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Lion King', 'Animated, Musical, Drama', 'Jonathan Taylor Thomas, Matthew Broderick, James Earl Jones, Jeremy Irons, Moira Kelly, Nathan Lane, Ernie Sabella', '88 min', 8.5, " +
                        "'A young lion prince flees his kingdom after the murder of his father and must reclaim his rightful place on the throne.', '9.The Lion King.jpeg')",

                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Grave of the Fireflies', 'Animated, War, Drama', 'Tsutomu Tatsumi, Ayano Shiraishi, Yoshiko Shinohara, Akemi Yamaguchi', '88 min', 8.5, " +
                        "'A young boy and his little sister struggle to survive during the final months of World War II in Japan.', '10.Grave of the Fireflies.jpeg')"
        };

        for (String sql : inserts) {
            conn.createStatement().execute(sql);
        }
    }

    public static List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL_WITH_DB, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies")) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("cast_members"),
                        rs.getString("duration"),
                        rs.getDouble("rating"),
                        rs.getString("summary"),
                        rs.getString("poster_url")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}