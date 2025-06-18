package model;

import model.pieces.*;

public class LogicBoard {

    private Piece[][] board = new Piece[8][8];

    public LogicBoard() {
        setupInitialPosition();
    }

    public LogicBoard(boolean empty) {
        if (!empty) {
            setupInitialPosition();
        } else {
            board = new Piece[8][8]; // empty board
        }
    }

    private void setupInitialPosition() {
        // Pawns
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Pawn(true, null, null);  // white
            board[1][col] = new Pawn(false, null, null);  // black
        }

        // Rooks
        board[7][0] = new Rook(true, null, null);
        board[7][7] = new Rook(true, null, null);
        board[0][0] = new Rook(false, null, null);
        board[0][7] = new Rook(false, null, null);

        // Knights
        board[7][1] = new Knight(true, null, null);
        board[7][6] = new Knight(true, null, null);
        board[0][1] = new Knight(false, null, null);
        board[0][6] = new Knight(false, null, null);

        // Bishops
        board[7][2] = new Bishop(true, null, null);
        board[7][5] = new Bishop(true, null, null);
        board[0][2] = new Bishop(false, null, null);
        board[0][5] = new Bishop(false, null, null);

        // Queens
        board[7][3] = new Queen(true, null, null);
        board[0][3] = new Queen(false, null, null);

        // Kings
        board[7][4] = new King(true, null, null);
        board[0][4] = new King(false, null, null);
    }

    public void placePiece(Piece piece, int row, int col) {
        board[row][col] = piece;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol];
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, int[] lastDoubleStepPawn) {
        Piece piece = getPiece(fromRow, fromCol);
        if (piece == null) return false;

        if (piece instanceof Pawn) {
            return ((Pawn) piece).isValidMove(fromRow, fromCol, toRow, toCol, board, lastDoubleStepPawn);
        }

        return piece.isValidMove(fromRow, fromCol, toRow, toCol, board);
    }

}
