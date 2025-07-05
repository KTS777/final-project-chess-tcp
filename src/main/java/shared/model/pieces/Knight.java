package shared.model.pieces;



import client.view.Board;
import shared.model.Piece;
import shared.model.Square;

import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece {

    private static final int[][] KNIGHT_MOVES = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
    };

    public Knight(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        List<Square> legalMoves = new LinkedList<>();
        Square[][] squares = board.getSquareArray();

        int x = getPosition().getBoardX();
        int y = getPosition().getBoardY();

        for (int[] move : KNIGHT_MOVES) {
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
        return "N";
    }
}
