package pieces;

import model.Square;
import model.pieces.Bishop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {
    private Board board;
    private Bishop whiteBishop;

    @BeforeEach
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board(null);
            board.setupEmptyBoard();
            Square startSquare = board.getSquare(3, 3);
            whiteBishop = new Bishop(true, startSquare, null); // updated constructor
            startSquare.setOccupyingPiece(whiteBishop);
        });
    }

    @Test
    public void testEmptyBoardDiagonalMoves() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<Square> moves = whiteBishop.getLegalMoves(board);

            assertTrue(moves.contains(board.getSquare(2, 2)));
            assertTrue(moves.contains(board.getSquare(1, 1)));
            assertTrue(moves.contains(board.getSquare(0, 0)));

            assertTrue(moves.contains(board.getSquare(4, 4)));
            assertTrue(moves.contains(board.getSquare(5, 5)));
            assertTrue(moves.contains(board.getSquare(6, 6)));
            assertTrue(moves.contains(board.getSquare(7, 7)));

            assertTrue(moves.contains(board.getSquare(2, 4)));
            assertTrue(moves.contains(board.getSquare(1, 5)));
            assertTrue(moves.contains(board.getSquare(0, 6)));

            assertTrue(moves.contains(board.getSquare(4, 2)));
            assertTrue(moves.contains(board.getSquare(5, 1)));
            assertTrue(moves.contains(board.getSquare(6, 0)));
        });
    }

    @Test
    public void testBishopBlockedByOwnPiece() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Bishop blocker = new Bishop(true, board.getSquare(5, 5), null);
            board.getSquare(5, 5).setOccupyingPiece(blocker);

            List<Square> moves = whiteBishop.getLegalMoves(board);
            assertFalse(moves.contains(board.getSquare(5, 5)));
            assertFalse(moves.contains(board.getSquare(6, 6)));
        });
    }

    @Test
    public void testBishopCanCaptureOpponent() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Bishop blackBishop = new Bishop(false, board.getSquare(1, 1), null);
            board.getSquare(1, 1).setOccupyingPiece(blackBishop);

            List<Square> moves = whiteBishop.getLegalMoves(board);
            assertTrue(moves.contains(board.getSquare(1, 1)));
        });
    }
}