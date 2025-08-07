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
                        "('Inception', 'Sci-Fi', 'Leonardo DiCaprio, Joseph Gordon-Levitt, Ken Watanabe, Tom Hardy, Elliot Page, Cillian Murphy', '148 min', 8.7, " +
                        "'A skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: ''inception'', the implantation of another person''s idea into a target''s subconscious.', '4.Inception.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Matrix', 'Sci-Fi', 'Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving', '136 min', 9.0, " +
                        "'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', '5.The Matrix.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Lord of the Rings: The Return of the King', 'Fantasy', 'Elijah Wood, Ian McKellen, Liv Tyler, Viggo Mortensen, Sean Astin, Cate Blanchett, Orlando Bloom, Andy Serkis', '201 min', 8.9, " +
                        "'The hobbit Frodo and his friend Sam continue their journey to destroy the One Ring, while the fellowship and their allies join forces against Sauron.', '6.The Lord of the Rings- The Return of the King.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Interstellar', 'Sci-Fi', 'Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine', '169 min', 8.7, " +
                        "'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.', '7.Interstellar.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Lion King', 'Animation', 'Jonathan Taylor Thomas, Matthew Broderick, James Earl Jones, Jeremy Irons, Moira Kelly, Nathan Lane, Ernie Sabella', '88 min', 8.5, " +
                        "'A young lion prince flees his kingdom after the murder of his father and must reclaim his rightful place on the throne.', '9.The Lion King.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Grave of the Fireflies', 'Animation', 'Tsutomu Tatsumi, Ayano Shiraishi, Yoshiko Shinohara, Akemi Yamaguchi', '88 min', 8.5, " +
                        "'A young boy and his little sister struggle to survive during the final months of World War II in Japan.', '10.Grave of the Fireflies.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Forrest Gump', 'Drama', 'Tom Hanks, Robin Wright, Gary Sinise', '142 min', 8.8, " +
                        "'The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.', '11.Forrest Gump.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Titanic', 'Romance', 'Leonardo DiCaprio, Kate Winslet', '195 min', 7.8, " +
                        "'A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.', '12.Titanic.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Fist of Fury', 'Action', 'Bruce Lee, Nora Miao', '106 min', 7.6, " +
                        "'Following the murder of his teacher, a young martial artist seeks vengeance on a rival Japanese dojo.', '13.Fist of Fury.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Apocalypse Now', 'War', 'Martin Sheen, Marlon Brando', '147 min', 8.4, " +
                        "'A U.S. Army officer is sent into Cambodia with a hazardous mission to assassinate a renegade Colonel who has set himself up as a god among a local tribe.', '14.Apocalypse Now.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('WALL·E', 'Animation', 'Ben Burtt, Elissa Knight', '98 min', 8.4, " +
                        "'In the distant future, a small waste-collecting robot inadvertently embarks on a space journey that will ultimately decide the fate of mankind.', '15.WALL·E.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Life of Pi', 'Adventure', 'Suraj Sharma, Irrfan Khan', '127 min', 7.9, " +
                        "'A young man who survives a disaster at sea is hurtled into an epic journey of adventure and discovery. While cast away, he forms an unexpected connection with another survivor: a fearsome Bengal tiger.', '16.Life of Pi.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('12th Fail', 'Biography', 'Vikrant Massey, Medha Shankar', '147 min', 9.2, " +
                        "'Based on the true story of IPS officer Manoj Kumar Sharma, 12th Fail is an honest, heartfelt and inspiring tale about never giving up.', '17.12th Fail.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Toy Story', 'Animation', 'Tom Hanks, Tim Allen', '81 min', 8.3, " +
                        "'A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy''s room.', '18.Toy Story .jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('The Conjuring: Last Rites', 'Horror', 'Vera Farmiga, Patrick Wilson', '120 min', 8.0, " +
                        "'The Warrens investigate a series of deaths linked to a demonic presence.', '19.The Conjuring- Last Rites.jpeg')",
                "INSERT INTO movies (title, genre, cast_members, duration, rating, summary, poster_url) VALUES " +
                        "('Coco', 'Animation', 'Anthony Gonzalez, Gael García Bernal', '105 min', 8.4, " +
                        "'Aspiring musician Miguel, confronted with his family''s ancestral ban on music, enters the Land of the Dead to find his great-great-grandfather, a legendary singer.', '20.Coco.jpeg')"
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