

import client.controller.CheckmateDetector;
import client.view.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.King;
import shared.model.pieces.Pawn;
import shared.model.pieces.Rook;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private Board board;
    private Square[][] squares;
    private LinkedList<Piece> whitePieces;
    private LinkedList<Piece> blackPieces;
    private CheckmateDetector detector;
    private King whiteKing;
    private King blackKing;

    @BeforeEach
    public void setup() {
        board = new Board(null);
        squares = board.getSquareArray();
        whitePieces = new LinkedList<>();
        blackPieces = new LinkedList<>();
        whiteKing = new King(true, squares[7][4], "wking.png");
        blackKing = new King(false, squares[0][4], "bking.png");
        squares[7][4].setOccupyingPiece(whiteKing);
        squares[0][4].setOccupyingPiece(blackKing);
        whitePieces.add(whiteKing);
        blackPieces.add(blackKing);
        detector = new CheckmateDetector(board, whitePieces, blackPieces, whiteKing, blackKing);
    }

    @Test
    public void testNoCheckInitially() {
        assertFalse(detector.whiteInCheck());
        assertFalse(detector.blackInCheck());
    }

    @Test
    public void testWhiteCheckBlack() {
        Rook rook = new Rook(true, squares[1][4], "wrook.png");
        squares[1][4].setOccupyingPiece(rook);
        whitePieces.add(rook);
        detector.update();
        assertTrue(detector.blackInCheck());
    }

    @Test
    public void testCheckmateCondition() {
        Rook rook1 = new Rook(true, squares[1][3], "wrook.png");
        Rook rook2 = new Rook(true, squares[1][5], "wrook.png");
        whitePieces.add(rook1);
        whitePieces.add(rook2);
        squares[1][3].setOccupyingPiece(rook1);
        squares[1][5].setOccupyingPiece(rook2);
        detector.update();
        assertTrue(detector.blackCheckMated());
    }

    @Test
    public void testStalemateCondition() {
        blackKing.setPosition(squares[0][0]);
        squares[0][0].setOccupyingPiece(blackKing);
        whiteKing.setPosition(squares[7][7]);
        squares[7][7].setOccupyingPiece(whiteKing);

        Pawn whitePawn = new Pawn(true, squares[1][1], "wp.png");
        whitePieces.add(whitePawn);
        squares[1][1].setOccupyingPiece(whitePawn);

        detector.update();
        assertTrue(detector.blackStalemated());
    }
}