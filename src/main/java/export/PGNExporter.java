package export;

import database.GameRecord;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PGNExporter {

    public static void exportGame(GameRecord game, String filePath) {
        if (!filePath.toLowerCase().endsWith(".pgn")) {
            filePath += ".pgn";
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(buildPGNString(game));
            JOptionPane.showMessageDialog(null, "Game exported successfully!");
            System.out.println("[PGN] Exported to: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to export game: " + e.getMessage());
        }
    }


    private static String buildPGNString(GameRecord game) {
        return String.format("""
            [White "%d"]
            [Black "%d"]
            [Result "%s"]
            [Date "%s"]

            %s
            """,
                game.getWhitePlayerId(),  // Replace with username if you add name fields later
                game.getBlackPlayerId(),
                game.getWinner(),
                game.getPlayedAt().toLocalDate(),
                game.getPgn()
        ).trim();
    }
}
