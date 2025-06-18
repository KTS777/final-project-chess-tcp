package pieces;

import model.Square;
import model.pieces.Pawn;
import model.pieces.Queen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(null);
        board.setupEmptyBoard();
    }

    @Test
    public void testQueenMovesDiagonallyAndStraight() {
        Queen queen = new Queen(true, board.getSquare(3, 3), "wq.png");
        board.getSquare(3, 3).setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        // Diagonal moves
        assertTrue(moves.contains(board.getSquare(2, 2)));
        assertTrue(moves.contains(board.getSquare(4, 4)));
        assertTrue(moves.contains(board.getSquare(2, 4)));
        assertTrue(moves.contains(board.getSquare(4, 2)));

        // Straight moves
        assertTrue(moves.contains(board.getSquare(3, 2)));
        assertTrue(moves.contains(board.getSquare(3, 4)));
        assertTrue(moves.contains(board.getSquare(2, 3)));
        assertTrue(moves.contains(board.getSquare(4, 3)));
    }

    @Test
    public void testQueenBlockedByFriendlyPiece() {
        Queen queen = new Queen(true, board.getSquare(3, 3), "wq.png");
        board.getSquare(3, 3).setOccupyingPiece(queen);
        board.getSquare(3, 4).setOccupyingPiece(new Pawn(true, board.getSquare(3, 4), "wp.png")); // friendly pawn

        List<Square> moves = queen.getLegalMoves(board);

        assertFalse(moves.contains(board.getSquare(3, 4)));
    }

    @Test
    public void testQueenCanCaptureEnemyPiece() {
        Queen queen = new Queen(true, board.getSquare(3, 3), "wq.png");
        board.getSquare(3, 3).setOccupyingPiece(queen);
        board.getSquare(3, 4).setOccupyingPiece(new Pawn(false, board.getSquare(3, 4), "bp.png")); // enemy pawn

        List<Square> moves = queen.getLegalMoves(board);

        assertTrue(moves.contains(board.getSquare(3, 4)));
    }
}