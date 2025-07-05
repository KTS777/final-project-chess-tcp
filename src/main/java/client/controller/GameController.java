package client.controller;


import client.view.Board;
import database.DatabaseManager;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.Pawn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    private boolean whiteTurn;
    private Piece currPiece;
    private final CheckmateDetector checkmateDetector;
    private boolean gameOver = false;
    private int winningColor = -1;
    private final Board board;
    private final MoveService moveService = new MoveService();
    private final boolean isWhitePlayer;


    private Square lastDoubleStepSquare;

    private final List<String> movesPGN = new ArrayList<>();



    public GameController(CheckmateDetector checkmateDetector, Board board, boolean isWhitePlayer) {
        this.checkmateDetector = checkmateDetector;
        this.board = board;
        this.isWhitePlayer = isWhitePlayer;
        this.whiteTurn = true;
    }


    public boolean isMyTurn() {
        return (whiteTurn && isWhitePlayer) || (!whiteTurn && !isWhitePlayer);
    }


    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void switchTurn() {
        whiteTurn = !whiteTurn;
    }

    public Piece getCurrentPiece() {
        return currPiece;
    }

    public void setCurrentPiece(Piece currPiece) {
        this.currPiece = currPiece;
    }

    public CheckmateDetector getCheckmateDetector() {
        return checkmateDetector;
    }

    public boolean isCorrectPlayerTurn(Piece piece) {
        return (piece.getColor() == WHITE && whiteTurn) ||
                (piece.getColor() == BLACK && !whiteTurn);
    }

    public void selectPiece(Square square) {
        if (!square.isOccupied()) return;

        Piece piece = square.getOccupyingPiece();
        if (!isCorrectPlayerTurn(piece)) return;

        this.currPiece = piece;
        square.setDisplay(false);
    }

    public boolean handlePieceDrop(Square targetSquare) throws SQLException {
        if (isMoveValid(targetSquare)) {
            applyMove(targetSquare);
            return true;
        } else {
            cancelMove();
            return false;
        }
    }

    private boolean isMoveValid(Square targetSquare) {
        return currPiece != null &&
                isCorrectPlayerTurn(currPiece) &&
                currPiece.getLegalMoves(board).contains(targetSquare) &&
                checkmateDetector.getAllowableSquares(whiteTurn).contains(targetSquare) &&
                checkmateDetector.testMove(currPiece, targetSquare);
    }

    private void applyMove(Square targetSquare) throws SQLException {
        String fromAlgebraic = currPiece.getPosition().getAlgebraic(); // <-- save from-square before move

        targetSquare.setDisplay(true);

        if (currPiece instanceof Pawn pawn) {
            int fromY = currPiece.getPosition().getYNum();
            int toY = targetSquare.getYNum();
            if (Math.abs(fromY - toY) == 2) {
                lastDoubleStepSquare = targetSquare;
            } else {
                lastDoubleStepSquare = null;
            }

            if (targetSquare.getXNum() != currPiece.getPosition().getXNum() && !targetSquare.isOccupied()) {
                int dir = (currPiece.getColor() == WHITE) ? -1 : 1;
                Square captured = board.getSquare(targetSquare.getXNum(), targetSquare.getYNum() - dir);
                captured.removePiece();
            }
        } else {
            lastDoubleStepSquare = null;
        }

        moveService.applyMove(currPiece, targetSquare, board);
        checkmateDetector.update();

        if (checkmateDetector.blackCheckMated()) {
            finishGame(WHITE);
        } else if (checkmateDetector.whiteCheckMated()) {
            finishGame(BLACK);
        } else {
            String pgnMove = convertToPGN(currPiece, fromAlgebraic, targetSquare);
            movesPGN.add(pgnMove);
            System.out.println("[PGN] " + pgnMove);
            currPiece = null;
            switchTurn();
        }
    }

    private String convertToPGN(Piece piece, String from, Square to) {
        String pieceCode = piece.getPGNCode(); // Usually returns "N", "B", etc. "P" for pawn
        String toAlgebraic = to.getAlgebraic();

        if (pieceCode.equals("")) {
            if (!to.isOccupied() && !from.substring(0, 1).equals(toAlgebraic.substring(0, 1))) {
                return from.charAt(0) + "x" + toAlgebraic;
            }
            return toAlgebraic;
        }

        return pieceCode + toAlgebraic;
    }


    private void cancelMove() {
        if (currPiece != null) {
            currPiece.getPosition().setDisplay(true);
            currPiece = null;
        }
    }

    private void finishGame(int winningColor) throws SQLException {
        if (gameOver) return;

        this.gameOver = true;
        this.winningColor = winningColor;

        String resultString = (winningColor == WHITE) ? "1-0" : (winningColor == BLACK ? "0-1" : "1/2-1/2");

        // Format PGN moves
        StringBuilder pgn = new StringBuilder();
        for (int i = 0; i < movesPGN.size(); i++) {
            if (i % 2 == 0) pgn.append((i / 2) + 1).append(". ");
            pgn.append(movesPGN.get(i)).append(" ");
        }


        String whiteUsername = "Alice";
        String blackUsername = "Bob";

        if (isWhitePlayer) {
            DatabaseManager db = new DatabaseManager();
            db.connect("localhost", "chess_db", "postgres", "password");
            db.saveGame(3, 4, resultString, pgn.toString().trim());
            db.disconnect();
        }

        // Print full PGN
        System.out.println("\nPGN:");
        for (int i = 0; i < movesPGN.size(); i++) {
            if (i % 2 == 0) System.out.print(((i / 2) + 1) + ". ");
            System.out.print(movesPGN.get(i) + " ");
        }
        System.out.println();

        System.out.println(resultString);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWinningColor() {
        return winningColor;
    }

    public Square getLastDoubleStepSquare() {
        return lastDoubleStepSquare;
    }

    public void setLastDoubleStepSquare(Square square) {
        this.lastDoubleStepSquare = square;
    }
}