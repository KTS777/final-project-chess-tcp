package server.db;

import database.DatabaseManager;

public class DatabaseTest {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        try {
            dbManager.connect("localhost", "chess_db", "postgres", "password" );
            // Dummy data for now — use 1, 2 as placeholder user IDs, "1-0" as result
            dbManager.saveGame(3, 4, "WHITE", "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.disconnect();
        }
    }
}
