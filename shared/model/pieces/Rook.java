package model.pieces;

import model.Piece;
import model.Square;
import view.Board;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        Square[][] squares = board.getSquareArray();
        List<Square> legalMoves = new LinkedList<>();

        int x = getPosition().getBoardX();
        int y = getPosition().getBoardY();

        int[] limits = getLinearOccupations(squares, x, y);
        int top = limits[0];
        int bottom = limits[1];
        int left = limits[2];
        int right = limits[3];

        for (int i = top; i <= bottom; i++) {
            if (i != y) legalMoves.add(squares[i][x]);
        }

        for (int i = left; i <= right; i++) {
            if (i != x) legalMoves.add(squares[y][i]);
        }

        return legalMoves;
    }

    @Override
    public String getSymbol() {
        return "R";
    }
}
