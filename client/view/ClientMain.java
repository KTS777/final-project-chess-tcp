package view;

import controller.PGNGameReplayer;
import pgn.PGNParser;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class ClientMain implements Runnable {
    public void run() {
        // Launch GUI
        SwingUtilities.invokeLater(new StartMenu());

        // --- PGN Replay Demo ---
        try {
            String path = "games/example.pgn";
            File pgnFile = new File(path);
            if (!pgnFile.exists()) {
                System.out.println("PGN file not found: " + pgnFile.getAbsolutePath());
                return;
            }

            PGNParser parser = new PGNParser();
            List<PGNParser.ParsedGame> games = parser.parseFile(path);

            if (games.isEmpty()) {
                System.out.println("No games found in PGN file.");
                return;
            }

            PGNParser.ParsedGame game = games.get(0);
            System.out.println("Tags: " + game.tags);
            System.out.println("Moves: " + game.moves);
            if (!game.syntaxErrors.isEmpty()) {
                System.out.println("Syntax errors: " + game.syntaxErrors);
            }


            StringBuilder output = new StringBuilder();
            boolean valid = PGNGameReplayer.replayGame(game, output);
            System.out.println(output);
            System.out.println("Game was " + (valid ? "valid ✅" : "invalid ❌"));

        } catch (Exception e) {
            System.out.println("Error during PGN replay: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ClientMain().run();
    }
}
