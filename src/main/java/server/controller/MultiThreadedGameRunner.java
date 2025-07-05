package server.controller;


import server.pgn.PGNParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedGameRunner {

    public static void replayGamesInParallel(List<PGNParser.ParsedGame> games) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<Boolean>> results = new ArrayList<>();

        for (PGNParser.ParsedGame game : games) {
            results.add(executor.submit(() -> {
                StringBuilder outputBuffer = new StringBuilder();
                boolean isValid = PGNGameReplayer.replayGame(game, outputBuffer);
                synchronized (System.out) {
                    System.out.println(outputBuffer.toString());
                }
                return isValid;
            }));
        }

        executor.shutdown();

        int validGames = 0;
        int invalidGames = 0;

        for (Future<Boolean> result : results) {
            try {
                if (result.get()) {
                    validGames++;
                } else {
                    invalidGames++;
                }
            } catch (Exception e) {
                System.out.println("Error replaying game: " + e.getMessage());
                invalidGames++;
            }
        }

        System.out.println("All games finished.");
        System.out.println("Valid games: " + validGames);
        System.out.println("Invalid games: " + invalidGames);
    }
}
