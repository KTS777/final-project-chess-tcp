package controller;

import model.Piece;
import model.pieces.*;
import pgn.MoveInterpreter;
import pgn.PGNParser;
import model.LogicBoard;

public class PGNGameReplayer {

    private static int[] lastDoubleStepPawn = null;

    public static boolean replayGame(PGNParser.ParsedGame game, StringBuilder outputBuffer) {
        LogicBoard board = new LogicBoard();
        boolean isWhiteTurn = true;

        for (String move : game.moves) {
            if (!applyMove(board, move, isWhiteTurn, outputBuffer)) {
                outputBuffer.append("Illegal move detected: ").append(move).append("\n");
                return false;
            }
            isWhiteTurn = !isWhiteTurn;
        }

        outputBuffer.append("Game is valid.\n");
        return true;
    }

    private static boolean applyMove(LogicBoard board, String move, boolean isWhiteTurn, StringBuilder outputBuffer) {
        MoveInterpreter.ParsedMove parsed = MoveInterpreter.parse(move);
        if (parsed == null) return false;

        int[] target = squareToCoords(parsed.targetSquare);
        if (target == null) return false;

        int toRow = target[0];
        int toCol = target[1];
        Piece[][] boardState = board.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = boardState[row][col];
                if (p == null || p.isWhite() != isWhiteTurn) continue;

                boolean matchType = parsed.pieceCode.equalsIgnoreCase(p.getSymbol());
                if (!matchType) continue;

                // Disambiguation
                if (parsed.disambiguation != null && !parsed.disambiguation.isEmpty()) {
                    char c = parsed.disambiguation.charAt(0);
                    if (Character.isLetter(c) && col != (c - 'a')) continue;
                    if (Character.isDigit(c) && row != (8 - Character.getNumericValue(c))) continue;
                }

                boolean isValid = (p instanceof Pawn pawn)
                        ? pawn.isValidMove(row, col, toRow, toCol, boardState, lastDoubleStepPawn)
                        : p.isValidMove(row, col, toRow, toCol, boardState);

                if (!isValid) continue;

                // Promotion
                if (p instanceof Pawn && parsed.promotion != null) {
                    Piece promoted;
                    switch (parsed.promotion.toUpperCase()) {
                        case "Q" -> promoted = new Queen(isWhiteTurn, null, null);
                        case "R" -> promoted = new Rook(isWhiteTurn, null, null);
                        case "B" -> promoted = new Bishop(isWhiteTurn, null, null);
                        case "N" -> promoted = new Knight(isWhiteTurn, null, null);
                        default -> {
                            outputBuffer.append("Invalid promotion: ").append(parsed.promotion).append("\n");
                            return false;
                        }
                    }
                    board.placePiece(promoted, toRow, toCol);
                    board.placePiece(null, row, col);
                }
                // En passant capture
                else if (p instanceof Pawn &&
                        Math.abs(col - toCol) == 1 &&
                        board.getPiece(toRow, toCol) == null) {
                    board.movePiece(row, col, toRow, toCol);
                    int capturedRow = isWhiteTurn ? toRow + 1 : toRow - 1;
                    board.placePiece(null, capturedRow, toCol);
                }
                // Normal move
                else {
                    board.movePiece(row, col, toRow, toCol);
                }

                if (p instanceof Pawn && Math.abs(toRow - row) == 2) {
                    lastDoubleStepPawn = new int[]{toRow, toCol};
                } else {
                    lastDoubleStepPawn = null;
                }

                outputBuffer.append("Move ").append(move).append(" applied.\n");
                return true;
            }
        }

        outputBuffer.append("No valid ").append(parsed.pieceCode)
                .append(" found to perform move: ").append(move).append("\n");
        return false;
    }

    private static int[] squareToCoords(String square) {
        if (square.length() != 2) return null;

        char file = square.charAt(0); // a–h
        char rank = square.charAt(1); // 1–8

        int col = file - 'a';
        int row = 8 - Character.getNumericValue(rank);

        if (col < 0 || col > 7 || row < 0 || row > 7) return null;

        return new int[]{row, col};
    }
}
