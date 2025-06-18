package view;

import model.Piece;
import model.Square;

import java.awt.*;

public class BoardRenderer {

    public void render(Graphics g, Square[][] board, Piece draggedPiece, boolean isWhiteTurn, int dragX, int dragY) {
        drawSquares(g, board);
        drawDraggedPiece(g, draggedPiece, isWhiteTurn, dragX, dragY);
    }

    private void drawSquares(Graphics g, Square[][] board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[y][x].paintComponent(g);
            }
        }
    }

    private void drawDraggedPiece(Graphics g, Piece piece, boolean isWhiteTurn, int dragX, int dragY) {
        if (piece == null) return;

        if ((piece.getColor() == 1 && isWhiteTurn) ||
                (piece.getColor() == 0 && !isWhiteTurn)) {
            final Image img = piece.getImage();
            g.drawImage(img, dragX, dragY, null);
        }
    }
}
