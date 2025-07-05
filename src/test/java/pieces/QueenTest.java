package pieces;


import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Square;
import shared.model.pieces.Pawn;
import shared.model.pieces.Queen;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {


    private Board board;
    private Queen queen;

    @BeforeEach
    public void setUp() {
        board = new Board(null);
        board.setupEmptyBoard();
    }


    @Test
    public void testQueenMovesInAllDirections() {
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


}