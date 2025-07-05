package pieces;



import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Square;
import shared.model.pieces.Bishop;

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
    public void testNotMoveOffBoard() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Bishop edgeBishop = new Bishop(true, board.getSquare(0, 0), null);
            board.getSquare(0, 0).setOccupyingPiece(edgeBishop);

            List<Square> moves = edgeBishop.getLegalMoves(board);

            // Only NE direction should be available (e.g., (1,1), (2,2), ...)
            // Confirm that all moves are inside board boundaries (0-7)
            for (Square move : moves) {
                int x = move.getX();
                int y = move.getY();
                assertTrue(x >= 0 && x < 8, "Move x out of bounds: " + x);
                assertTrue(y >= 0 && y < 8, "Move y out of bounds: " + y);
            }

            // Specifically check that (1,1) is a legal move
            assertTrue(moves.contains(board.getSquare(1, 1)));

            // Do NOT check moves containing invalid squares like (-1, -1)
            // because those squares don't exist in the board.
        });
    }
}