package model.pieces;

import model.Piece;
import model.Square;
import view.Board;

import java.util.LinkedList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        List<Square> legalMoves = new LinkedList<>();
        Square[][] squares = board.getSquareArray();

        int x = getPosition().getBoardX();
        int y = getPosition().getBoardY();

        // Rook-like linear moves
        int[] bounds = getLinearOccupations(squares, x, y);

        for (int i = bounds[0]; i < y; i++) legalMoves.add(squares[i][x]);         // Up
        for (int i = y + 1; i <= bounds[1]; i++) legalMoves.add(squares[i][x]);    // Down
        for (int i = bounds[2]; i < x; i++) legalMoves.add(squares[y][i]);         // Left
        for (int i = x + 1; i <= bounds[3]; i++) legalMoves.add(squares[y][i]);    // Right

        // Bishop-like diagonal moves
        legalMoves.addAll(getDiagonalOccupations(squares, x, y));

        System.out.println("Queen at " + getPosition().getXNum() + "," + getPosition().getYNum());
        System.out.println("Legal moves: ");
        for (Square sq : legalMoves) {
            System.out.println(" -> " + sq.getXNum() + "," + sq.getYNum());
        }


        return legalMoves;
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        int dx = Math.abs(toCol - fromCol);
        int dy = Math.abs(toRow - fromRow);

        if (dx == dy) {
            // Diagonal (bishop-style)
            int stepRow = (toRow - fromRow) / dy;
            int stepCol = (toCol - fromCol) / dx;
            for (int i = 1; i < dx; i++) {
                if (board[fromRow + i * stepRow][fromCol + i * stepCol] != null)
                    return false;
            }
            return canCaptureOrMove(toRow, toCol, board);
        } else if (dx == 0 || dy == 0) {
            // Rook-style
            int stepRow = Integer.compare(toRow, fromRow);
            int stepCol = Integer.compare(toCol, fromCol);
            int steps = Math.max(dx, dy);
            for (int i = 1; i < steps; i++) {
                int r = fromRow + stepRow * i;
                int c = fromCol + stepCol * i;
                if (board[r][c] != null) return false;
            }
            return canCaptureOrMove(toRow, toCol, board);
        }

        return false;
    }

    private boolean canCaptureOrMove(int r, int c, Piece[][] board) {
        return board[r][c] == null || board[r][c].isWhite() != this.isWhite();
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

}
