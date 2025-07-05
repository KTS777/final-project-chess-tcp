package pieces;

import client.controller.MoveService;
import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.Pawn;
import shared.model.pieces.Queen;
import shared.model.pieces.Rook;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Board board;
    private MoveService moveService;

    @BeforeEach
    void setup() {
        board = new Board(null);
        board.setupEmptyBoard();
        moveService = new MoveService();
    }

    // --- Pawn basic forward moves ---

    @Test
    void testPawnSingleMoveForward() {
        Pawn pawn = new Pawn(true, board.getSquare(4, 6), "wp.png");
        board.getSquare(4, 6).setOccupyingPiece(pawn);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertTrue(legalMoves.contains(board.getSquare(4, 5)));
    }

    @Test
    void testPawnDoubleMoveInitial() {
        Pawn pawn = new Pawn(true, board.getSquare(4, 6), "wp.png");
        board.getSquare(4, 6).setOccupyingPiece(pawn);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertTrue(legalMoves.contains(board.getSquare(4, 4)));
    }

    @Test
    void testPawnBlockedByPiece() {
        Pawn pawn = new Pawn(true, board.getSquare(4, 6), "wp.png");
        board.getSquare(4, 6).setOccupyingPiece(pawn);

        Piece blocker = new Rook(false, board.getSquare(4, 5), "br.png");
        board.getSquare(4, 5).setOccupyingPiece(blocker);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertFalse(legalMoves.contains(board.getSquare(4, 5)));
        assertFalse(legalMoves.contains(board.getSquare(4, 4)));
    }

    // --- Pawn captures ---

    @Test
    void testPawnCapturesDiagonally() {
        Pawn pawn = new Pawn(true, board.getSquare(4, 6), "wp.png");
        board.getSquare(4, 6).setOccupyingPiece(pawn);

        Piece enemy1 = new Rook(false, board.getSquare(3, 5), "br.png");
        Piece enemy2 = new Rook(false, board.getSquare(5, 5), "br.png");

        board.getSquare(3, 5).setOccupyingPiece(enemy1);
        board.getSquare(5, 5).setOccupyingPiece(enemy2);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertTrue(legalMoves.contains(board.getSquare(3, 5)));
        assertTrue(legalMoves.contains(board.getSquare(5, 5)));
    }

    @Test
    void testPawnCannotCaptureForward() {
        Pawn pawn = new Pawn(true, board.getSquare(4, 6), "wp.png");
        board.getSquare(4, 6).setOccupyingPiece(pawn);

        Piece enemy = new Rook(false, board.getSquare(4, 5), "br.png");
        board.getSquare(4, 5).setOccupyingPiece(enemy);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertFalse(legalMoves.contains(board.getSquare(4, 5)));
    }

    // --- Pawn promotion ---

    @Test
    void testPawnPromotionPossible() {
        Pawn pawn = new Pawn(true, board.getSquare(0, 1), "wp.png");
        board.getSquare(0, 1).setOccupyingPiece(pawn);

        List<Square> legalMoves = pawn.getLegalMoves(board);
        assertTrue(legalMoves.contains(board.getSquare(0, 0)));
    }

    @Test
    void testWhitePawnPromotionToQueen() {
        Square start = board.getSquare(0, 1);
        Square end = board.getSquare(0, 0);

        Pawn whitePawn = new Pawn(true, start, "wp.png");
        board.getWhitePieces().add(whitePawn);
        start.setOccupyingPiece(whitePawn);

        boolean moved = moveService.applyMove(whitePawn, end, board);

        assertTrue(moved, "Move should succeed");
        assertTrue(end.getOccupyingPiece() instanceof Queen, "Pawn should promote to Queen");
        assertTrue(end.getOccupyingPiece().isWhite(), "Promoted Queen should be white");
    }

    @Test
    void testBlackPawnPromotionToQueen() {
        Square start = board.getSquare(0, 6);
        Square end = board.getSquare(0, 7);

        Pawn blackPawn = new Pawn(false, start, "bp.png");
        board.getBlackPieces().add(blackPawn);
        start.setOccupyingPiece(blackPawn);

        boolean moved = moveService.applyMove(blackPawn, end, board);

        assertTrue(moved, "Move should succeed");
        assertTrue(end.getOccupyingPiece() instanceof Queen, "Pawn should promote to Queen");
        assertFalse(end.getOccupyingPiece().isWhite(), "Promoted Queen should be black");
    }


}
