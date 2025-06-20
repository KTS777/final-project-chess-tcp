package controller;

import model.Piece;
import model.Square;
import model.pieces.Pawn;
import view.Board;

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

    private Square lastDoubleStepSquare;



    public GameController(CheckmateDetector checkmateDetector, Board board) {
        this.checkmateDetector = checkmateDetector;
        this.board = board;
        this.whiteTurn = true;
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

    public boolean handlePieceDrop(Square targetSquare) {
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

    private void applyMove(Square targetSquare) {
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
            currPiece = null;
            switchTurn();
        }
    }

    private void cancelMove() {
        if (currPiece != null) {
            currPiece.getPosition().setDisplay(true);
            currPiece = null;
        }
    }

    private void finishGame(int winningColor) {
        this.gameOver = true;
        this.winningColor = winningColor;
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
