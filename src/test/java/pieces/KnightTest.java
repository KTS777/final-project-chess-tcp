package pieces;

import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.Knight;
import shared.model.pieces.Rook;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {

    private Board board;
    private Knight knight;

    @BeforeEach
    public void setUp() {
        board = new Board(null);
        board.setupEmptyBoard();
        knight = new Knight(true, board.getSquare(4, 4), "wn.png"); // White knight
        board.getSquare(4, 4).setOccupyingPiece(knight);
    }

    @Test
    public void testKnightMovesFromCenter() {
        List<Square> legalMoves = knight.getLegalMoves(board);
        int[][] expectedMoves = {
                {2, 3}, {2, 5}, {3, 2}, {3, 6},
                {5, 2}, {5, 6}, {6, 3}, {6, 5}
        };
        for (int[] move : expectedMoves) {
            assertTrue(legalMoves.contains(board.getSquare(move[1], move[0])),
                    "Expected move: (" + move[0] + ", " + move[1] + ")");
        }
        assertEquals(8, legalMoves.size());
    }

    @Test
    public void testKnightMovesFromCorner() {
        knight.setPosition(board.getSquare(0, 0));
        board.getSquare(4, 4).removePiece();
        board.getSquare(0, 0).setOccupyingPiece(knight);

        List<Square> legalMoves = knight.getLegalMoves(board);
        assertEquals(2, legalMoves.size());
        assertTrue(legalMoves.contains(board.getSquare(2, 1)));
        assertTrue(legalMoves.contains(board.getSquare(1, 2)));
    }

    @Test
    public void testKnightCanCaptureEnemy() {
        Piece enemy = new Rook(false, board.getSquare(3, 2), "br.png"); // Black rook at (3, 2)
        board.getSquare(3, 2).setOccupyingPiece(enemy);

        List<Square> legalMoves = knight.getLegalMoves(board);
        assertTrue(legalMoves.contains(board.getSquare(3, 2)));
    }

    @Test
    public void testKnightCannotCaptureFriendly() {
        Piece friendly = new Rook(true, board.getSquare(3, 2), "wr.png"); // Friendly rook at (3, 2)
        board.getSquare(3, 2).setOccupyingPiece(friendly);

        List<Square> legalMoves = knight.getLegalMoves(board);
        assertFalse(legalMoves.contains(board.getSquare(3, 2)));
    }
}
