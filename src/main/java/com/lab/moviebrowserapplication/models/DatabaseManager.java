import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:movies.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            // Create the movies table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS movies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "director TEXT NOT NULL," +
                    "year INTEGER NOT NULL," +
                    "poster_url TEXT NOT NULL" +
                    ");";
            stmt.execute(createTableSQL);
            System.out.println("Database and table initialized.");

            // Delete all existing data from the table
            String deleteDataSQL = "DELETE FROM movies;";
            stmt.execute(deleteDataSQL);
            System.out.println("Existing data deleted from the movies table.");

            // Insert sample movies
            insertSampleMovies();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleMovies() {
        String insertSQL = "INSERT INTO movies (title, director, year, poster_url) VALUES (?, ?, ?, ?);";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            List<Movie> moviesToInsert = new ArrayList<>();
            moviesToInsert.add(new Movie("The Shawshank Redemption", "Frank Darabont", 1994, "1.The Shawshank Redemption.jpg"));
            moviesToInsert.add(new Movie("The Godfather", "Francis Ford Coppola", 1972, "2.The Godfather.jpg"));
            moviesToInsert.add(new Movie("The Dark Knight", "Christopher Nolan", 2008, "3.The Dark Knight.jpeg"));
            moviesToInsert.add(new Movie("Inception", "Christopher Nolan", 2010, "4.Inception.jpeg"));
            moviesToInsert.add(new Movie("The Matrix", "Lana Wachowski", 1999, "5.The Matrix.jpeg"));
            moviesToInsert.add(new Movie("The Lord of the Rings: The Return of the King", "Peter Jackson", 2003, "6.The Lord of the Rings- The Return of the King.jpeg"));
            moviesToInsert.add(new Movie("Interstellar", "Christopher Nolan", 2014, "7.Interstellar.jpeg"));
            moviesToInsert.add(new Movie("Pulp Fiction", "Quentin Tarantino", 1994, "8.Pulp Fiction.jpg"));
            moviesToInsert.add(new Movie("The Lion King", "Roger Allers", 1994, "9.The Lion King.jpeg"));
            moviesToInsert.add(new Movie("Grave of the Fireflies", "Isao Takahata", 1988, "10.Grave of the Fireflies.jpeg"));

            for (Movie movie : moviesToInsert) {
                pstmt.setString(1, movie.getTitle());
                pstmt.setString(2, movie.getDirector());
                pstmt.setInt(3, movie.getYear());
                pstmt.setString(4, movie.getPosterUrl());
                pstmt.executeUpdate();
            }
            System.out.println("Sample movies inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title, director, year, poster_url FROM movies;";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getString("poster_url")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}