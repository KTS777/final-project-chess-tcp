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

        return legalMoves;
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

}
