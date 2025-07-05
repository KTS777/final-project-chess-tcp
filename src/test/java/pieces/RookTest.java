package pieces;


import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.Rook;

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
    void testRookMovesInAllDirectionsUnblocked() {
        Rook rook = new Rook(true, board.getSquare(4, 4), "wrook.png");
        board.getSquare(4, 4).setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        // Vertical up
        for (int y = 0; y < 4; y++) {
            assertTrue(moves.contains(board.getSquare(4, y)), "Should include upward move to (4," + y + ")");
        }
        // Vertical down
        for (int y = 5; y <= 7; y++) {
            assertTrue(moves.contains(board.getSquare(4, y)), "Should include downward move to (4," + y + ")");
        }
        // Horizontal left
        for (int x = 0; x < 4; x++) {
            assertTrue(moves.contains(board.getSquare(x, 4)), "Should include left move to (" + x + ",4)");
        }
        // Horizontal right
        for (int x = 5; x <= 7; x++) {
            assertTrue(moves.contains(board.getSquare(x, 4)), "Should include right move to (" + x + ",4)");
        }
    }

    @Test
    void testRookBlockedBySameColor() {
        Rook rook = new Rook(true, board.getSquare(4, 4), "wrook.png");
        Piece blockerDown = new Rook(true, board.getSquare(4, 6), "wrook.png");  // Down
        Piece blockerLeft = new Rook(true, board.getSquare(2, 4), "wrook.png");  // Left

        board.getSquare(4, 4).setOccupyingPiece(rook);
        board.getSquare(4, 6).setOccupyingPiece(blockerDown);
        board.getSquare(2, 4).setOccupyingPiece(blockerLeft);

        List<Square> moves = rook.getLegalMoves(board);

        // Should include other legal moves
        assertTrue(moves.contains(board.getSquare(4, 3)), "Should include move upward");
        assertTrue(moves.contains(board.getSquare(5, 4)), "Should include move right");
        assertTrue(moves.contains(board.getSquare(6, 4)), "Should include move further right");
    }




    @Test
    void testRookInCorner() {
        Rook rook = new Rook(true, board.getSquare(0, 0), "wrook.png");
        board.getSquare(0, 0).setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        for (int i = 1; i <= 7; i++) {
            assertTrue(moves.contains(board.getSquare(0, i)), "Should include move down to (0," + i + ")");
            assertTrue(moves.contains(board.getSquare(i, 0)), "Should include move right to (" + i + ",0)");
        }
        assertEquals(14, moves.size(), "Rook in corner should have 14 legal moves");
    }

    @Test
    void testRookDoesNotIncludeOwnSquare() {
        Rook rook = new Rook(true, board.getSquare(3, 3), "wrook.png");
        board.getSquare(3, 3).setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        assertFalse(moves.contains(board.getSquare(3, 3)), "Rook's own square should not be a legal move");
    }
}