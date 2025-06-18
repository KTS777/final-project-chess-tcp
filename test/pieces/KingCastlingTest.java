package pieces;

import controller.MoveService;
import model.Piece;
import model.Square;
import model.pieces.King;
import model.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;

import static org.junit.jupiter.api.Assertions.*;

public class KingCastlingTest {

    private Board board;
    private MoveService moveService;

    @BeforeEach
    void setUp() {
        board = new Board(null);
        board.setupEmptyBoard();
        moveService = new MoveService();
    }

    @Test
    void testWhiteKingsideCastling() {
        Square kingSquare = board.getSquare(4, 7); // e1
        Square rookSquare = board.getSquare(7, 7); // h1

        King king = new King(true, kingSquare, "wk.png");
        Rook rook = new Rook(true, rookSquare, "wr.png");

        kingSquare.setOccupyingPiece(king);
        rookSquare.setOccupyingPiece(rook);

        board.getWhitePieces().add(king);
        board.getWhitePieces().add(rook);

        Square castlingTarget = board.getSquare(6, 7); // g1

        boolean result = moveService.applyMove(king, castlingTarget, board);
        assertTrue(result, "Castling should be allowed");

        assertEquals(king, board.getSquare(6, 7).getOccupyingPiece());

        Piece movedRook = board.getSquare(5, 7).getOccupyingPiece();
        assertTrue(movedRook instanceof Rook, "Rook should have moved to f1");
    }

    @Test
    void testBlackQueensideCastling() {
        Square kingSquare = board.getSquare(4, 0); // e8
        Square rookSquare = board.getSquare(0, 0); // a8

        King king = new King(false, kingSquare, "bk.png");
        Rook rook = new Rook(false, rookSquare, "br.png");

        kingSquare.setOccupyingPiece(king);
        rookSquare.setOccupyingPiece(rook);

        board.getBlackPieces().add(king);
        board.getBlackPieces().add(rook);

        Square castlingTarget = board.getSquare(2, 0); // c8

        boolean result = moveService.applyMove(king, castlingTarget, board);
        assertTrue(result, "Queenside castling should be allowed");

        assertEquals(king, board.getSquare(2, 0).getOccupyingPiece());

        Piece movedRook = board.getSquare(3, 0).getOccupyingPiece();
        assertTrue(movedRook instanceof Rook, "Rook should have moved to d8");
    }
}
