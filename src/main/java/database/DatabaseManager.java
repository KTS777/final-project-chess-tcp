package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/chess_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private static Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Connected to PostgreSQL!");
        } catch (SQLException e) {
            System.err.println("[DB] Connection failed: " + e.getMessage());
        }
    }

    public static void connect(String host, String dbName, String user, String password) throws SQLException {
        String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to database.");
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGame(int whitePlayerId, int blackPlayerId, String result, String movesPGN) {
        String sql = "INSERT INTO games (white_player_id, black_player_id, winner, pgn) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, whitePlayerId);
            stmt.setInt(2, blackPlayerId);
            stmt.setString(3, result);
            stmt.setString(4, movesPGN);
            stmt.executeUpdate();
            System.out.println("[DB] Game saved successfully.");
        } catch (SQLException e) {
            System.err.println("[DB] Failed to save game: " + e.getMessage());
        }
    }

    public List<GameRecord> fetchAllGames() {
        List<GameRecord> games = new ArrayList<>();
        String sql = "SELECT * FROM games";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                GameRecord game = new GameRecord(
                        rs.getInt("white_player_id"),
                        rs.getInt("black_player_id"),
                        rs.getString("winner"),
                        rs.getString("pgn"),
                        rs.getTimestamp("played_at").toLocalDateTime()
                );
                games.add(game);
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to fetch games: " + e.getMessage());
        }

        return games;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to close connection: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
