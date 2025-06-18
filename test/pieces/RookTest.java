package pieces;

import model.Piece;
import model.Square;
import model.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(null);
        board.setupEmptyBoard();
    }

    @Test
    void testRookMovesStraight() {
        Rook rook = new Rook(true, board.getSquare(3, 3), "wrook.png");
        board.getSquare(3, 3).setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        assertTrue(moves.contains(board.getSquare(3, 0)));
        assertTrue(moves.contains(board.getSquare(3, 7)));
        assertTrue(moves.contains(board.getSquare(0, 3)));
        assertTrue(moves.contains(board.getSquare(7, 3)));
    }

    @Test
    void testRookBlockedBySameColorPiece() {
        Rook rook = new Rook(true, board.getSquare(3, 3), "wrook.png");
        Piece blocker = new Rook(true, board.getSquare(3, 5), "wrook.png");

        board.getSquare(3, 3).setOccupyingPiece(rook);
        board.getSquare(3, 5).setOccupyingPiece(blocker);

        List<Square> moves = rook.getLegalMoves(board);

        assertFalse(moves.contains(board.getSquare(3, 5)));
        assertFalse(moves.contains(board.getSquare(3, 6)));
    }

    @Test
    void testRookCapturesOpponent() {
        Rook rook = new Rook(true, board.getSquare(3, 3), "wrook.png");
        Piece enemy = new Rook(false, board.getSquare(3, 5), "brook.png");

        board.getSquare(3, 3).setOccupyingPiece(rook);
        board.getSquare(3, 5).setOccupyingPiece(enemy);

        List<Square> moves = rook.getLegalMoves(board);

        assertTrue(moves.contains(board.getSquare(3, 5)));
    }
}