package shared.model.pieces;

import client.view.Board;
import shared.model.Piece;
import shared.model.Square;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, Square initSq, String imgPath) {
        super(isWhite, initSq, imgPath);
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        Square[][] squares = board.getSquareArray();
        int x = getPosition().getBoardX(); // or getXNum(), based on your API
        int y = getPosition().getBoardY(); // or getYNum()

        return getDiagonalOccupations(squares, x, y);
    }

    @Override
    public String getSymbol() {
        return "B";
    }
}
