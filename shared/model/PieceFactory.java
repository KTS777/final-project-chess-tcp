package model;

import model.pieces.*;
import java.util.List;
import static view.PieceImages.*;

public class PieceFactory {

    private static final int ROW_WHITE_BACK = 7;
    private static final int ROW_WHITE_PAWN = 6;
    private static final int ROW_BLACK_BACK = 0;
    private static final int ROW_BLACK_PAWN = 1;

    public static King[] createStandardSetup(Square[][] board, List<Piece> whitePieces, List<Piece> blackPieces) {

        for (int x = 0; x < 8; x++) {
            placePiece(new Pawn(false, board[ROW_BLACK_PAWN][x], BPAWN), board[ROW_BLACK_PAWN][x], blackPieces);
            placePiece(new Pawn(true, board[ROW_WHITE_PAWN][x], WPAWN), board[ROW_WHITE_PAWN][x], whitePieces);
        }

        Queen bq = placePiece(new Queen(false, board[ROW_BLACK_BACK][3], BQUEEN), board[ROW_BLACK_BACK][3], blackPieces);
        Queen wq = placePiece(new Queen(true, board[ROW_WHITE_BACK][3], WQUEEN), board[ROW_WHITE_BACK][3], whitePieces);

        King bk = placePiece(new King(false, board[ROW_BLACK_BACK][4], BKING), board[ROW_BLACK_BACK][4], blackPieces);
        King wk = placePiece(new King(true, board[ROW_WHITE_BACK][4], WKING), board[ROW_WHITE_BACK][4], whitePieces);

        placePiece(new Rook(false, board[ROW_BLACK_BACK][0], BROOK), board[ROW_BLACK_BACK][0], blackPieces);
        placePiece(new Rook(false, board[ROW_BLACK_BACK][7], BROOK), board[ROW_BLACK_BACK][7], blackPieces);
        placePiece(new Rook(true, board[ROW_WHITE_BACK][0], WROOK), board[ROW_WHITE_BACK][0], whitePieces);
        placePiece(new Rook(true, board[ROW_WHITE_BACK][7], WROOK), board[ROW_WHITE_BACK][7], whitePieces);

        placePiece(new Knight(false, board[ROW_BLACK_BACK][1], BKNIGHT), board[ROW_BLACK_BACK][1], blackPieces);
        placePiece(new Knight(false, board[ROW_BLACK_BACK][6], BKNIGHT), board[ROW_BLACK_BACK][6], blackPieces);
        placePiece(new Knight(true, board[ROW_WHITE_BACK][1], WKNIGHT), board[ROW_WHITE_BACK][1], whitePieces);
        placePiece(new Knight(true, board[ROW_WHITE_BACK][6], WKNIGHT), board[ROW_WHITE_BACK][6], whitePieces);

        placePiece(new Bishop(false, board[ROW_BLACK_BACK][2], BBISHOP), board[ROW_BLACK_BACK][2], blackPieces);
        placePiece(new Bishop(false, board[ROW_BLACK_BACK][5], BBISHOP), board[ROW_BLACK_BACK][5], blackPieces);
        placePiece(new Bishop(true, board[ROW_WHITE_BACK][2], WBISHOP), board[ROW_WHITE_BACK][2], whitePieces);
        placePiece(new Bishop(true, board[ROW_WHITE_BACK][5], WBISHOP), board[ROW_WHITE_BACK][5], whitePieces);

        return new King[]{ wk, bk };
    }

    private static <T extends Piece> T placePiece(T piece, Square square, List<Piece> collection) {
        square.setOccupyingPiece(piece);
        collection.add(piece);
        return piece;
    }
}
