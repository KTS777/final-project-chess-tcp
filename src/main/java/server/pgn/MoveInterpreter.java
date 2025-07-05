package server.pgn;

public class MoveInterpreter {
    public static class ParsedMove {
        public String pieceCode;
        public String targetSquare;
        public String disambiguation;
        public boolean isCastlingKingside;
        public boolean isCastlingQueenside;
        public String promotion;

        public boolean isCastling() {
            return isCastlingKingside || isCastlingQueenside;
        }
    }

    public static ParsedMove parse(String rawMove) {
        ParsedMove result = new ParsedMove();
        String move = rawMove.replace("+", "").replace("#", "").replace("x", "");

        if (move.equals("O-O")) {
            result.isCastlingKingside = true;
            return result;
        }

        if (move.equals("O-O-O")) {
            result.isCastlingQueenside = true;
            return result;
        }

        if (move.contains("=")) {
            int idx = move.indexOf('=');
            result.promotion = move.substring(idx + 1);
            move = move.substring(0, idx);
        }

        if (move.matches("^[a-h][1-8]$")) {
            result.pieceCode = "P";
            result.targetSquare = move;
        } else if (move.matches("^[a-h][a-h][1-8]$")) {
            result.pieceCode = "P";
            result.disambiguation = move.substring(0, 1);
            result.targetSquare = move.substring(1);
        } else if (move.matches("^[NBRQK][a-h][a-h][1-8]$")) {
            result.pieceCode = move.substring(0, 1);
            result.disambiguation = move.substring(1, 2);
            result.targetSquare = move.substring(2);
        } else if (move.matches("^[NBRQK][1-8][a-h][1-8]$")) {
            result.pieceCode = move.substring(0, 1);
            result.disambiguation = move.substring(1, 2);
            result.targetSquare = move.substring(2);
        } else if (move.matches("^[NBRQK][a-h][1-8]$")) {
            result.pieceCode = move.substring(0, 1);
            result.targetSquare = move.substring(1);
        } else {
            System.out.println("Unsupported move format: " + rawMove);
            return null;
        }

        return result;
    }
}
