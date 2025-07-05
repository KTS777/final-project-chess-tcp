package database;

import java.time.LocalDateTime;

public class GameRecord {
    private final int whitePlayerId;
    private final int blackPlayerId;
    private final String winner;
    private final String pgn;
    private final LocalDateTime playedAt;

    public GameRecord(int whitePlayerId, int blackPlayerId, String winner, String pgn, LocalDateTime playedAt) {
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.winner = winner;
        this.pgn = pgn;
        this.playedAt = playedAt;
    }

    public int getWhitePlayerId() {
        return whitePlayerId;
    }

    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    public String getWinner() {
        return winner;
    }

    public String getPgn() {
        return pgn;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    // Optional: for debugging or logging
    @Override
    public String toString() {
        return "GameRecord{" +
                "whitePlayerId=" + whitePlayerId +
                ", blackPlayerId=" + blackPlayerId +
                ", winner='" + winner + '\'' +
                ", pgn='" + pgn + '\'' +
                ", playedAt=" + playedAt +
                '}';
    }
}
