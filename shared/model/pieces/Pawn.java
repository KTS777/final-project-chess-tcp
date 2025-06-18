package model.pieces;

import model.Piece;
import model.Square;
import view.Board;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<>();
        Square[][] board = b.getSquareArray();

        int x = getPosition().getBoardX();
        int y = getPosition().getBoardY();
        int dir = isWhite() ? -1 : 1;

        Square enPassantTarget = null;
        if (b.getGameController() != null) {
            enPassantTarget = b.getGameController().getLastDoubleStepSquare();
        }

        if (inBounds(y + dir) && !board[y + dir][x].isOccupied()) {
            legalMoves.add(board[y + dir][x]);

            if (!wasMoved()) {
                int doubleStepY = y + 2 * dir;
                if (inBounds(doubleStepY) && !board[doubleStepY][x].isOccupied()) {
                    legalMoves.add(board[doubleStepY][x]);
                }
            }
        }

        for (int dx : new int[]{-1, 1}) {
            int targetX = x + dx;
            int targetY = y + dir;
            if (inBounds(targetX) && inBounds(targetY)) {
                Square target = board[targetY][targetX];
                if (target.isOccupied() && target.getOccupyingPiece().getColor() != getColor()) {
                    legalMoves.add(target);
                }
                if (enPassantTarget != null && enPassantTarget.getBoardX() == targetX && enPassantTarget.getBoardY() == y) {
                    legalMoves.add(board[targetY][targetX]);
                }
            }
        }

        return legalMoves;
    }

    public boolean isPromotionRank() {
        int y = getPosition().getBoardY();
        return (isWhite() && y == 0) || (!isWhite() && y == 7);
    }

    private boolean inBounds(int i) {
        return i >= 0 && i < 8;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "P" : "p";
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        return isValidMove(fromRow, fromCol, toRow, toCol, board, null);
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board, int[] lastDoubleStepPawn) {
        int dir = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;

        Piece target = board[toRow][toCol];

        // Diagonal capture (including en passant)
        if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + dir) {
            if (target != null && target.isWhite() != this.isWhite()) {
                return true;
            }

            // En passant
            if (target == null && lastDoubleStepPawn != null &&
                    lastDoubleStepPawn[0] == fromRow && lastDoubleStepPawn[1] == toCol) {
                return true;
            }
        }

        // Forward single
        if (fromCol == toCol && toRow == fromRow + dir && target == null) {
            return true;
        }

        // Forward double
        if (fromCol == toCol && fromRow == startRow && toRow == fromRow + 2 * dir) {
            if (board[fromRow + dir][toCol] == null && target == null) {
                return true;
            }
        }

        return false;
    }
}
