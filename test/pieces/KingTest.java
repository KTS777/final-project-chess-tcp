package pieces;

import model.Piece;
import model.Square;
import model.pieces.King;
import model.pieces.Knight;
import model.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(null);
    }

    @Test
    public void testKingStandardMoves() {
        King king = new King(true, board.getSquare(4, 4), "wk.png"); // White king
        board.getSquare(4, 4).setOccupyingPiece(king);

        List<Square> moves = king.getLegalMoves(board);

        assertTrue(moves.contains(board.getSquare(3, 3)));
        assertTrue(moves.contains(board.getSquare(3, 4)));
        assertTrue(moves.contains(board.getSquare(3, 5)));
        assertTrue(moves.contains(board.getSquare(4, 3)));
        assertTrue(moves.contains(board.getSquare(4, 5)));
        assertTrue(moves.contains(board.getSquare(5, 3)));
        assertTrue(moves.contains(board.getSquare(5, 4)));
        assertTrue(moves.contains(board.getSquare(5, 5)));
        assertEquals(8, moves.size());
    }

    @Test
    public void testKingBlockedByFriendlyPieces() {
        King king = new King(true, board.getSquare(4, 4), "wk.png");
        board.getSquare(4, 4).setOccupyingPiece(king);

        // Surround king with white rooks
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = 4 + dx;
                int y = 4 + dy;
                if (x == 4 && y == 4) continue;
                Piece p = new Rook(true, board.getSquare(x, y), "wr.png");
                board.getSquare(x, y).setOccupyingPiece(p);
            }
        }

        List<Square> moves = king.getLegalMoves(board);
        assertEquals(0, moves.size());
    }

    @Test
    public void testKingCanCaptureEnemies() {
        King king = new King(false, board.getSquare(4, 4), "bk.png"); // Black king
        board.getSquare(4, 4).setOccupyingPiece(king);

        Piece enemy1 = new Knight(true, board.getSquare(3, 3), "wn.png"); // White knight
        Piece enemy2 = new Knight(true, board.getSquare(4, 5), "wn.png");
        board.getSquare(3, 3).setOccupyingPiece(enemy1);
        board.getSquare(4, 5).setOccupyingPiece(enemy2);

        List<Square> moves = king.getLegalMoves(board);
        assertTrue(moves.contains(board.getSquare(3, 3)));
        assertTrue(moves.contains(board.getSquare(4, 5)));
    }
}