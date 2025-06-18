package model.pieces;

import model.Piece;
import model.Square;
import view.Board;

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    private static final int[][] KING_MOVES = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    public King(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }


    @Override
    public List<Square> getLegalMoves(Board board) {
        List<Square> legalMoves = new LinkedList<>();
        Square[][] squares = board.getSquareArray();

        int x = getPosition().getBoardX();
        int y = getPosition().getBoardY();

        for (int[] move : KING_MOVES) {
            int newX = x + move[0];
            int newY = y + move[1];

            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Square target = squares[newY][newX];
                if (!target.isOccupied() || target.getOccupyingPiece().getColor() != getColor()) {
                    legalMoves.add(target);
                }
            }
        }

        return legalMoves;
    }

    @Override
    public String getSymbol() {
        return "K";
    }
}
